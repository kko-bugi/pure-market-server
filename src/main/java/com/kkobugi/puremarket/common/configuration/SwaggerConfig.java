package com.kkobugi.puremarket.common.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("Pure Market API")
                .description("2024 Google Solution Challenge Pure Market Project")
                .version("v1.0.0");

        // Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Bearer");

        // Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("JWT");

        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes("Bearer", bearerAuth))
                .addSecurityItem(addSecurityItem);
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi usersApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi produceApi() {
        return GroupedOpenApi.builder()
                .group("produce")
                .pathsToMatch("/api/v1/produce/**")
                .build();
    }

    @Bean
    public GroupedOpenApi giveawayApi() {
        return GroupedOpenApi.builder()
                .group("giveaway")
                .pathsToMatch("/api/v1/giveaway/**")
                .build();
    }

    @Bean
    public GroupedOpenApi recipeApi() {
        return GroupedOpenApi.builder()
                .group("recipe")
                .pathsToMatch("/api/v1/recipe/**")
                .build();
    }

    @Bean
    public GroupedOpenApi homeApi() {
        return GroupedOpenApi.builder()
                .group("home")
                .pathsToMatch("/api/v1/home/**")
                .build();
    }
}