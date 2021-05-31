package com.ourd.config.handler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

public class SecurityContextLogoutHandler {

	
	 	private boolean invalidateHttpSession;

		public void logout(HttpServletRequest request, HttpServletResponse response,
	 			Authentication authentication) {
	 		Assert.notNull(request, "HttpServletRequest required");
	 		if (invalidateHttpSession) {
	 			HttpSession session = request.getSession(false);
	 			if (session != null) {
	 				//Logger.debug("Invalidating session: " + session.getId());
	 				session.invalidate();
	 			}
	 		}
	 	}
}