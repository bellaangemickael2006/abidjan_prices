package com.home.abidjanprices.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "marche")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Marche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String commune;

    private String localisation;
}
