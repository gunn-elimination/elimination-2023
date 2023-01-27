package net.gunn.elimination;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
@EnableCaching
@Configuration
public class EliminationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EliminationApplication.class, args);
    }

	@Bean
	@Profile(value = "test")
	public EmbeddedDatabase dataSource() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			.addScript("org/springframework/session/jdbc/schema-h2.sql").build();
	}

	@ExceptionHandler(CannotCreateTransactionException.class)
	public String handleDataAccessResourceFailureException() {
		System.exit(1);
		return "try again"; // trololol
	}

	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}

}
