package berlin.giz.keycloak.roleauthenticator;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.events.Errors;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.keycloak.services.messages.Messages;

import javax.ws.rs.core.Response;

/**
 * {@link Authenticator} that requires a user to have a mandatory role.
 */
public class RequiredRoleAuthenticator implements Authenticator {

    private static final String DENIED_MESSAGE_ATTRIBUTE_KEY = "deniedMessage";
    private static final Logger LOGGER = Logger.getLogger(RequiredRoleAuthenticator.class);
    private static final String DEFAULT_DENIED_MESSAGE = Messages.NO_ACCESS;
    private static final String AUTH_CONFIG_MISSING = "Unable to get authenticator config for required role authenticator. " +
            "Have you forgotten to configure it?";
    private static final String REQUIRED_ROLE_EMPTY = "The required role could not be retrieved or is empty. " +
            "Have you forgotten to configure it?";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        ClientModel client = context.getAuthenticationSession().getClient();
        UserModel user = context.getUser();
        AuthenticatorConfigModel authenticatorConfig = context.getAuthenticatorConfig();

        if (authenticatorConfig == null) {
            LOGGER.error(AUTH_CONFIG_MISSING);
            context.getEvent().client(client).error(AUTH_CONFIG_MISSING);
            respondWithError(context, DEFAULT_DENIED_MESSAGE);
            return;
        }

        String requiredRoleName = authenticatorConfig.getConfig().get(RequiredRoleAuthenticatorFactory.REQUIRED_ROLE_NAME);
        if (requiredRoleName == null || requiredRoleName.trim().isBlank()) {
            LOGGER.error(REQUIRED_ROLE_EMPTY);
            context.getEvent().client(client).error(REQUIRED_ROLE_EMPTY);
            respondWithError(context, DEFAULT_DENIED_MESSAGE);
            return;
        }
        requiredRoleName = requiredRoleName.trim();

        LOGGER.debugf("Checking access to authentication for client %s through mandatory role %s", client.getClientId(), requiredRoleName);

        RoleModel requiredRole = client.getRole(requiredRoleName);
        if (requiredRole == null) {
            context.success();
            return;
        }

        if (user.hasRole(requiredRole)) {
            LOGGER.debugf("User %s successfully authenticated with role %s for client %s", user.getUsername(), requiredRoleName, client.getClientId());
            context.success();
            return;
        }

        LOGGER.infof("Authentication for user %s on client %s denied as mandatory role %s is missing", user.getUsername(), client.getClientId(), requiredRoleName);
        // Generates a LOGIN_ERROR event that will be logged in the admin UI if configured.
        // As the email event listener is activated by default, this will also send an email to the user trying to log in.
        context.getEvent().user(user).error(Errors.NOT_ALLOWED);

        String deniedMessage = requiredRole.getFirstAttribute(DENIED_MESSAGE_ATTRIBUTE_KEY);
        if (deniedMessage == null || deniedMessage.isEmpty()) {
            deniedMessage = DEFAULT_DENIED_MESSAGE;
        }

        respondWithError(context, deniedMessage);
    }

    public void respondWithError(AuthenticationFlowContext context, String deniedMessage) {
        LoginFormsProvider loginFormsProvider = context.form();

        Response errorForm = loginFormsProvider
                .setError(deniedMessage)
                .createErrorPage(Response.Status.FORBIDDEN);
        context.forceChallenge(errorForm);
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void action(AuthenticationFlowContext context) {

    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public void close() {

    }
}
