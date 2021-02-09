package com.tgfc.tw.security.config;

//import com.tgfc.tw.security.OAuth2AuthenticationEntryPoint;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

//@Configuration
//@EnableResourceServer
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfiguration  /*extends ResourceServerConfigurerAdapter*/ {

//	@Override
//	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//		resources.authenticationEntryPoint(new OAuth2AuthenticationEntryPoint());
//	}
//
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//                    .anyRequest().authenticated().and()
//                    .requestMatchers().antMatchers("/**");
//		http.csrf().disable().cors();
//	}
}
