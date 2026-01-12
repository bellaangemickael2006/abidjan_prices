package com.home.abidjanprices.repository;

import com.home.abidjanprices.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
