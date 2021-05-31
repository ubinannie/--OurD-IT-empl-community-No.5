package com.ourd.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	 @Override
	 public void onAuthenticationSuccess(HttpServletRequest request,
HttpServletResponse response, Authentication authentication)throws IOException,
 ServletException{
		 HttpSession session = request.getSession();
		 
		 session.setAttribute("login", authentication.getName() +"님 반갑습니다.");
		 
		 response.sendRedirect("/loginSuccess");
	 }
}