package com.lucadev.trampoline.security.service.impl;

import com.lucadev.trampoline.security.model.User;
import com.lucadev.trampoline.security.service.UserAuthenticationService;
import com.lucadev.trampoline.security.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * {@link UserAuthenticationService} implementation.
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 27-4-18
 */
@AllArgsConstructor
public class TrampolineUserAuthenticationService implements UserAuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userService.update(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateUserState(User user) {
        if (!user.isEnabled()) {
            throw new DisabledException("Could not authorize user because the account is disabled.");
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("Could not authorize user because the account is expired.");
        }

        if (!user.isCredentialsNonExpired()) {
            throw new AccountExpiredException("Could not authorize user because the credentials are expired.");
        }

        if (!user.isAccountNonLocked()) {
            throw new LockedException("Could not authorize user because the account is locked.");
        }
    }


}
