package es.decroly.tienda_decroly.controllers;

import es.decroly.tienda_decroly.domain.Familia;
import es.decroly.tienda_decroly.services.FamiliaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Familias", description = "Familias de productos")
@RestController
@RequestMapping("/api/familias")
@RequiredArgsConstructor
public class FamiliaRestController {

    private final FamiliaService familiaService;

    @Operation(summary = "Listar todas las familias")
    @GetMapping
    public List<Familia> listar() {
        return familiaService.listar();
    }

    @Operation(summary = "Consultar una familia por su id")
    @GetMapping("/{id}")
    public Familia buscarPorId(@PathVariable Long id) {
        return familiaService.buscarPorId(id);
    }

    @Operation(summary = "Crear una familia")
    @PostMapping
    public ResponseEntity<Familia> crear(@RequestBody Familia familia) {
        Familia creada = familiaService.crear(familia);
        URI direccion = URI.create("/api/familias/" + creada.getId());
        return ResponseEntity.created(direccion).body(creada);
    }

    @Operation(summary = "Modificar una familia")
    @PutMapping("/{id}")
    public Familia modificar(@PathVariable Long id,
                             @RequestBody Familia datos) {
        return familiaService.modificar(id, datos);
    }

    @Operation(summary = "Borrar una familia")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        familiaService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
