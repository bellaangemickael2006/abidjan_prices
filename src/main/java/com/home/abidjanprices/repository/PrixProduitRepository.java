package com.home.abidjanprices.repository;

import com.home.abidjanprices.model.PrixProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PrixProduitRepository extends JpaRepository<PrixProduit, Long> {
    List<PrixProduit> findByProduitIdAndDate(Long produitId, LocalDate date);
    List<PrixProduit> findByProduitIdAndMarcheIdAndDate(Long produitId, Long marcheId, LocalDate date);

    @Query("SELECT p FROM PrixProduit p WHERE p.produit.id = :productId AND p.date BETWEEN :from AND :to")
    List<PrixProduit> findByProduitAndDateRange(@Param("productId") Long productId,
                                               @Param("from") LocalDate from,
                                               @Param("to") LocalDate to);

    @Query("SELECT MIN(p.prix), MAX(p.prix), AVG(p.prix) FROM PrixProduit p WHERE p.produit.id = :productId AND p.date = :date")
    Object[] findMinMaxAvgByProduitAndDate(@Param("productId") Long productId, @Param("date") LocalDate date);
}
