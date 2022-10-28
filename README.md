# Lab instructions

## Prerequisites
- JDK 17. Preferably [GrallVM JDK 17](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.3.0) or above
- a decent Java IDE like [STS](https://spring.io/tools), [VS code](https://code.visualstudio.com/download) or [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
- Git
- A local Keycloak instance ([instruction there](https://github.com/ch4mpy/spring-addons/tree/master/samples/tutorials)) with a `meetup-public` client for which 
  * authorization-code flow is enabled
  * `BARMAN`, `WAITER` 	and `CASHIER` roles are enabled
  * a few users are defined with above roles mapped
  * client roles mapper is enabled if roles where declared at client level rather than realm one

## Vanilla spring-boot-starter-oauth2-resource-server
- add a dependency to `org.springframework.boot:spring-boot-starter-oauth2-resource-server`
- add a `SecurityConf` class decorated with `@Configuration`
- define a `@Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { return http.build(); }`
- in this bean
  * configure an OAuth2 resource-server with JWT decoder: `http.oauth2ResourceServer().jwt();`
  * configure access-control: `http.authorizeHttpRequests().requestMatchers("/tables").permitAll().requestMatchers("/drinks").permitAll().anyRequest().authenticated();`
  * disable sessions: `http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);`
  * disable CSRF: `http.csrf().disable();`
- in properties file, configure JWT decoder: `spring.security.oauth2.resourceserver.jwt.issuer-uri=https://localhost:8443/realms/master`
- in `OrdersController`, add some `@PreAuthorize("hasAuthrority('...')")` access-control expressions
- test with Postman: https://www.getpostman.com/collections/813917d5e5d92eeecc70 with following OAuth2 authentication parameters:
  * redirect URI: as configured in Keycloak for `meetup-public` client
  * Auth URL: https://localhost:8443/realms/master/protocol/openid-connect/auth
  * Token URL: https://localhost:8443/realms/master/protocol/openid-connect/token
  * client ID: `meetup-public`
  * Scope: openid profile email offline_access roles

## spring-addons
Replace `spring-boot-starter-oauth2-resource-server` dependency with 
```xml
<dependency>
	<groupId>com.c4-soft.springaddons</groupId>
	<artifactId>spring-addons-webmvc-jwt-resource-server</artifactId>
	<version>6.0.3</version>
</dependency>
```
Replace `SecurityConfig` with
```java
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
```
Replace `issuer-uri` property with:
```properties
com.c4-soft.springaddons.security.issuers[0].location=https://localhost:8443/realms/master
com.c4-soft.springaddons.security.issuers[0].authorities.claims=realm_access.roles,resource_access.meetup-public.roles
com.c4-soft.springaddons.security.cors[0].path=/orders/**
com.c4-soft.springaddons.security.permit-all=/actuator/health/readiness,/actuator/health/liveness,/v3/api-docs/**,/tables,/drinks
```
Test with Postman it works the same

## Unit tests
- check the dependency to `com.c4-soft.springaddons:spring-addons-webmvc-jwt-test:6.0.3`
- fix `OrderController` to have `OrderControllerTest` pass

As we replaced the default `JwtAuthenticationToken` with `OAuthentication<OpenidClaimSet>` in security config, `@AuthenticationPrincipal` now injects an `OpenidClaimSet` (instead of a `Jwt`). This enables you to write expressions like:
```java
    @PreAuthorize("#order.placedBy eq #claims.subject")
    Object securedControllerMethod(@PathVariable(name = "orderId") Order order, @AuthenticationPrincipal OpenidClaimSet claims) {
        ...
    }
```

## To go further
Follow this [tutorials](https://github.com/ch4mpy/spring-addons/tree/master/samples/tutorials) and [samples](https://github.com/ch4mpy/spring-addons/tree/master/samples)

For Keycloak mapper sample (add private claims to tokens), you can refer to [this project](https://github.com/ch4mpy/user-proxies/tree/main/api#keycloak-mapper). Pay attention to resource files, Maven dependencies and implemented interfaces.