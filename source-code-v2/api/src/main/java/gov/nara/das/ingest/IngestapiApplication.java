package gov.nara.das.ingest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class IngestapiApplication {

	public static void main(String[] args) {
		// jackson issue
		System.out.println("IngestapiApplication: "+com.fasterxml.jackson.databind.ObjectMapper.class.getProtectionDomain().getCodeSource().getLocation());
		SpringApplication.run(IngestapiApplication.class, args);
	}
}
