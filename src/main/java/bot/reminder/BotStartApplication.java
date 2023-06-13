package bot.reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BotStartApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotStartApplication.class, args);
	}

}
