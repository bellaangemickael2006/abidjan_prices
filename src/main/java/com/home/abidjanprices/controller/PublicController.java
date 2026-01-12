package com.home.abidjanprices.controller;

import com.home.abidjanprices.model.PrixProduit;
import com.home.abidjanprices.repository.PrixProduitRepository;
import com.home.abidjanprices.repository.ProduitRepository;
import com.home.abidjanprices.repository.MarcheRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final PrixProduitRepository prixRepo;
    private final ProduitRepository produitRepo;
    private final MarcheRepository marcheRepo;

    public PublicController(PrixProduitRepository prixRepo, ProduitRepository produitRepo, MarcheRepository marcheRepo) {
        this.prixRepo = prixRepo;
        this.produitRepo = produitRepo;
        this.marcheRepo = marcheRepo;
    }

    @GetMapping("/produits")
    public List<?> listProduits(){
        return produitRepo.findAll();
    }

    @GetMapping("/marches")
    public List<?> listMarches(){
        return marcheRepo.findAll();
    }

    @GetMapping("/prix")
    public List<PrixProduit> prixParProduitAndDate(@RequestParam Long produitId, @RequestParam String date){
        LocalDate d = LocalDate.parse(date);
        return prixRepo.findByProduitIdAndDate(produitId, d);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats(@RequestParam Long produitId, @RequestParam String date){
        LocalDate d = LocalDate.parse(date);
        Object[] res = prixRepo.findMinMaxAvgByProduitAndDate(produitId, d);
        Map<String,Object> map = new java.util.HashMap<>();
        if(res != null){
            map.put("min", res[0]);
            map.put("max", res[1]);
            map.put("avg", res[2]);
        }
        return map;
    }

    @GetMapping("/compare")
    public List<PrixProduit> compareByPrice(@RequestParam Long produitId, @RequestParam String date){
        LocalDate d = LocalDate.parse(date);
        List<PrixProduit> list = prixRepo.findByProduitIdAndDate(produitId, d);
        list.sort((a,b)-> a.getPrix().compareTo(b.getPrix()));
        return list;
    }

    @GetMapping("/historique")
    public List<PrixProduit> historique(@RequestParam Long produitId, @RequestParam String from, @RequestParam String to){
        LocalDate f = LocalDate.parse(from);
        LocalDate t = LocalDate.parse(to);
        return prixRepo.findByProduitAndDateRange(produitId, f, t);
    }
}
