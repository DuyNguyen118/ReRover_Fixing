package WebProject.ReRover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import WebProject.ReRover.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class ReRoverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReRoverApplication.class, args);
    }
}