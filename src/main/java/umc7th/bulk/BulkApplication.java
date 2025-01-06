package umc7th.bulk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BulkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulkApplication.class, args);
	}

}
