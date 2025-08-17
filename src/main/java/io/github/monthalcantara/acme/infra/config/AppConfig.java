package io.github.monthalcantara.acme.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Solicitações ACME")
                        .version("v1.0.0")
                        .description("API responsável pelo ciclo de vida das solicitações de apólice de seguro. "
                                + "Esta API permite a criação, consulta, atualização e exclusão de solicitações, "
                                + "facilitando a integração com sistemas de terceiros.")
                        .termsOfService("https://github.com/MonthAlcantara/Desafio-Seguros-PF")

                        .contact(new Contact()
                                .name("Montival Alcantara da Silva Júnior")
                                .email("montival_junior@yahoo.com.br")
                                .url("https://monthalcantara.github.io/"))

                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}