package com.okdev.ems.config;

import com.okdev.ems.config.jwt.JwtFilter;
import com.okdev.ems.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtFilter jwtFilter;

//    @Autowired
//    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder);
//    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/category").hasRole("USER")
                .antMatchers("/api/users/register", "/api/users/auth").permitAll()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/user/*").hasRole("USER")
//                .anyRequest().authenticated()
//                .and()
//        .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/j_spring_security_check")
//                .failureUrl("/login?error")
//                .usernameParameter("j_login")
//                .passwordParameter("j_password")
//                .permitAll()
//                .and()
//        .logout()
//                .permitAll()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout");
                .and()
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
