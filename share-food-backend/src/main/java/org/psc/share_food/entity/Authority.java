package org.psc.share_food.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Authority() {
    }

    public Authority(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Authority setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Authority setName(String name) {
        this.name = name;
        return this;
    }
}
