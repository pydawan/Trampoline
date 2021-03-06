# trampoline-security-jwt

Basic setup for enabling JWT authentication/authorization ontop of [trampoline-security](../trampoline-security)

## Architecture

This module embeds jwt authorization through the use of standard Spring interfaces such as:

- `AuthenticationEntryPoint`: `JwtAuthenticationEntryPoint` denies all requests by default
- `AuthenticationProvider`: `JwtAuthenticationProvider` accepts our own `AuthenticationToken` implementation
- `AbstractAuthenticationToken`: `JwtAuthenticationToken` contains the `JwtPayload` using for authorization
- `OncePerRequestFilter`: `JwtAuthorizationFilter` which is basically a security filter which invokes the `JwtAuthenticationProvider` through the `AuthenticationManager`

We then have the following interfaces and classes to work with:

- `JwtConfiguration`: Implement this to manage JWT token creation.
- `JwtPayload`: POJO of a JWT token.
- `TokenService`: implemented by `JwtTokenService` to manage tokens, obtain claims, etc...

By implementing already existing Spring interface we can easily switch authorization without having any deeply coupled jwt code in our projects.

The authorization and refresh controllers are part of the trampoline-security-jwt-web module.