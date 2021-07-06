package com.okdev.ems.config;

import com.okdev.ems.config.jwt.JwtAuthenticationSuccessHandler;
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
                .antMatchers("/api/users/id").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/category").hasRole("USER")
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/user/*").hasRole("USER")
                .antMatchers("/", "/index.html", "/api/users/register", "/api/users/auth", "/login", "/registration", "/js/**", "/css/**", "/vendor/**", "/fonts/**", "/images/**").permitAll()
//                .anyRequest().authenticated()
                .and()
        .formLogin()
                .loginPage("/login.html")
//                .loginProcessingUrl("/api/users/auth")
//                .failureUrl("/login?error")
                .usernameParameter("email")
                .passwordParameter("password")
//                .defaultSuccessUrl("/index.html")
                .successHandler(jwtAuthenticationSuccessHandler)
//                .permitAll()
                .and()
        .logout()
//                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .and()
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
