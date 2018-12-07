# Trampoline
Jump straight into serious Spring development through the use of multiple ready-to-use Spring Boot starters.

Please check [trampoline-example-app](/trampoline-example-app) for an example application.

## About

Trampoline is a collection of Spring(boot) starters to speed up development by providing even more opinionated service.
However, a lot of services/components maintain configurable.

## Features

* User management(persisted through JPA, see trampoline-security)
* JPA base entity to handle JPA auditing, provides an UUID id, date create, and date updated.
* ABAC: extension of the user management starter. Configure policies using SpeL expressions, load them from JSON or persist them using JPA(can be imported from JSON too!).
Only the policy configurations are required to get started.
* RBAC: Simple RBAC implementation with hierarchy. With roles and privileges(authorities).
* JWT starter: import the JWT starter and have JWT enabled without any required configurations(expands on the user management)
* Asset storage: handle file uploads with ease without having to persist everything in the db as a blob.
* Asset storage decoupling: write your own AssetStore implementation to work with S3, GCP, Azure or your local filesystem.
* Ready to use frontend tools(JWT REST client, components, etc...)

## Getting started
Please read the [docs](/docs/README.md) for more information.

## Project Status

This project is still in early development. Please do not use it for any serious applications(yet).


## Contributing

If you wish to contribute please read the guidelines as described in [CONTRIBUTING.md](/CONTRIBUTING.md)

## License

This project is licensed under the [MIT License](/LICENSE.txt)

## Why use Trampoline

Some examples on why you could use Trampoline.

Setup role/privilege scheme:

```java
//Wrap inside SystemAuthentication SecurityContext
authorizationSchemeService.builder().wrapSystemAuthentication((wrappedBuilder) ->
        //Create user role
    wrappedBuilder.createRole("ROLE_USER").withPrivileges("WHOAMI_GET", "PING").buildAnd()
            //Create admin role, adds privileges from ROLE_USER to this role.
            .createRole("ROLE_ADMIN").withExistingRolePrivileges("ROLE_USER").withPrivileges("MANAGE_USERS").buildAnd()
            //Only build when dev profile is enabled
            .forProfile("dev", (devBuilder) ->
                //Add developer role
                devBuilder.createRole("ROLE_DEVELOPER").withPrivilege("DEVELOPER_ACCESS").buildAnd()
            )
);
```

You could also define the entire scheme in a json file and import that.