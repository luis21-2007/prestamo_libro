package edu.utez.mx.Prestamos_Libros.controller.dto;
import edu.utez.mx.Prestamos_Libros.model.EstadoPrestamo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PrestamoResponseDTO {
    private Long id;
    private Long lectorId;
    private String nombreLector;
    private Long libroId;
    private String tituloLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionMaxima;
    private EstadoPrestamo estado;
}