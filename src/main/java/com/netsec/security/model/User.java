package com.netsec.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author George Karampelas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    private static final long PASSWORD_EXPIRATION_TIME
            = 30L * 1000L; // 5 minutes
    //            = 30L * 24L * 60L * 60L * 1000L; //30 days
    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String password;
    private String description;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "password_changed_time")
    private Date passwordChangedTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(() -> "read");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isPasswordExpired(){
        if(this.passwordChangedTime == null) return false;
        long currentTime = System.currentTimeMillis();
        long lastChangeTime = this.passwordChangedTime.getTime();
//        boolean expired = currentTime > lastChangeTime + PASSWORD_EXPIRATION_TIME;
//        System.err.println("curr " + expired);
//        long lastUntil = lastChangeTime + PASSWORD_EXPIRATION_TIME;
//        System.err.println("current time: " + currentTime + " and last: " + lastChangeTime + " and last until: " + lastUntil );
        return currentTime > lastChangeTime + PASSWORD_EXPIRATION_TIME;
    }
}
