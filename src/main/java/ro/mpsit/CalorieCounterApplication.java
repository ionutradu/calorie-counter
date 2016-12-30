package ro.mpsit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
@ComponentScan
@SpringBootApplication
public class CalorieCounterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalorieCounterApplication.class, args);
	}
}
