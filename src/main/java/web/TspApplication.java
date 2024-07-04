package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import web.services.MainPageService;


@SpringBootApplication
public class TspApplication extends MainPageService {

	public static void main(String[] args) {
		SpringApplication.run(TspApplication.class, args);
	}

}
