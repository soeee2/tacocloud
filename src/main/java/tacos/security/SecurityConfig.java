package tacos.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import tacos.User;
import tacos.data.UserRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/design", "/orders/**")
                        .hasRole("USER")

                        .requestMatchers("/", "/login", "/register",
                                "/images/**", "/styles.css")
                        .permitAll()

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/design", true)
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {

        return username -> {

            User user = userRepo.findByUsername(username);

            if (user != null) {

                List<SimpleGrantedAuthority> authorities =
                        Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_USER"));

                return new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        authorities);
            }

            throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
        };
    }
}