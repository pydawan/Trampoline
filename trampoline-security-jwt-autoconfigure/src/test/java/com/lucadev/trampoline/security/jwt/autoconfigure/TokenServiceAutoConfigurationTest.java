package com.lucadev.trampoline.security.jwt.autoconfigure;

import com.lucadev.trampoline.security.jwt.JwtPayload;
import com.lucadev.trampoline.security.jwt.JwtTokenService;
import com.lucadev.trampoline.security.jwt.TokenService;
import com.lucadev.trampoline.security.jwt.configuration.JwtConfiguration;
import com.lucadev.trampoline.security.jwt.configuration.JwtSecurityProperties;
import com.lucadev.trampoline.security.model.User;
import com.lucadev.trampoline.security.service.UserService;
import com.lucadev.trampoline.service.time.TimeProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
public class TokenServiceAutoConfigurationTest {
    private AnnotationConfigApplicationContext context;
    private JwtConfiguration jwtConfiguration;
    private JwtSecurityProperties jwtSecurityProperties;
    private TimeProvider timeProvider;
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        jwtSecurityProperties = mock(JwtSecurityProperties.class);
        timeProvider = mock(TimeProvider.class);
        userService = mock(UserService.class);
        jwtConfiguration = mock(JwtConfiguration.class);
        context = new AnnotationConfigApplicationContext();
    }

    @After
    public void tearDown() throws Exception {
        if (this.context != null) {
            this.context.close();
        }
        if (jwtSecurityProperties != null) {
            jwtSecurityProperties = null;
        }
        if (timeProvider != null) {
            timeProvider = null;
        }
        if (userService != null) {
            userService = null;
        }
        if (jwtConfiguration != null) {
            jwtConfiguration = null;
        }
    }

    @Test
    public void registersJwtTokenServiceAutomatically() {
        this.context.registerBean(TokenServiceAutoConfiguration.class, jwtConfiguration, jwtSecurityProperties,
                timeProvider, userService);
        this.context.refresh();
        TokenService tokenService = this.context.getBean(TokenService.class);
        assertThat(tokenService, instanceOf(JwtTokenService.class));
    }

    @Test
    public void customTokenServiceBean() {
        this.context.register(CustomTokenServiceConfig.class);
        this.context.registerBean(TokenServiceAutoConfiguration.class, jwtConfiguration, jwtSecurityProperties,
                timeProvider, userService);
        this.context.refresh();
        TokenService tokenService = this.context.getBean(TokenService.class);
        assertThat(tokenService, instanceOf(TestTokenService.class));
    }

    @Configuration
    protected static class CustomTokenServiceConfig {

        @Bean
        public TokenService tokenService() {
            return new TestTokenService();
        }
    }

    protected static class TestTokenService implements TokenService {

        @Override
        public String createToken(User user) {
            return null;
        }

        @Override
        public String refreshToken(String token) {
            return null;
        }

        @Override
        public JwtPayload getTokenData(String token) {
            return null;
        }

        @Override
        public JwtPayload getTokenDataFromRequest(HttpServletRequest request) {
            return null;
        }

        @Override
        public boolean isValidToken(JwtPayload jwtPayload, User user) {
            return false;
        }

        @Override
        public String processTokenRefreshRequest(HttpServletRequest request) {
            return null;
        }

        @Override
        public Authentication getAuthenticationToken(HttpServletRequest request) {
            return null;
        }
    }
}