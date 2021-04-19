package com.okdev.ems;

//import com.okdev.ems.config.jwt.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpenseManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseManagerApplication.class, args);
	}

//	@Bean
//	public FilterRegistrationBean<JwtFilter> filterRegistrationBean() {
//		FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
//		JwtFilter authFilter = new JwtFilter();
//		registrationBean.setFilter(authFilter);
//		registrationBean.addUrlPatterns("/admin/*");
//		registrationBean.addUrlPatterns("/user/*");
//		return registrationBean;
//	}

}
