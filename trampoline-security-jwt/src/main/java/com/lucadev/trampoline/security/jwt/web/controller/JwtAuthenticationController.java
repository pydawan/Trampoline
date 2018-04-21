package com.lucadev.trampoline.security.jwt.web.controller;

import com.lucadev.trampoline.security.jwt.JwtService;
import com.lucadev.trampoline.security.jwt.authentication.AuthenticationService;
import com.lucadev.trampoline.security.jwt.authentication.UsernamePasswordAuthenticationPayload;
import com.lucadev.trampoline.security.jwt.model.AuthenticationTokenResponse;
import com.lucadev.trampoline.security.jwt.model.UserAuthenticationRequest;
import com.lucadev.trampoline.security.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService tokenService;

    /**
     * Login with username-password
     *
     * @param userAuthenticationRequest
     * @return
     */
    @PostMapping("/authorize")
    public AuthenticationTokenResponse submitAuthenticationTokenRequest(@RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        User user = authenticationService.authenticate(
                new UsernamePasswordAuthenticationPayload(userAuthenticationRequest.getUsername(),
                        userAuthenticationRequest.getPassword()));
        String token = tokenService.createToken(user);

        if (token == null || token.isEmpty()) {
            return new AuthenticationTokenResponse(false, null, "Could not authenticate user");
        }
        return new AuthenticationTokenResponse(true, token, "ok");
    }

    /**
     * Refresh token from current logged in request
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/refresh")
    public AuthenticationTokenResponse submitAuthenticationTokenRefreshRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshedToken = tokenService.processTokenRefreshRequest(request);
            return new AuthenticationTokenResponse(true, refreshedToken, "ok");
        } catch (Exception e) {
            //Catch exception to always return correct format
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new AuthenticationTokenResponse(false, null, e.getMessage());
        }
    }


}
