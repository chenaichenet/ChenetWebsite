package pers.website.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger UI配置类
 *
 * @author ChenetChen
 * @since 2023/4/24 15:39
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Value("${swagger.enable}")
    private boolean enable;
    
    @Bean
    public Docket getUserDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("竹风小站接口")
                .description("竹风小站 API")
                .version("1.0")
                .build();
        
        return new Docket(DocumentationType.OAS_30)
                .enable(enable)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pers.website.web"))
                .paths(PathSelectors.any())
                .build();
    }
}
