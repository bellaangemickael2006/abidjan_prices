package com.home.abidjanprices.config;

import com.home.abidjanprices.model.Marche;
import com.home.abidjanprices.model.Produit;
import com.home.abidjanprices.model.PrixProduit;
import com.home.abidjanprices.model.Utilisateur;
import com.home.abidjanprices.repository.MarcheRepository;
import com.home.abidjanprices.repository.ProduitRepository;
import com.home.abidjanprices.repository.PrixProduitRepository;
import com.home.abidjanprices.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProduitRepository produitRepository;
    private final MarcheRepository marcheRepository;
    private final PrixProduitRepository prixProduitRepository;

    public DataInitializer(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, ProduitRepository produitRepository, MarcheRepository marcheRepository, PrixProduitRepository prixProduitRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.produitRepository = produitRepository;
        this.marcheRepository = marcheRepository;
        this.prixProduitRepository = prixProduitRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // admin user
        if (utilisateurRepository.findByEmail("admin@local").isEmpty()){
            Utilisateur admin = new Utilisateur();
            admin.setNom("Administrateur");
            admin.setEmail("admin@local");
            admin.setMotDePasse(passwordEncoder.encode("admin123"));
            admin.setRole(Utilisateur.Role.ADMIN);
            utilisateurRepository.save(admin);
        }

        // produits
        if (produitRepository.count() == 0){
            Produit p1 = new Produit(null, "Riz", "Céréales", "kg");
            Produit p2 = new Produit(null, "Igname", "Tubercules", "kg");
            produitRepository.saveAll(Arrays.asList(p1,p2));
        }

        // marchés
        if (marcheRepository.count() == 0){
            Marche m1 = new Marche(null, "Marché Treichville", "Treichville", "Treichville centre");
            Marche m2 = new Marche(null, "Marché Cocody", "Cocody", "Cocody 2");
            marcheRepository.saveAll(Arrays.asList(m1,m2));
        }

        // prix exemples
        if (prixProduitRepository.count() == 0){
            Produit prod = produitRepository.findAll().get(0);
            Marche marche = marcheRepository.findAll().get(0);
            PrixProduit pr1 = new PrixProduit(null, prod, marche, new BigDecimal("500.00"), LocalDate.now());
            PrixProduit pr2 = new PrixProduit(null, prod, marcheRepository.findAll().get(1), new BigDecimal("520.00"), LocalDate.now().minusDays(1));
            prixProduitRepository.saveAll(Arrays.asList(pr1, pr2));
        }
    }
}
