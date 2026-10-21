package edu.utez.mx.Prestamos_Libros.repository;

import edu.utez.mx.Prestamos_Libros.model.EstadoPrestamo;
import edu.utez.mx.Prestamos_Libros.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    long countByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);
    List<Prestamo> findByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);
}