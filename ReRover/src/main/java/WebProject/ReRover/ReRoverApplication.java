package WebProject.ReRover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import WebProject.ReRover.config.FileStorageProperties;

import javax.sql.DataSource;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
@Configuration
public class ReRoverApplication {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String dbUsername;
    
    @Value("${spring.datasource.password}")
    private String dbPassword;

    public static void main(String[] args) {
        SpringApplication.run(ReRoverApplication.class, args);
    }

    @Bean
    public DataSource dataSource() {
        // First, create a basic data source
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        // Execute SQL scripts
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.addScript(new ClassPathResource("data.sql"));
        populator.setContinueOnError(true);
        
        try {
            populator.execute(dataSource);
        } catch (Exception e) {
            System.err.println("Error executing SQL scripts: " + e.getMessage());
        }

        return dataSource;
    }
}