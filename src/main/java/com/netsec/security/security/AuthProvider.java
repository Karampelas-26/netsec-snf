package com.netsec.security.security;

import com.netsec.security.model.Attempts;
import com.netsec.security.model.Logging;
import com.netsec.security.model.User;
import com.netsec.security.repository.AttemptsRepository;
import com.netsec.security.repository.LoggingRepository;
import com.netsec.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author George Karampelas
 */
@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {

    private static final int ATTEMPTS_LIMIT = 3;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AttemptsRepository attemptsRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoggingRepository loggingRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.err.println("eisai malalaks");
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> user = userRepository.findByUsername(username);

        if (user == null || !user.get().getUsername().equals(username)) {
            System.err.println("usr nmotoot found");
            throw new NoSuchElementException("Username not found");
        }
        if (!user.get().isAccountNonLocked()){
            System.err.println("accpimtt locked");
            throw new LockedException("Account is locked!");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            processFailedAttempts(user);
            System.err.println("wronogngn pass");
            throw new BadCredentialsException("Wrong password");
        }

        resetAttempts(user);
        Logging logging = new Logging();
        logging.setUsername(user.get().getUsername());
        loggingRepository.save(logging);

        return new UsernamePasswordAuthenticationToken(user,password);
    }

    private void resetAttempts(Optional<User> user) {
        Optional<Attempts> userAttempts = attemptsRepository.findAttemptsByUsername(user.get().getUsername());
        if(!userAttempts.isEmpty()){
            userAttempts.get().setAttempts(0);
            attemptsRepository.save(userAttempts.get());
        }
    }

    private void processFailedAttempts(Optional<User> user) {
        Optional<Attempts> userAttempts = attemptsRepository.findAttemptsByUsername(user.get().getUsername());
        if(userAttempts.isEmpty()) {
            Attempts attempts = new Attempts();
            attempts.setUsername(user.get().getUsername());
            attempts.setAttempts(1);
            attemptsRepository.save(attempts);
        } else {
            Attempts attempts = userAttempts.get();
            attempts.setAttempts(attempts.getAttempts() + 1);
            attemptsRepository.save(attempts);

            if (attempts.getAttempts() + 1 > ATTEMPTS_LIMIT) {
                user.get().setAccountNonLocked(false);
                userRepository.save(user.get());
                throw new LockedException("Too many invalid attempts. Account is locked!");
            }
        }
    }
//    private void processFailedAttempts(String username, User user) {
//        Optional<Attempts>
//                userAttempts = attemptsRepository.findAttemptsByUsername(username);
//        if (userAttempts.isEmpty()) {
//            Attempts attempts = new Attempts();
//            attempts.setUsername(username);
//            attempts.setAttempts(1);
//            attemptsRepository.save(attempts);
//        } else {
//            Attempts attempts = userAttempts.get();
//            attempts.setAttempts(attempts.getAttempts() + 1);
//            attemptsRepository.save(attempts);
//
//            if (attempts.getAttempts() + 1 >
//                    ATTEMPTS_LIMIT) {
//                user.setAccountNonLocked(false);
//                userRepository.save(user);
//                throw new LockedException("Too many invalid attempts. Account is locked!!");
//            }
//        }
//    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
