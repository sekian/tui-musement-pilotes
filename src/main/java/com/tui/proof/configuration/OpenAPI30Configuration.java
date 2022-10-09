package com.tui.proof.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPI30Configuration {

//    /**
//     * Configure the OpenAPI components.
//     *
//     * @return Returns fully configure OpenAPI object
//     * @see OpenAPI
//     */
//    @Bean
//    public OpenAPI customizeOpenAPI() {
//        //@formatter:off
//        final String securitySchemeName = "bearerAuth";
//        return new OpenAPI()
//                .addSecurityItem(new SecurityRequirement()
//                        .addList(securitySchemeName))
//                .components(new Components()
//                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
//                                .name(securitySchemeName)
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .description(
//                                        "Provide the JWT token. JWT token can be obtained from the Login API. For testing, use the credentials <strong>john/password</strong>")
//                                .bearerFormat("JWT")));
//        //@formatter:on
//
//    }
}