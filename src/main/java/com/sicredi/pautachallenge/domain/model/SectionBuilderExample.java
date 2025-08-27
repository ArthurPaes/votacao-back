package com.sicredi.pautachallenge.domain.model;

import java.time.LocalDateTime;

public class SectionBuilderExample {

    public static void main(String[] args) {
        Section section1 = SectionBuilder.builder()
            .name("Pauta Importante")
            .description("Esta é uma descrição detalhada da pauta que será votada pelos associados")
            .expiration(10)
            .startNow()
            .build();

        System.out.println("=== Exemplo 1: Criação básica ===");
        System.out.println("Nome: " + section1.getName());
        System.out.println("Descrição: " + section1.getDescription());
        System.out.println("Expiração: " + section1.getExpiration() + " minutos");
        System.out.println("Início: " + section1.getStart_at());
        System.out.println();

        LocalDateTime customStartTime = LocalDateTime.of(2024, 1, 15, 14, 30, 0);
        Section section2 = SectionBuilder.builder()
            .name("Pauta com Horário Específico")
            .description("Pauta com horário de início personalizado")
            .expiration(30)
            .startAt(customStartTime)
            .build();

        System.out.println("=== Exemplo 2: Criação com horário personalizado ===");
        System.out.println("Nome: " + section2.getName());
        System.out.println("Início: " + section2.getStart_at());
        System.out.println();

        Section section3 = SectionBuilder.builder()
            .name("Pauta Encadeada")
            .description("Demonstração do encadeamento de métodos")
            .expiration(15)
            .startNow()
            .build();

        System.out.println("=== Exemplo 3: Criação com encadeamento de métodos ===");
        System.out.println("Nome: " + section3.getName());
        System.out.println("Descrição: " + section3.getDescription());
        System.out.println();

        Section section4 = SectionBuilder.builder()
            .name("Pauta Simples")
            .description("Pauta sem definir horário de início")
            .expiration(5)
            .build();

        System.out.println("=== Exemplo 4: Criação de múltiplas seções ===");
        System.out.println("Nome: " + section4.getName());
        System.out.println("Início: " + section4.getStart_at());
        System.out.println();

        System.out.println("=== Resumo ===");
        System.out.println("Total de seções criadas: 4");
        System.out.println("Builder pattern implementado com sucesso!");
    }
} 