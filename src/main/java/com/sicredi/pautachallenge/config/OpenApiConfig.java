package com.sicredi.pautachallenge.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pauta Challenge API")
                        .description("""
                                ## Sistema de Votação de Pautas
                                
                                Esta API permite gerenciar usuários, pautas e votos em um sistema de votação.
                                
                                ### Funcionalidades Principais:
                                - **Gestão de Usuários**: Cadastro, autenticação e gerenciamento de usuários
                                - **Gestão de Pautas**: Criação e consulta de pautas para votação
                                - **Sistema de Votos**: Registro e controle de votos dos usuários
                                - **Validação de CPF**: Sistema de validação automática de CPF
                                
                                ### Endpoints Disponíveis:
                                
                                #### Usuários (`/user`)
                                - `POST /user` - Criar novo usuário
                                
                                #### Autenticação (`/auth`)
                                - `POST /auth` - Login de usuário
                                
                                #### Pautas (`/section`)
                                - `GET /section` - Listar todas as pautas com contagem de votos
                                - `POST /section` - Criar nova pauta
                                
                                #### Votos (`/votes`)
                                - `POST /votes` - Registrar voto em uma pauta
                                
                                ### Tecnologias Utilizadas:
                                - **Spring Boot 3.1.12**
                                - **Spring Data JPA**
                                - **PostgreSQL** (produção) / **H2** (testes)
                                - **Spring Security** (criptografia)
                                - **Flyway** (migrações)
                                - **Lombok**
                                - **Validation API**
                                
                                ### Validações Implementadas:
                                - Validação de email único
                                - Validação de CPF único
                                - Validação de formato de CPF
                                - Validação de tamanho mínimo de campos
                                - Validação de voto único por usuário por pauta
                                
                                ### Tratamento de Erros:
                                - Mensagens de erro padronizadas
                                - Códigos de status HTTP apropriados
                                - Logs detalhados para debugging
                                
                                ### Segurança:
                                - Senhas criptografadas com BCrypt
                                - Validação de CPF para votos
                                - Controle de acesso por usuário
                                
                                ### Monitoramento:
                                - Logs estruturados
                                - Métricas de performance
                                - Rastreamento de operações
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pauta Challenge Team")
                                .email("contato@pautachallenge.com")
                                .url("https://github.com/pautachallenge"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.pautachallenge.com")
                                .description("Servidor de Produção")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT para autenticação"))
                );
    }
} 