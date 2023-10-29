package pe.edu.cibertec.DAWII_CL2_CastilloTaraEnzo.configuration;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pe.edu.cibertec.DAWII_CL2_CastilloTaraEnzo.service.DetalleUsuarioService;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final DetalleUsuarioService detalleUsuarioService;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/login",
                                        "/auth/registrar",
                                        "/auth/guardarUsuario",
                                        "/resources/**",
                                        "/static/**",
                                        "/styles/**",
                                        "/scripts/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                ).formLogin(login ->
                        login.loginPage("/auth/login")
                                .defaultSuccessUrl("/auth/login")
                                .usernameParameter("nomusuario")
                                .passwordParameter("password")
                ).authenticationProvider(authenticationProvider())
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") // URL para cerrar sesión
                                .logoutSuccessUrl("/auth/login") // Redirigir a esta URL después del cierre de sesión
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID") // Eliminar cookies al cerrar sesión
                );

        return http.build();
    }



    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(detalleUsuarioService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

}