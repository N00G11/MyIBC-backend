package com.app.MyIBC.GestionDesUtilisateur.Controller;

import com.app.MyIBC.GestionDesUtilisateur.entity.Tresorier;
import com.app.MyIBC.GestionDesUtilisateur.repository.TresorierRepository;
import com.app.MyIBC.GestionDesUtilisateur.service.TresorierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tresoriers")
@RequiredArgsConstructor
public class TresorierController {

    private final TresorierRepository tresorierRepository;
    private final TresorierService tresorierService;

    // Récupérer tous les trésoriers
    @GetMapping
    public ResponseEntity<List<Tresorier>> getAll() {
        return ResponseEntity.ok(tresorierRepository.findAll());
    }

    // Récupérer un trésorier par ID
    @GetMapping("/{id}")
    public ResponseEntity<Tresorier> getById(@PathVariable Long id) {
        Tresorier tresorier = tresorierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trésorier non trouvé"));
        return ResponseEntity.ok(tresorier);
    }

    // Ajouter un trésorier
    @PostMapping
    public ResponseEntity<Tresorier> save(@RequestBody Tresorier tresorier) {
        Tresorier saved = tresorierService.saveTresorier(tresorier);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Modifier un trésorier
    @PutMapping("/{id}")
    public ResponseEntity<Tresorier> update(@PathVariable Long id, @RequestBody Tresorier updatedData) {
        Tresorier existing = tresorierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trésorier non trouvé"));

        existing.setUsername(updatedData.getUsername());
        existing.setEmail(updatedData.getEmail());
        // (optionnel) update du mot de passe ou autres champs ?

        Tresorier updated = tresorierRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    // Supprimer un trésorier
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!tresorierRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trésorier non trouvé");
        }
        tresorierRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
