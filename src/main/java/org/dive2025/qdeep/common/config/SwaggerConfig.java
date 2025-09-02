package org.dive2025.qdeep.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
@Configuration
public class SwaggerConfig {

    /*
    전역 Swagger 설정
     */
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    @Bean
    public Info info(){
        return new Info()
                .title("Qdeep API")
                .description("Qdeep API 레퍼런스")
                .version("1.0");
    }

    /*
    유저 도메인 API 레퍼런스 설정
     */
    @Bean
    public GroupedOpenApi userAPI(){
        return GroupedOpenApi.builder()
                .group("유저 API")
                .pathsToMatch("/user/**","/auth/**")
                .addOpenApiCustomizer(userAPICustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer userAPICustomizer(){
        return openApi -> openApi.info(new Info()
                .title("유저 API")
                .description("유저 도메인을 위한 API")
                .version("1.0"));

    }


}
