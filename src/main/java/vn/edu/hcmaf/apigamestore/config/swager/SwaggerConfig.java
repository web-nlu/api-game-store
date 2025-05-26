package vn.edu.hcmaf.apigamestore.config.swager;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    /**
     * Configures OpenAPI documentation for the API Game Store.
     *
     * @return OpenAPI instance with custom information.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Game Store")
                        .version("1.0")
                        .description("Documentation for the API Game Store"));
    }
}
