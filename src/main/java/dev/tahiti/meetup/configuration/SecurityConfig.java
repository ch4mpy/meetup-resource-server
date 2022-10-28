package dev.tahiti.meetup.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.c4_soft.springaddons.security.oauth2.OAuthentication;
import com.c4_soft.springaddons.security.oauth2.OpenidClaimSet;
import com.c4_soft.springaddons.security.oauth2.config.OAuth2AuthoritiesConverter;
import com.c4_soft.springaddons.security.oauth2.config.synchronised.OAuth2AuthenticationFactory;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {/**
	 * <p>
	 * Switch {@link Authentication} implementation for valid JWTs from Spring's
	 * default {@link JwtAuthenticationToken} to
	 * {@link OAuthentication}&lt;{@link OpenidClaimSet}&gt;
	 * </p>
	 * <p>
	 * Note that bean is used only if one of
	 * spring-addons-{webmvc|webflux}-{jwt|introspecting}-resource-server is on the
	 * classpath.
	 * </p>
	 * 
	 * @param authoritiesConverter
	 * @return
	 */
	@Bean
	OAuth2AuthenticationFactory authenticationBuilder(OAuth2AuthoritiesConverter authoritiesConverter) {
		return (bearerString, claims) -> new OAuthentication<>(new OpenidClaimSet(claims),
				authoritiesConverter.convert(claims), bearerString);
	}
}
