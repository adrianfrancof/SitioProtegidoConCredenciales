package com.edutecno.configuration;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

//clase de configuracion para manejar la autentificacion del usuario mediante AuthenticationSucessHandler
//AuthenticationSucessHandler es declarado y utilizado en la clase WebSecurityConfig
@Configuration
public class CustomAuthenticationSucessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		// se obtienen las autoridades que contiene el usuario luego de ser logueado para redirigirlo
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		
		if (roles.contains("ADMIN")) {
			response.sendRedirect("/admin");
		}else {
			response.sendRedirect("/client");
		}
	}
}