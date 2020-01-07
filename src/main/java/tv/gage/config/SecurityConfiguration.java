package tv.gage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private final String USER = "USER";

	@Autowired
    protected void configureGlobal(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles(USER);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                .httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/v1/**").hasRole(USER)
//                .antMatchers(HttpMethod.POST, "/v1/**").hasRole(USER)
//                .antMatchers(
//                        "/", "/csrf", 
//                        "/v2/api-docs", 
//                        "/webjars/**",
//                        "/swagger-resources/**",
//                        "/swagger-ui.html"
//                        ).hasRole(USER)
//                .and()
                .csrf().disable()
                .formLogin().disable();
    }

}
