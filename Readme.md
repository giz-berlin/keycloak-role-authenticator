# Keycloak role authenticator

[![pipeline status](https://rechenknecht.net/giz/keycloak/role-authenticator/badges/master/pipeline.svg)](https://rechenknecht.net/giz/keycloak/role-authenticator/-/commits/master)

This simple module allows authenticating for a Keycloak role before handing over the authentication flow back to a client. For example, you can allow access to an application only for a single group.

In contrast to the built-in `User Role Authenticator` this authenticator does not require you to create an own flow for every client which might have a different user set. This module allows you to add one role per client and assign users to those specific roles while having only central authentication flows. Additionally, with this module you can set a specific error message per client to let your users know, why they are not able to login.

## Installation

### Dependencies

To install this module, you need to place the generated jar-file to Keycloak deployments. For the official Docker containers, the Keycloak deployments are located in `/opt/jboss/keycloak/standalone/deployments`.

Furthermore, you have to use the additional templates defined in [our sample-theme](sample-theme). For installation instructions of the theme, please refer [to the theme Readme](sample-theme/Readme.md).

In order to allow execution of this module, you have to enable the option `feature.scripts`. You can do so by adding this line to your `profile.properties`:

```text
feature.scripts=enabled
```

For more information see [Keycloak Profiles](https://www.keycloak.org/docs/latest/server_installation/#profiles).

### Keycloak flows

In order to use the module during authentication, you need to include it in the authentication flows in Keycloak.

#### Default authentication flow

⚠ Please take care **using** all the flows you will set up in this section, e.g. by using the global flow bindings of your realm and checking for every client that there is not another flow defined there.

You need to add a default browser authentication flow like this:

![Default authentication flow, modified for the role-authenticator](docs/browser-auth-flow.png)

For the direct grant flow, you need something like:

![Direct grant flow](docs/direct-grant-flow.png)

If you are using external identity providers, it is very important to check the role after the provider authenticated the user. Therefore, you have to overwrite the `Post Login Flow` in your identity providers with a flow like this:

![Post login flow, needed for external Idp providers](docs/post-login-flow.png)

*In this flow, it is very important to use the `Browser Redirect/Refresh` after our `Required Role Authenticator` as you might encounter errors otherwise.*

⚠ If you use any other flows, make sure you don't need to include the `Required Role Authenticator` there as well.

## Usage

To require a role when authenticating for a client, you need to add the role `feature:authenticate` to a client. Only if this role exists, this modules will take action.

For all users, which should get access to such client, you need to assign them the role.

You can set a unique access denied message to the role `feature:authenticate` by adding an attribute `deniedMessage` to it, which will be shown when a user is missing this specific role to use the client.
