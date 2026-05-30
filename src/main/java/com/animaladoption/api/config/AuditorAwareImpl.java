package com.animaladoption.api.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {

		var auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated()) {
			return Optional.of("SYSTEM");
		}

		return Optional.ofNullable(auth.getName());
	}
}