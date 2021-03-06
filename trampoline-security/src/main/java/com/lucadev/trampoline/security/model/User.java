package com.lucadev.trampoline.security.model;

import com.lucadev.trampoline.data.entity.TrampolineEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A {@link TrampolineEntity} and {@link UserDetails} to easily manage users.
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
@Entity
@Table(name = "TRAMPOLINE_USER")
@Setter
@Getter
public class User extends TrampolineEntity implements UserDetails {

    @NotBlank
    @Length(min = 3, max = 32)
    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_expired", nullable = false)
    private boolean expired = false;

    @Column(name = "is_locked", nullable = false)
    private boolean locked = false;

    @Column(name = "is_credentials_expired", nullable = false)
    private boolean credentialsExpired = false;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled = true;

    //UserDetails roles can never be null so we use eager loading for roles.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TRAMPOLINE_USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    @Column(name = "last_password_reset_at", nullable = false)
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordReset;

    @Column(name = "last_seen", nullable = false)
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSeen;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        getRoles().forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                    role.getPrivileges()
                            .forEach(privilege -> authorities.add(
                                    new SimpleGrantedAuthority(privilege.getName()))
                            );
                }
        );
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        return username != null ? username.equals(user.username) : user.username == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
