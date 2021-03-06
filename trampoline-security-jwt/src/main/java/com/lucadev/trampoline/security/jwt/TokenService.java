package com.lucadev.trampoline.security.jwt;

import com.lucadev.trampoline.security.model.User;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for interacting with JWT tokens
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
public interface TokenService {

	/**
	 * Create a new token
	 *
	 * @param user user to create token for.
	 * @return jwt token.
	 */
    String createToken(User user);

	/**
	 * Refresh an existing token without any checks
	 *
	 * @param token jwt
	 * @return refreshed jwt
	 */
    String refreshToken(String token);

	/**
	 * Get all token information
	 *
	 * @param token jwt string
	 * @return jwt DTO representation.
	 */
    JwtPayload getTokenData(String token);

    /**
     * Similar to {@link #getTokenData(String)} but this reads the request header instead of passing the raw token
     *
     * @param request http req
     * @return jwt dto.
     */
    JwtPayload getTokenDataFromRequest(HttpServletRequest request);

    /**
     * Validate a token
     *
     * @param jwtPayload the data read from the token
     * @param user       the user to validate the data on
     * @return if the token is valid with the given user
     */
    boolean isValidToken(JwtPayload jwtPayload, User user);

	/**
	 * Handle a request to refresh a token
	 *
	 * @param request http req
	 * @return jwt token string.
	 */
    String processTokenRefreshRequest(HttpServletRequest request);

	/**
	 * Read the header containing our token and create an {@link Authentication} object from it.
	 *
	 * @param request http req
	 * @return auth object.
	 */
    Authentication getAuthenticationToken(HttpServletRequest request);

}
