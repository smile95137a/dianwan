package com.one.onekuji.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.one.onekuji.service.CustomUserDetails;

public class SecurityUtils {

	public static final String ROLE_DEFAULT = "ROLE_XXX";

	public static Authentication getAuthenticationObject() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static CustomUserDetails getCurrentUserPrinciple() {
		Authentication authentication = getAuthenticationObject();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof CustomUserDetails) {
				return ((CustomUserDetails) principal);
			}
		}
		return null;
	}

}
