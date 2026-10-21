package edu.utez.mx.Prestamos_Libros.controller;
import edu.utez.mx.Prestamos_Libros.controller.dto.PrestamoResponseDTO;
import edu.utez.mx.Prestamos_Libros.controller.dto.SolicitudPrestamoDTO;
import edu.utez.mx.Prestamos_Libros.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<PrestamoResponseDTO> solicitarPrestamo(@Valid @RequestBody SolicitudPrestamoDTO dto) {
        PrestamoResponseDTO prestamo = prestamoService.solicitarPrestamo(dto);
        return new ResponseEntity<>(prestamo, HttpStatus.CREATED);
    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<PrestamoResponseDTO> devolverLibro(@PathVariable Long id) {
        PrestamoResponseDTO prestamo = prestamoService.devolverLibro(id);
        return ResponseEntity.ok(prestamo);
    }

    @PutMapping("/renovar/{id}")
    public ResponseEntity<PrestamoResponseDTO> renovarPrestamo(@PathVariable Long id) {
        PrestamoResponseDTO prestamo = prestamoService.renovarPrestamo(id);
        return ResponseEntity.ok(prestamo);
    }

    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<PrestamoResponseDTO>> listarPrestamosActivosLector(@PathVariable Long lectorId) {
        List<PrestamoResponseDTO> prestamos = prestamoService.listarPrestamosActivosLector(lectorId);
        return ResponseEntity.ok(prestamos);
    }
}