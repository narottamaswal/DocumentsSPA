package springboot.app;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication(scanBasePackages="springboot.app")
public class App {

	public static void main( String[] args ){
        SpringApplication.run(App.class, args);
    }
	
	
	
}
 

