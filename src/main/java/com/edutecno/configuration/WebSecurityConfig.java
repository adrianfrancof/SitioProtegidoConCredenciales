package com.edutecno.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig {
	
	//especificar estrategia de autentificacion, y validar el ingreso del usuario
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	//constructor vacio que instancia el private AuthenticationSuccessHandler authenticationSuccessHandler;
//	public WebSecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler) {
//		this.authenticationSuccessHandler = authenticationSuccessHandler;
//	}
	
	//manejando la autentificacion del usuario recibiendo la configuracion de autentificacion al usuario
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	//configuracion de usuarios usados en memoria
//	@Bean
//    InMemoryUserDetailsManager userDetailsService() {
//		
//        UserDetails user = User.builder()
//            .username("correo@gmail.com")
//            .password(passwordEncoder().encode("admin"))
//            .roles("ADMIN")
//            .build();
//        
//        UserDetails user2 = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("user"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user,user2);
//    }
	
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable(); //configuracion para entrar a la base de datos H2 desde el navegador
		http
		.authorizeRequests()//autorizando los request recibidos de los antMatchers y verificacion de rol
		.antMatchers("/admin/**").hasAuthority("ADMIN")//.hasRole("ADMIN")
		.antMatchers("/client/**").hasAuthority("CLIENT")//.hasRole("USER")
		.antMatchers("/login")
		.permitAll()
		.anyRequest()//request que sea recibido a /login debe ser autenticado
		.authenticated()
		.and()
		.formLogin()//definiendo cual es la pagina inicial para el login
		.loginPage("/login")//URL para la pagina de login o inicio de sesión
		.successHandler(authenticationSuccessHandler)//manejando la autentificacion del usuario mediante el authenticationSuccessHandler
		.failureUrl("/login?error=true")//URL para un login fallido
		.usernameParameter("email")//nombre del parametro del input en el formulario
		.passwordParameter("password")//nombre del parametro para el password en el input del formulario
//		.defaultSuccessUrl("/default", true)
		.and()
		.exceptionHandling()//si ocurre un error de ingreso se ejecuta la pagina de recurso-prohibido
		.accessDeniedPage("/recurso-prohibido");
		
		return http.build();
	}
	
	@Bean 
	@Order(0)
	SecurityFilterChain resources(HttpSecurity http) throws Exception {
	    http
	        .requestMatchers((matchers) -> matchers.antMatchers("/images/**", "/js/**", "/webjars/**"))
	        .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
	        .requestCache().disable()
	        .securityContext().disable()
	        .sessionManagement().disable();

	    return http.build();
	}
	
//	@Bean
//    WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
//    }
	
	//metodo encargado de codificar la password
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
