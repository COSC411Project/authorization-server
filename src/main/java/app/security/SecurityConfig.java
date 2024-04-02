package app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import app.repositories.IUserRepository;
import app.security.user.CustomUserDetailsManager;
import app.security.user.UserAuthenticationProvider;

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
	ClientRegistration githubRegistration() {
		return CommonOAuth2Provider.GITHUB.getBuilder("github")
										  .clientId("b3471ad069f7baeff07a")
										  .clientSecret("a6fd1680c9109a561eee36a1c33a2a3820f5682f")
										  .build();
	}
	
	@Bean
	ClientRegistrationRepository clientRegistrationRepository() {
		var githubRegistration = githubRegistration();
		
		return new InMemoryClientRegistrationRepository(githubRegistration);
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
		config.addAllowedOrigin("http://localhost:5173");
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	OAuthConversionFilter oauthConversionFilter(CustomUserDetailsManager userDetailsManager) {
		return new OAuthConversionFilter(userDetailsManager);
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource, OAuthConversionFilter oauthConversionFilter) throws Exception {
		return http.cors((customizer) -> {
			customizer.configurationSource(corsConfigurationSource);
		}).csrf((customizer) -> {
			customizer.disable();
		}).oauth2Login((customizer) -> {
			customizer.loginPage("/login");
		}).formLogin((customizer) -> {
			customizer.loginPage("/login")
					  .usernameParameter("email")
					  .passwordParameter("password");
		}).authorizeHttpRequests((customizer) -> {
			customizer.requestMatchers("/login/**").permitAll();
			customizer.requestMatchers("/api/**").hasRole("DEV");
			customizer.anyRequest().hasAnyRole("USER", "DEV");
		}).addFilterAfter(oauthConversionFilter, OAuth2LoginAuthenticationFilter.class).build();
	}
}
