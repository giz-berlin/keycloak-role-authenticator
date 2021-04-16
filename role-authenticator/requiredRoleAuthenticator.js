AuthenticationFlowError = Java.type("org.keycloak.authentication.AuthenticationFlowError");

function authenticate(context) {
    var MANDATORY_ROLE = 'feature:authenticate';
    var username = user ? user.username : "anonymous";

    var client = session.getContext().getClient();

    LOG.debug("Checking access to authentication for client '" + client.getName() + "' through mandatory role '" + MANDATORY_ROLE + "' for user '" + username + "'");

    var mandatoryRole = client.getRole(MANDATORY_ROLE);

    if (mandatoryRole === null) {
        LOG.debug("No mandatory role '" + MANDATORY_ROLE + "' for client '" + client.getName() + "'");
        return context.success();
    }

    if (user.hasRole(mandatoryRole)) {
        LOG.info("Successful authentication for user '" + username + "' with mandatory role '" + MANDATORY_ROLE + "' for client '" + client.getName() + "'");
        return context.success();
    }

    LOG.info("Denied authentication for user '" + username + "' without mandatory role '" + MANDATORY_ROLE + "' for client '" + client.getName() + "'");
    return denyAccess(context, mandatoryRole);
}

function denyAccess(context, mandatoryRole) {
    var formBuilder = context.form();
    var client = session.getContext().getClient();
    var description = !mandatoryRole.getAttribute('deniedMessage').isEmpty() ? mandatoryRole.getAttribute('deniedMessage') : [''];
    var form = formBuilder
        .setAttribute('clientUrl', client.getRootUrl())
        .setAttribute('clientName', client.getName())
        .setAttribute('description', description[0])
        .createForm('denied-auth.ftl');
    return context.failure(AuthenticationFlowError.INVALID_USER, form);
}
