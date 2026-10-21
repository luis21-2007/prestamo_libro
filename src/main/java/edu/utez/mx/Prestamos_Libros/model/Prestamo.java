package edu.utez.mx.Prestamos_Libros.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lector_id", nullable = false)
    private Lector lector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    @Column(nullable = false)
    private LocalDate fechaDevolucionMaxima;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPrestamo estado;
}