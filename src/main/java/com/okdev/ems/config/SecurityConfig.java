package com.okdev.ems.config;

import com.okdev.ems.config.jwt.JwtAuthenticationSuccessHandler;
import com.okdev.ems.config.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    public SecurityConfig(JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler) {
        this.jwtAuthenticationSuccessHandler = jwtAuthenticationSuccessHandler;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/admin/*").hasRole("ADMIN")
                .antMatchers("/api/users/id").hasRole("USER")
                .antMatchers("/api/category/*").hasRole("USER")
                .antMatchers("/api/transaction/*").hasRole("USER")
                .antMatchers("/api/users/register").permitAll()
                .antMatchers("/api/users/auth").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/admin.html").permitAll()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/registration.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/login.html")
                .and()
        .formLogin()
                .loginPage("/login.html")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(jwtAuthenticationSuccessHandler)
                .and()
        .logout()
                .logoutSuccessUrl("/login.html")
                .and()
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    public void configure(WebSecurity web) throws Exception {
        // Allow resources to be accessed without authentication
        web.ignoring()
                .antMatchers("/js/**")
                .antMatchers("/css/**")
                .antMatchers("/vendor/**")
                .antMatchers("/fonts/**")
                .antMatchers("/images/**")
                .antMatchers("/apidocs/**")
                .antMatchers("/v3/api-docs*")
                .antMatchers("/swagger-ui.html")
//                .antMatchers("/swagger-ui-custom.html")
                .antMatchers("/swagger-ui/**");
    }
}
