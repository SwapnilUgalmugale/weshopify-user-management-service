package com.weshopify.platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.weshopify.platform.exceptions.FilterChainExceptionHandler;

@Configuration
public class AppSecurityConfig {
	
	@Autowired
	private JwtAuthenticationService authnService;
	
	@Autowired
	private FilterChainExceptionHandler filterChainExceptionHandler;

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> {
				try {
					authorize
						//.requestMatchers(HttpMethod.POST, "/categories/**").permitAll()
					    .anyRequest().authenticated()
					    .and().csrf().disable().anonymous().disable()
					    .addFilterBefore(new JwtAuthnFilter(authnService), BasicAuthenticationFilter.class)
					    .addFilterAfter(filterChainExceptionHandler, LogoutFilter.class);
					   
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			});
//            .formLogin(Customizer.withDefaults());
        return http.build();
    }
	
	/*
	 * @Bean public InMemoryUserDetailsManager userDetailsService() { UserDetails
	 * user =
	 * User.withDefaultPasswordEncoder().username("admin").password("admin").roles(
	 * "ADMIN").build(); return new InMemoryUserDetailsManager(user); }
	 */

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
	}

}
