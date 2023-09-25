package ar.edu.uade.tpoapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import ar.edu.uade.tpoapi.security.filters.JwtAuthenticationFilter;
import ar.edu.uade.tpoapi.security.jwt.JwtUtils;
import ar.edu.uade.tpoapi.services.UserDetailsImpl;


@EnableWebSecurity
@Configuration
public class SecurityConfig{

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsImpl userDetailsImpl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,AuthenticationManager authenticationManager) throws Exception{

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return httpSecurity
                    .csrf(config -> config.disable())
                    .authorizeHttpRequests(auth -> {
                        auth.requestMatchers("/auth/validarDocumento").permitAll();
                        auth.requestMatchers("/auth/registrar").permitAll();
                        auth.anyRequest().authenticated();
                    })
                    .sessionManagement( session -> {
                        session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    })
                    .addFilter(jwtAuthenticationFilter)
                    .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsImpl)
        .passwordEncoder(passwordEncoder())
        .and().build();
    }


    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("K@r@7e33"));
    }
}
