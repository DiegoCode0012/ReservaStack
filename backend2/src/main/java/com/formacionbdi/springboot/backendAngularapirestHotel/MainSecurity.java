package com.formacionbdi.springboot.backendAngularapirestHotel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.formacionbdi.springboot.backendAngularapirestHotel.security.jwt.JwtEntryPoint;
import com.formacionbdi.springboot.backendAngularapirestHotel.security.jwt.JwtTokenFilter;
import com.formacionbdi.springboot.backendAngularapirestHotel.services.UserDetailsServiceImp;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MainSecurity {
    @Autowired
    UserDetailsServiceImp userDetailsServiceImpl;

    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    AuthenticationManager authenticationManager;

    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
	        builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
	        authenticationManager = builder.build();
	        http.authenticationManager(authenticationManager);
	        return http
	                .csrf(AbstractHttpConfigurer::disable)
	                .authorizeHttpRequests(authRequest ->
	                        authRequest
	                                .requestMatchers("/auth/**").permitAll()
	                                .requestMatchers("/api/uploads/img/**").permitAll() // Rutas libres
	                                .requestMatchers("/api/uploads/**").permitAll()
	                                .requestMatchers("/api/habitacionesdisponibles").permitAll()
	                                .requestMatchers("/api/habitaciones/idTipo/**").permitAll()
	                                .requestMatchers("/api/habitaciones/upload").permitAll()
	                                .requestMatchers("/api/reservas/obtenerGananciasMensuales/").permitAll()
	                                .requestMatchers("/api/reservas").permitAll()
	                                .requestMatchers("/auth/users/**").permitAll()
	                                .requestMatchers("/api/reservas/totalClientes/").permitAll()
	                                .requestMatchers("/api/reservas/obtenerGananciasMensuales").permitAll()
	                                .requestMatchers("/api/reservas/ganaciasxTipo/").permitAll()
	                                .requestMatchers("/api/tipos").permitAll()
	                                .anyRequest().authenticated()
	                )
	                .sessionManagement(sessionManager ->
	                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	                )
	                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
	                .exceptionHandling(handling -> handling.authenticationEntryPoint(jwtEntryPoint)) // Agregamos .and() para continuar con la construcción de la configuración
	                .build(); 
	    }
}
