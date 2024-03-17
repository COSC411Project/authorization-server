package app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import app.enums.Role;
import app.repositories.IUserRepository;

@Configuration
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	CustomUserDetailsManager customUserDetailsManager(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
		return new CustomUserDetailsManager(userRepository, passwordEncoder);
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsManager customUserDetailsManager) throws Exception {
		UserAuthenticationProvider provider = new UserAuthenticationProvider(customUserDetailsManager, passwordEncoder());
		
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.authenticationProvider(provider);
		
		return builder.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addAllowedOrigin("*");
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
		return http.cors((customizer) -> {
			customizer.configurationSource(corsConfigurationSource);
		}).csrf((customizer) -> {
			customizer.disable();
		}).formLogin((customizer) -> {
		}).authorizeHttpRequests((customizer) -> {
			customizer.anyRequest().hasRole("USER");
		}).build();
	}
}
