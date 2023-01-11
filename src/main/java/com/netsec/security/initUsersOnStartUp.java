package com.netsec.security;

import com.netsec.security.model.User;
import com.netsec.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author George Karampelas
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class initUsersOnStartUp implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * initialize two users, admin and p3180072
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .description("i am admin")
                .accountNonLocked(true)
                .passwordChangedTime(new Date())
                .build();
        User p3180072 = User.builder()
                .username("p3180072")
                .password(passwordEncoder.encode("pass"))
                .description("i am student with AM 3180072")
                .accountNonLocked(true)
                .passwordChangedTime(new Date())
                .build();
        userRepository.save(admin);
        userRepository.save(p3180072);
    }
}
