package org.dive2025.qdeep.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
                .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

    }

    /*
    가게 도메인 API 레퍼런스 설정
     */

    @Bean
    public GroupedOpenApi storeAPI(){
        return GroupedOpenApi.builder()
                .group("가게 API")
                .pathsToMatch("/store/**")
                .addOpenApiCustomizer(storeAPICustomized())
                .build();
    }

    @Bean
    public OpenApiCustomizer storeAPICustomized(){
        return openApi -> openApi.info(new Info()
                .title("가게 API")
                .description("가게 도메인을 위한 API")
                .version("1.0"));
    }

    /*
    가게 추천 API 레퍼런스 설정
     */

    @Bean
    public GroupedOpenApi recommendAPI(){
        return GroupedOpenApi.builder()
                .group("추천 API")
                .pathsToMatch("/gpt/chat")
                .addOpenApiCustomizer(recommendAPICustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer recommendAPICustomizer(){
        return openApi -> openApi.info(new Info()
                .title("추천 API")
                .description("가게 추천 전용 API - openAI Gpt API")
                .version("1.0"));

    }

    /*
    찜하기 도메인 API 레퍼런스 설정
     */

    @Bean
    public GroupedOpenApi favoritedAPI(){
        return GroupedOpenApi.builder()
                .group("찜하기 API")
                .pathsToMatch("/favorite/**")
                .addOpenApiCustomizer(favoriteAPICustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer favoriteAPICustomizer(){
        return openApi -> openApi.info(new Info()
                .title("찜하기 API")
                .description("찜하기 도메인을 위한 API")
                .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

    }

    /*
    글쓰기 도메인 API 레퍼런스 설정
     */

    @Bean
    public GroupedOpenApi boardAPI(){
        return GroupedOpenApi.builder()
                .group("글쓰기 API")
                .pathsToMatch("/board/**")
                .addOpenApiCustomizer(boardAPICustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer boardAPICustomizer(){
        return openApi -> openApi.info(new Info()
                .title("찜하기 API")
                .description("글쓰기 도메인을 위한 API")
                .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

    }

    /*
    파일 API 레퍼런스 설정
     */
    @Bean
    public GroupedOpenApi fileAPI(){
        return GroupedOpenApi.builder()
                .group("파일 API")
                .pathsToMatch("/S3File/**")
                .addOpenApiCustomizer(fileAPICustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer fileAPICustomizer(){
        return openApi -> openApi.info(new Info()
                .title("파일 API")
                .description("파일 업로드 및 삭제/로드 API")
                .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }


}
