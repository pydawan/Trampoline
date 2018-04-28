package com.lucadev.trampoline.security.service;

import com.lucadev.trampoline.security.model.Privilege;

/**
 * Service for managing {@link Privilege} entities.
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
public interface PrivilegeService {

    /**
     * Create a new {@link Privilege}.
     *
     * @param privilege the {@link Privilege} name
     * @return the newly persisted {@link Privilege}
     */
    Privilege create(String privilege);

    /**
     * Create a new {@link Privilege}.
     *
     * @param privilege the {@link Privilege} name
     * @param target    the type that this privilege will be applied on
     * @return the newly persisted {@link Privilege}
     */
    Privilege create(String privilege, String target);

    /**
     * Remove a {@link Privilege}
     *
     * @param privilege the privilege name
     */
    void remove(String privilege);

    /**
     * Find a {@link Privilege} by it's name.
     *
     * @param privilegeName the {@link Privilege#name} attribute value.
     * @return the found {@link Privilege} or null.
     */
    Privilege find(String privilegeName);
}
