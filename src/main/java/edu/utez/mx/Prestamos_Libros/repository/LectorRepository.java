package edu.utez.mx.Prestamos_Libros.repository;

import edu.utez.mx.Prestamos_Libros.model.Lector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {
}