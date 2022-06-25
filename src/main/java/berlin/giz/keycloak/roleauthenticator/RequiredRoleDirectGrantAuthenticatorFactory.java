package berlin.giz.keycloak.roleauthenticator;

import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;

public class RequiredRoleDirectGrantAuthenticatorFactory extends RequiredRoleAuthenticatorFactory {

    private static final String PROVIDER_ID = "required-role-direct-grant";
    private static final RequiredRoleDirectGrantAuthenticator ROLE_AUTHENTICATOR = new RequiredRoleDirectGrantAuthenticator();

    @Override
    public String getDisplayType() {
        return "Required Role Authenticator for Direct Grant";
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return ROLE_AUTHENTICATOR;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
