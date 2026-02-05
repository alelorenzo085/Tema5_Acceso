package edu.acceso.testjpa.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private LocalDate nacimiento;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Centro centro;

    public Estudiante() {
        super();
    }

    public Estudiante initialize(String nombre, LocalDate nacimiento, Centro centro) {
        setNombre(nombre);
        setNacimiento(nacimiento);
        setCentro(centro);
        return this;
    }

    public Estudiante(String nombre, LocalDate nacimiento, Centro centro) {
        initialize(nombre, nacimiento, centro);
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNacimiento(LocalDate nacimiento) {
        this.nacimiento = nacimiento;
    }

    public long getEdad() {
        LocalDate hoy = LocalDate.now();
        return ChronoUnit.YEARS.between(nacimiento, hoy);
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    @Override
    public String toString() {
        return String.format("%s, %d años", nombre, getEdad());
    }
}