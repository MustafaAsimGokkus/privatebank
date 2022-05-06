package com.bank.privatebnk.config;


import com.bank.privatebnk.security.AuthEntryPointJwt;
import com.bank.privatebnk.security.AuthTokenFilter;
import com.bank.privatebnk.security.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private UserDetailsServiceImpl userDetailsServiceImpl;

    private final AuthEntryPointJwt unauthorizedHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    protected AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }


//We set the object "userDetailsServiceImpl" and "passwordEncoder" for spring security
// that will be used for authentication and authorization.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());

    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //it is (CSRF) cross site request forgery.
        //I disable because this app(API app) open to the public.
        //It may be inconvenient when you are in under development
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll().and()
                .authorizeRequests().antMatchers("/register","/login","/message/visitor","/management/**")
                .permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    private static final String[] AUTH_LIST= {
//            "/index.html",
//            "/public/**",
//            "/v3/api-docs/**",
//            "/configuration/ui",
//            "/swagger-resources",
//            "/configuration/security",
//            "/swagger-ui/**",
//            "/webjars/**",
//            "/swagger/**"
//    };


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers(AUTH_LIST);
//    }


}
