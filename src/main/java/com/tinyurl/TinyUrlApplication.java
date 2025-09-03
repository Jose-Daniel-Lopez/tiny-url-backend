package com.tinyurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the TinyURL application.
 *
 * <p>This class serves as the primary configuration and bootstrap class for the TinyURL
 * Spring Boot application. It uses Spring Boot's autoconfiguration features to automatically
 * configure the application context based on the dependencies present in the classpath.</p>
 *
 * <p>The {@link SpringBootApplication} annotation is a convenience annotation that combines:</p>
 * <ul>
 *   <li>{@code @Configuration} - Marks the class as a source of bean definitions</li>
 *   <li>{@code @EnableAutoConfiguration} - Enables Spring Boot's autoconfiguration mechanism</li>
 *   <li>{@code @ComponentScan} - Enables component scanning for the current package and sub-packages</li>
 * </ul>
 *
 * <p><strong>Application Features:</strong></p>
 * <ul>
 *   <li>URL shortening service with custom alias support</li>
 *   <li>MongoDB integration for data persistence</li>
 *   <li>REST API endpoints for URL management</li>
 *   <li>Caching support for improved performance</li>
 *   <li>Comprehensive validation and error handling</li>
 *   <li>Analytics tracking with click count monitoring</li>
 * </ul>
 *
 * <p><strong>Configuration:</strong> The application can be configured through
 * {@code application.properties} or {@code application.yml} files, including
 * database connections, caching settings, and URL shortener behavior.</p>
 *
 * <p><strong>Running the Application:</strong></p>
 * <ul>
 *   <li>Development: Run this main method directly from IDE</li>
 *   <li>Production: Package as JAR and run with {@code java -jar tinyurl.jar}</li>
 *   <li>Docker: Can be containerized using standard Spring Boot Docker practices</li>
 * </ul>
 *
 * @author Jose
 * @since 1.0
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 */
@SpringBootApplication
public class TinyUrlApplication {

	/**
	 * Main method to start the TinyURL Spring Boot application.
	 *
	 * <p>This method serves as the entry point for the application, using Spring Boot's
	 * {@link SpringApplication#run(Class, String...)} method to bootstrap the application
	 * context, start the embedded web server, and initialize all configured beans.</p>
	 *
	 * <p><strong>Bootstrap Process:</strong></p>
	 * <ol>
	 *   <li>Creates and configures the Spring application context</li>
	 *   <li>Performs autoconfiguration based on classpath dependencies</li>
	 *   <li>Starts the embedded Tomcat web server (default)</li>
	 *   <li>Initializes all Spring beans and components</li>
	 *   <li>Begins listening for HTTP requests on configured port</li>
	 * </ol>
	 *
	 * <p><strong>Command Line Arguments:</strong> The application accepts standard
	 * Spring Boot command line arguments for configuration overrides, such as:</p>
	 * <ul>
	 *   <li>{@code --server.port=8081} - Override default port</li>
	 *   <li>{@code --spring.profiles.active=prod} - Activate specific profiles</li>
	 *   <li>{@code --spring.config.location=classpath:custom.properties} - Custom config location</li>
	 * </ul>
	 *
	 * <p><strong>Development Notes:</strong></p>
	 * <ul>
	 *   <li>The application runs on port 8080 by default</li>
	 *   <li>MongoDB must be available and configured for proper operation</li>
	 *   <li>Hot reloading can be enabled with spring-boot-devtools dependency</li>
	 * </ul>
	 *
	 * @param args command line arguments passed to the application,
	 *             which can include Spring Boot configuration properties
	 * @see SpringApplication#run(Class, String...)
	 */
	public static void main(String[] args) {
		SpringApplication.run(TinyUrlApplication.class, args);
	}
}
