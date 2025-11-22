package br.com.fiap.skillup.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // === BEAN QUE ESTÁ FALTANDO ===
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt é o padrão mais usado pra senha
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ROTAS PÚBLICAS
                        .requestMatchers(
                                "/",
                                "/home-public",
                                "/login",
                                "/register",
                                "/css/**",
                                "/images/**",
                                "/js/**",
                                "/webjars/**"
                        ).permitAll()

                        // ÁREA ADMIN
                        .requestMatchers("/app/admin/**").hasRole("ADMIN")

                        // ÁREA LOGADA
                        .requestMatchers("/app/**").authenticated()

                        // RESTANTE
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/app", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .permitAll()
                );

        return http.build();
    }
}
