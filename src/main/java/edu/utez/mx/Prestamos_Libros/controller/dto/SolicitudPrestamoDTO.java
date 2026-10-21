package edu.utez.mx.Prestamos_Libros.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitudPrestamoDTO {

    @NotNull(message = "El ID del lector es obligatorio.")
    private Long lectorId;

    @NotNull(message = "El ID del libro es obligatorio.")
    private Long libroId;
}