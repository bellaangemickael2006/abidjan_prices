package com.home.abidjanprices.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prix_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrixProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Produit produit;

    @ManyToOne(optional = false)
    private Marche marche;

    @Column(nullable = false)
    private BigDecimal prix;

    @Column(nullable = false)
    private LocalDate date;
}
