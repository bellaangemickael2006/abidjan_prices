package com.home.abidjanprices.controller;

import com.home.abidjanprices.dto.AuthRequest;
import com.home.abidjanprices.dto.AuthResponse;
import com.home.abidjanprices.model.Utilisateur;
import com.home.abidjanprices.repository.UtilisateurRepository;
import com.home.abidjanprices.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getMotDePasse()));
        String token = jwtUtil.generateToken(req.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur u){
        Optional<Utilisateur> existing = utilisateurRepository.findByEmail(u.getEmail());
        if(existing.isPresent()) return ResponseEntity.badRequest().body("Email déjà utilisé");
        u.setMotDePasse(passwordEncoder.encode(u.getMotDePasse()));
        if (u.getRole() == null) u.setRole(Utilisateur.Role.USER);
        Utilisateur saved = utilisateurRepository.save(u);
        String token = jwtUtil.generateToken(saved.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
