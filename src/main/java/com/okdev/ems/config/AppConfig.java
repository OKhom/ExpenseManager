package com.okdev.ems.config;

import com.okdev.ems.models.Currencies;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;
import com.okdev.ems.repositories.CurrencyRepository;
import com.okdev.ems.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner demo(final UserRepository userRepository, final CurrencyRepository currencyRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Currencies currency;
                String login = "admin@ok-expense.com";
                Long count = userRepository.getCountByEmail(login);
                if (count == 0) {
                    String passhash = "$2a$10$y2qGAuLAnCcpwHdBKL.Q1uQuHak.N08C8a8SSgWsgSyfOgYptxxym";
                    Users user = new Users("Admin", "", login, passhash, UserRole.ADMIN);
                    userRepository.save(user);
                }

                if (currencyRepository.findAll().size() == 0) {

                    currency = new Currencies("Euro", "EUR", "&#8364;");
                    currencyRepository.save(currency);

                    currency = new Currencies("US Dollar", "USD", "&#36;");
                    currencyRepository.save(currency);

                    currency = new Currencies("Ukrainian Hryvnia", "UAH", "&#8372;");
                    currencyRepository.save(currency);

                    currency = new Currencies("Russian Ruble", "RUR", "&#8381;");
                    currencyRepository.save(currency);

                    currency = new Currencies("British Pound", "GBP", "&#163;");
                    currencyRepository.save(currency);

                    currency = new Currencies("Polish Zloty", "PLZ", "zł");
                    currencyRepository.save(currency);

                    currency = new Currencies("Swiss Franc", "CHF", "Fr");
                    currencyRepository.save(currency);

                    currency = new Currencies("Japanese Yen", "JPY", "&#165;");
                    currencyRepository.save(currency);

                    currency = new Currencies("Chinese Yuan", "CNY", "&#165;");
                    currencyRepository.save(currency);
                }
            }
        };
    }
}
