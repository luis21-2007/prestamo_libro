package edu.utez.mx.Prestamos_Libros.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lectores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private boolean activo;

    @Column(nullable = false)
    private boolean membresiaPremium;
}