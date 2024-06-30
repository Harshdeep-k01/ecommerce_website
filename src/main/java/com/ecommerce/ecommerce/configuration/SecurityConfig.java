package com.ecommerce.ecommerce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        

        // Configure HttpSecurity
        http.authorizeRequests()
                // Permit all users to access these paths
                .antMatchers("/", "/shop", "/register", "/h2-console/**").permitAll()
                // Restrict access to /admin/** to users with the ADMIN role
                .antMatchers("/admin/**").hasRole("ADMIN")
                // All other requests require authentication
                .anyRequest().authenticated()
            .and()
                // Configure form login
                .formLogin()
                .permitAll()
                .failureUrl("/login?error=true") // Redirect to this URL on login failure
                .defaultSuccessUrl("/") // Redirect to this URL on login success
                .usernameParameter("email") // Set the username parameter
                .passwordParameter("password") // Set the password parameter
            .and()
                // Configure OAuth2 login
                .oauth2Login()
                .loginPage("/login") // The login page URL
                .successHandler(googleOAuth2SuccessHandler) // The success handler for OAuth2 login
            .and()
                // Configure logout
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL to trigger logout
                .logoutSuccessUrl("/login") // Redirect to this URL on logout success
                .invalidateHttpSession(true) // Invalidate the HTTP session
                .deleteCookies("JSESSIONID") // Delete the JSESSIONID cookie
            .and()
                .exceptionHandling()
            .and()
                .csrf().disable(); // Disable CSRF protection
    }
     
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // TODO Auto-generated method stub
        auth.userDetailsService(customUserDeitailService)
        .passwordEncoder(bCryptPasswordEncoder());
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        // TODO Auto-generated method stub
        // Configure web security to ignore certain requests
        web.ignoring().antMatchers("/resources/**", "/static/**", "/images/**", "/productimages/**", "/css/**","/js/**");
    }

    

    
}
