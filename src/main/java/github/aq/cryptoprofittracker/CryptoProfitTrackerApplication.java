package github.aq.cryptoprofittracker;

import java.util.concurrent.Executor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;




@SpringBootApplication
public class CryptoProfitTrackerApplication {
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
	  return new WebMvcConfigurerAdapter() {
	    @Override
	    public void addCorsMappings(CorsRegistry registry) {
	      registry.addMapping("/api/v1/*").allowedOrigins("http://localhost:8081");
	    }
	  };
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CryptoProfitTrackerApplication.class, args);
	}
	
	@Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5000);
        executor.setMaxPoolSize(5000);        
        executor.setThreadNamePrefix("trades-");
        executor.initialize();
        return executor;
    }
	
	@Component
	public class LoadDataOnStartup implements CommandLineRunner {
	    public void run(String... args) {
	    	//System.out.println("CommandLineRunnerBean 1");
	    	RestTemplate restTemplate = new RestTemplate();
	    	String result = restTemplate.getForObject("http://localhost:8080/api/v1/trades/parse", String.class);
	    	result = restTemplate.getForObject("http://localhost:8080/api/v1/assets/price", String.class);
	    }
	}
}
