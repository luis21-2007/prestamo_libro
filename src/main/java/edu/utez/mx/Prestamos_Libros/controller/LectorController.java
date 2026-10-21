package edu.utez.mx.Prestamos_Libros.controller;
import edu.utez.mx.Prestamos_Libros.service.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectores")
public class LectorController {

    private final PrestamoService prestamoService;

    public LectorController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PutMapping("/rehabilitar/{id}")
    public ResponseEntity<String> rehabilitarLector(@PathVariable Long id) {
        prestamoService.rehabilitarLector(id);
        return ResponseEntity.ok("El lector con ID " + id + " ha sido rehabilitado exitosamente.");
    }
}