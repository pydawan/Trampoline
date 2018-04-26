package com.lucadev.trampoline.security.authentication;

import com.lucadev.trampoline.security.exception.AuthenticationException;
import com.lucadev.trampoline.security.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

/**
 * {@link AuthenticationService} implementation based on username/password authentication.
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
@AllArgsConstructor
@Getter
public class UsernamePasswordAuthenticationService extends AbstractAuthenticationService<UsernamePasswordAuthenticationPayload> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernamePasswordAuthenticationService.class);
    private final AuthenticationManager authenticationManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedType(Class<? extends AuthenticationPayload> payload) {
        return payload.isAssignableFrom(UsernamePasswordAuthenticationPayload.class);
    }

    /**
     * Authenticate a {@link User} with {@link UsernamePasswordAuthenticationPayload}
     * {@inheritDoc}
     */
    @Override
    protected Optional<User> doAuthenticate(UsernamePasswordAuthenticationPayload authPayload) {
        String username = authPayload.getUsername();
        String password = authPayload.getPassword();
        if (username == null || password == null) {
            throw new NullPointerException("Could not authenticate: username or password is null");
        }
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            Object principal = auth.getPrincipal();
            if (principal == null) {
                throw new AuthenticationException("Failed authentication(null principal)");
            }

            if (principal instanceof User) {
                LOGGER.debug("Authenticated user \"{}\" with success", username);
                return Optional.of((User) principal);
            } else {
                throw new AuthenticationException("Unexpected principal type(" + principal.getClass().getName() + "): " + principal);
            }
        } catch (DisabledException e) {
            throw new AuthenticationException("User \"" + username + "\" is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials for user \"" + username + "\"", e);
        }
    }
}
