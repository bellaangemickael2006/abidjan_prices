package com.home.abidjanprices.controller;

import com.home.abidjanprices.model.Marche;
import com.home.abidjanprices.model.Produit;
import com.home.abidjanprices.model.PrixProduit;
import com.home.abidjanprices.repository.MarcheRepository;
import com.home.abidjanprices.repository.ProduitRepository;
import com.home.abidjanprices.repository.PrixProduitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProduitRepository produitRepo;
    private final MarcheRepository marcheRepo;
    private final PrixProduitRepository prixRepo;

    public AdminController(ProduitRepository produitRepo, MarcheRepository marcheRepo, PrixProduitRepository prixRepo) {
        this.produitRepo = produitRepo;
        this.marcheRepo = marcheRepo;
        this.prixRepo = prixRepo;
    }

    @PostMapping("/produits")
    public Produit createProduit(@RequestBody Produit p){
        return produitRepo.save(p);
    }

    @PostMapping("/marches")
    public Marche createMarche(@RequestBody Marche m){
        return marcheRepo.save(m);
    }

    @PostMapping("/prix")
    public PrixProduit createPrix(@RequestBody PrixProduit p){
        if (p.getDate() == null) p.setDate(LocalDate.now());
        return prixRepo.save(p);
    }

    @DeleteMapping("/prix/{id}")
    public ResponseEntity<?> deletePrix(@PathVariable Long id){
        prixRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
