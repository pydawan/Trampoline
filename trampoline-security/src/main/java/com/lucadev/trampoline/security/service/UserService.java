package com.lucadev.trampoline.security.service;

import com.lucadev.trampoline.security.authentication.IdentificationType;
import com.lucadev.trampoline.security.exception.CurrentUserNotFoundException;
import com.lucadev.trampoline.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service to manage {@link User} entities.
 * Extends {@link UserDetailsService} to implement Spring Security.
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
public interface UserService extends UserDetailsService {

    /**
     * Obtain the {@link User} from the current {@link Thread}
     *
     * @return currently active {@link User} inside an {@link Optional}
     */
    Optional<User> currentUser();

    /**
     * Obtains the {@link User} from the current {@link Thread}.
     * Throws a {@link CurrentUserNotFoundException} when null.
     *
     * @return currently active {@link User}
     */
    User currentUserOrThrow();

    /**
     * Find a {@link User} by it's {@link User#getId()}
     *
     * @param subject the {@link UUID} to find.
     * @return null or the found {@link User}
     */
    User findById(UUID subject);

    /**
     * Persist new {@link User}
     *
     * @param user the {@link User} to persist
     * @return the persisted {@link User}
     */
    User save(User user);

    /**
     * Update a {@link User}
     *
     * @param user the {@link User} containing updated data.
     * @return the updated {@link User} after being updated.
     */
    User update(User user);

    /**
     * Update {@link User#getUpdated()} value.
     *
     * @param user the {@link User} to update.
     * @return the updated {@link User}
     */
    User updateLastSeen(User user);

    /**
     * Get a {@link List} of all users
     *
     * @return a {@link List} of all {@link User} entities.
     */
    List<User> findAll();

    /**
     * Find all users but paged.
     *
     * @param pageable paging information.
     * @return a {@link Page} of {@link User} entities.
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Set user enabled to false.
     *
     * @param user the {@link User} to disable.
     * @return the updated, now disabled {@link User}
     */
    User disable(User user);

    /**
     * Set user enabled to true.
     *
     * @param user the {@link User} to enable.
     * @return the updated, enabled {@link User}
     */
    User enable(User user);

    /**
     * Set the user enabled flag
     *
     * @param user    the {@link User} to set the flag of.
     * @param enabled enabled flag
     * @return the updated {@link User}
     */
    User setEnabled(User user, boolean enabled);

    /**
     * Expire a {@link User}
     *
     * @param user the {@link User} to expire.
     * @return the updated, expired {@link User}
     */
    User expire(User user);

    /**
     * Set the user expired. flag
     *
     * @param user    the {@link User} to set the flag on.
     * @param expired the new flag value.
     * @return the updated {@link User}
     */
    User setExpired(User user, boolean expired);

    /**
     * Lock a {@link User}
     *
     * @param user the {@link User} to lock.
     * @return the updated {@link User}
     */
    User lock(User user);

    /**
     * Unlock a {@link User}
     *
     * @param user the {@link User} to unlock.
     * @return the updated {@link User}
     */
    User unlock(User user);

    /**
     * Set the user locked flag.
     *
     * @param user   the {@link User} to apply the flag on.
     * @param locked the new locked value.
     * @return the updated {@link User}
     */
    User setLocked(User user, boolean locked);

    /**
     * Expires the {@link User} credentials.
     *
     * @param user the {@link User} to expire the credentials on.
     * @return the updated {@link User}
     */
    User expireCredentials(User user);

    /**
     * Set the user credentials expired flag.
     *
     * @param user    the {@link User} to apply the flag on.
     * @param expired the new flag value.
     * @return the updated {@link User}
     */
    User setCredentialsExpired(User user, boolean expired);

    /**
     * Similar to {@link UserDetailsService#loadUserByUsername(String)}
     *
     * @param email user email.
     * @return resolved user.
     */
    UserDetails loadUserByEmail(String email);

    /**
     * Current {@link IdentificationType} being used
     *
     * @return method of identification(username/email)
     */
    IdentificationType getIdentificationType();

    /**
     * Set a new {@link IdentificationType}
     *
     * @param identificationType if we use username or email for authorization.
     */
    void setIdentificationType(IdentificationType identificationType);
}
