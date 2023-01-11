package com.netsec.security.auth;

import com.netsec.security.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

/**
 * @author George Karampelas
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@ControllerAdvice
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        System.err.println("sadfhasdf");
        try {
            return ResponseEntity.ok(authenticationService.login(request));
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("usrrrr " + noSuchElementException);
            throw new ApiRequestException("User not found");
        } catch (BadCredentialsException badCredentialsException) {
            System.err.println("sadf " + badCredentialsException);
            throw new ApiRequestException("Bad credentials");
        } catch (LockedException lockedException) {
            System.err.println(lockedException);
            throw new ApiRequestException("Account is locked");
        }
    }

    @GetMapping("/loggings/{username}")
    public ResponseEntity<LoggingResponse> getLogging(@PathVariable String username){
        return ResponseEntity.ok(authenticationService.getLoggigns(username));
    }
}
