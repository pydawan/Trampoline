package com.lucadev.trampoline.security.configuration;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.lucadev.trampoline.security.configuration.TrampolineWebSecurityDevelopmentConfiguration.DEV_SECURITY_CONFIGURATION_ORDER;
import static com.lucadev.trampoline.security.configuration.TrampolineWebSecurityDevelopmentConfiguration.DEV_SECURITY_CONFIGURATION_PROFILE;

/**
 * Configure websecurity for dev profile to disable security on the H2 console, etc..
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
@Configuration
@Profile(DEV_SECURITY_CONFIGURATION_PROFILE)
@Order(DEV_SECURITY_CONFIGURATION_ORDER)
public class TrampolineWebSecurityDevelopmentConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * The {@link Order} of this configuration
     */
    public static final int DEV_SECURITY_CONFIGURATION_ORDER = 90;
    /**
     * The profile which activates this configuration
     */
    public static final String DEV_SECURITY_CONFIGURATION_PROFILE = "dev";

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/console/**/**");
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                // Un-secure H2 Database
                .antMatchers("/console/**/**").permitAll();
        // disable page caching
        http
                .headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();

        http.authorizeRequests().antMatchers("/console/**").permitAll()
                .and()
                .headers().frameOptions().disable();
    }

    /**
     * Strictly used in dev profile to load h2 console
     *
     * @return h2 console bean.
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}
