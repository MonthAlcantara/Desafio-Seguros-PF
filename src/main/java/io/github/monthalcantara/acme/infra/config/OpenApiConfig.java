package io.github.monthalcantara.acme.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Solicitações ACME")
                        .version("v1.0.0")
                        .description("API responsável pelo ciclo de vida das solicitações de apólice de seguro.")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}