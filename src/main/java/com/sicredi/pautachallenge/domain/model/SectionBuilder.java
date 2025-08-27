package com.sicredi.pautachallenge.domain.model;

import java.time.LocalDateTime;

/**
 * Builder para construção de objetos Section.
 * Permite a criação passo a passo de objetos Section de forma mais legível e flexível.
 */
public class SectionBuilder {
    private String name;
    private String description;
    private Integer expiration;
    private LocalDateTime startAt;

    /**
     * Define o nome da seção.
     * @param name nome da seção
     * @return this builder para encadeamento
     */
    public SectionBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Define a descrição da seção.
     * @param description descrição da seção
     * @return this builder para encadeamento
     */
    public SectionBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Define o tempo de expiração da seção em minutos.
     * @param expiration tempo de expiração em minutos
     * @return this builder para encadeamento
     */
    public SectionBuilder expiration(Integer expiration) {
        this.expiration = expiration;
        return this;
    }

    /**
     * Define a data e hora de início da seção.
     * @param startAt data e hora de início
     * @return this builder para encadeamento
     */
    public SectionBuilder startAt(LocalDateTime startAt) {
        this.startAt = startAt;
        return this;
    }

    /**
     * Define a data e hora de início como o momento atual.
     * @return this builder para encadeamento
     */
    public SectionBuilder startNow() {
        this.startAt = LocalDateTime.now();
        return this;
    }

    /**
     * Constrói e retorna o objeto Section com os valores configurados.
     * @return objeto Section construído
     */
    public Section build() {
        Section section = new Section();
        section.setName(this.name);
        section.setDescription(this.description);
        section.setExpiration(this.expiration);
        section.setStart_at(this.startAt != null ? this.startAt : LocalDateTime.now());
        return section;
    }

    /**
     * Método estático para criar uma nova instância do builder.
     * @return nova instância do SectionBuilder
     */
    public static SectionBuilder builder() {
        return new SectionBuilder();
    }
} 