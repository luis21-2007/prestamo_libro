package edu.utez.mx.Prestamos_Libros.service;

import edu.utez.mx.Prestamos_Libros.controller.dto.PrestamoResponseDTO;
import edu.utez.mx.Prestamos_Libros.controller.dto.SolicitudPrestamoDTO;

import java.util.List;

public interface PrestamoService {
    PrestamoResponseDTO solicitarPrestamo(SolicitudPrestamoDTO dto);
    PrestamoResponseDTO devolverLibro(Long prestamoId);
    PrestamoResponseDTO renovarPrestamo(Long prestamoId);
    void rehabilitarLector(Long lectorId);
    List<PrestamoResponseDTO> listarPrestamosActivosLector(Long lectorId);
}