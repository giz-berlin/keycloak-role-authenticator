package berlin.giz.keycloak.roleauthenticator;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.representations.idm.OAuth2ErrorRepresentation;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * {@link RequiredRoleAuthenticator} for the direct grant flow.
 */
public class RequiredRoleDirectGrantAuthenticator extends RequiredRoleAuthenticator {

    private static final String MISSING_REQUIRED_ROLE_ERROR = "missing_required_role";

    @Override
    public void respondWithError(AuthenticationFlowContext context, String deniedMessage) {
        OAuth2ErrorRepresentation errorRepresentation = new OAuth2ErrorRepresentation(MISSING_REQUIRED_ROLE_ERROR, deniedMessage);
        Response errorResponse = Response
                .status(Response.Status.FORBIDDEN.getStatusCode())
                .entity(errorRepresentation)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
        context.failure(AuthenticationFlowError.INVALID_USER, errorResponse);
    }
}
