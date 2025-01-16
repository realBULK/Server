package umc7th.bulk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class BulkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulkApplication.class, args);
	}

}
