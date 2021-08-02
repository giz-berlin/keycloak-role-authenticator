package berlin.giz.keycloak.roleauthenticator;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Collections;
import java.util.List;

public class RequiredRoleAuthenticatorFactory implements AuthenticatorFactory {

    private static final String PROVIDER_ID = "required-role-authenticator";
    private static final String DEFAULT_REQUIRED_ROLE = "feature:authenticate";

    public static final String REQUIRED_ROLE_NAME = "required-role";
    public static final RequiredRoleAuthenticator ROLE_AUTHENTICATOR = new RequiredRoleAuthenticator();

    @Override
    public String getDisplayType() {
        return "Required Role Authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Requires the user to have the configured required role";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        ProviderConfigProperty role = new ProviderConfigProperty();
        role.setDefaultValue(DEFAULT_REQUIRED_ROLE);

        role.setType(ProviderConfigProperty.STRING_TYPE);
        role.setName(REQUIRED_ROLE_NAME);
        role.setLabel("Name of the required role");
        role.setHelpText("Name of the required role a user needs to have for a successful authentication. " +
                "The role must be present on the client that wants to authenticate. If the role is not present, the authentication will pass.");
        return Collections.singletonList(role);
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return ROLE_AUTHENTICATOR;
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
