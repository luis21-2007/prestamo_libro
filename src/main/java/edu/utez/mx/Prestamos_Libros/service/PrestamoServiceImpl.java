package edu.utez.mx.Prestamos_Libros.service;

import edu.utez.mx.Prestamos_Libros.controller.dto.PrestamoResponseDTO;
import edu.utez.mx.Prestamos_Libros.controller.dto.SolicitudPrestamoDTO;
import edu.utez.mx.Prestamos_Libros.exception.BusinessRuleException;
import edu.utez.mx.Prestamos_Libros.exception.ResourceNotFoundException;
import edu.utez.mx.Prestamos_Libros.model.EstadoPrestamo;
import edu.utez.mx.Prestamos_Libros.model.Lector;
import edu.utez.mx.Prestamos_Libros.model.Libro;
import edu.utez.mx.Prestamos_Libros.model.Prestamo;
import edu.utez.mx.Prestamos_Libros.repository.LectorRepository;
import edu.utez.mx.Prestamos_Libros.repository.LibroRepository;
import edu.utez.mx.Prestamos_Libros.repository.PrestamoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository,
                               LectorRepository lectorRepository,
                               LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public PrestamoResponseDTO solicitarPrestamo(SolicitudPrestamoDTO dto) {

        Lector lector = lectorRepository.findById(dto.getLectorId())
                .orElseThrow(() -> new ResourceNotFoundException("El lector con ID " + dto.getLectorId() + " no existe."));

        if (!lector.isActivo()) {
            throw new BusinessRuleException("El lector se encuentra inactivo (bloqueado).");
        }

        Libro libro = libroRepository.findById(dto.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("El libro con ID " + dto.getLibroId() + " no existe."));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new BusinessRuleException("El libro '" + libro.getTitulo() + "' no cuenta con ejemplares disponibles.");
        }


        List<EstadoPrestamo> estadosActivos = Arrays.asList(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
        long prestamosActivosCount = prestamoRepository.countByLectorIdAndEstadoIn(lector.getId(), estadosActivos);
        int limitePrestamos = lector.isMembresiaPremium() ? 5 : 3;

        if (prestamosActivosCount >= limitePrestamos) {
            throw new BusinessRuleException("El lector ha alcanzado el límite máximo permitido de préstamos activos (" + limitePrestamos + ").");
        }


        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);


        Prestamo prestamo = Prestamo.builder()
                .lector(lector)
                .libro(libro)
                .fechaPrestamo(LocalDate.now())
                .fechaDevolucionMaxima(LocalDate.now().plusDays(14))
                .estado(EstadoPrestamo.PRESTADO)
                .build();

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
        return mapToDTO(prestamoGuardado);
    }

    @Override
    @Transactional
    public PrestamoResponseDTO devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo con ID " + prestamoId + " no existe."));

        if (prestamo.getEstado() != EstadoPrestamo.PRESTADO && prestamo.getEstado() != EstadoPrestamo.VENCIDO) {
            throw new BusinessRuleException("El préstamo ya se encuentra en estado DEVUELTO.");
        }

        LocalDate fechaActual = LocalDate.now();
        if (fechaActual.isAfter(prestamo.getFechaDevolucionMaxima()) || prestamo.getEstado() == EstadoPrestamo.VENCIDO) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);


        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);
        return mapToDTO(prestamoActualizado);
    }

    @Override
    @Transactional
    public PrestamoResponseDTO renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo con ID " + prestamoId + " no existe."));


        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BusinessRuleException("No se puede renovar un préstamo que ya fue devuelto.");
        }

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO || LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            throw new BusinessRuleException("No se puede renovar un préstamo que se encuentra vencido.");
        }


        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);
        return mapToDTO(prestamoActualizado);
    }

    @Override
    @Transactional
    public void rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new ResourceNotFoundException("El lector con ID " + lectorId + " no existe."));

        lector.setActivo(true);
        lectorRepository.save(lector);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listarPrestamosActivosLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new ResourceNotFoundException("El lector con ID " + lectorId + " no existe.");
        }

        List<EstadoPrestamo> estadosActivos = Arrays.asList(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
        List<Prestamo> prestamos = prestamoRepository.findByLectorIdAndEstadoIn(lectorId, estadosActivos);

        return prestamos.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PrestamoResponseDTO mapToDTO(Prestamo prestamo) {
        return PrestamoResponseDTO.builder()
                .id(prestamo.getId())
                .lectorId(prestamo.getLector().getId())
                .nombreLector(prestamo.getLector().getNombre())
                .libroId(prestamo.getLibro().getId())
                .tituloLibro(prestamo.getLibro().getTitulo())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima())
                .estado(prestamo.getEstado())
                .build();
    }
}