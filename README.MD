# Lab instructions

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
- test with Postman: https://www.getpostman.com/collections/813917d5e5d92eeecc70

## spring-addons
Replace `spring-boot-starter-oauth2-resource-server` dependency with 
```xml
<dependency>
	<groupId>com.c4-soft.springaddons</groupId>
	<artifactId>spring-addons-webmvc-jwt-resource-server</artifactId>
	<version>6.0.3</version>
</dependency>
```
Flush `SecurityConfig` content
```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
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
- add a dependency to `com.c4-soft.springaddons:spring-addons-oauth2-test:6.0.3`
- create a Jupiter (JUnit 5) test class for `OrderController`
- add some @Test for various end-points with access-control rules. Use `@WithMockOidcAuth` to mock users identities and assert that HTTP response statuses match `@PreAuthorize` statements.