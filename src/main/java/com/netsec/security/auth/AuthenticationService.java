package com.netsec.security.auth;

import com.netsec.security.config.JwtService;
import com.netsec.security.model.Logging;
import com.netsec.security.model.User;
import com.netsec.security.repository.LoggingRepository;
import com.netsec.security.repository.UserRepository;
import com.netsec.security.security.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author George Karampelas
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthProvider authProvider;
    private final LoggingRepository loggingRepository;

    public LoginResponse login(LoginRequest request){

        authProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        boolean isExpired = user.isPasswordExpired();
        return LoginResponse.builder()
                .response("Successful log in")
                .token(jwtToken)
                .isPasswordExpired(isExpired)
                .build();
    }

    public LoggingResponse getLoggigns(String username) {
        List<Logging> logging = loggingRepository.findAllByUsername(username);
        return LoggingResponse.builder()
                .response("Successful loggings: " + logging.size())
                .loggingList(logging)
                .build();
    }
}
