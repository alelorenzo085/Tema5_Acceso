package edu.acceso.testjpa.domain;

import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Centro") // Sólo útil si la table se llama de modo diferente.

public class Centro {

    private static class TitularidadConverter implements AttributeConverter<Titularidad, String> {

        @Override
        public String convertToDatabaseColumn(Titularidad titularidad) {
            return titularidad.toString();
        }

        @Override
        public Titularidad convertToEntityAttribute(String column) {
            return Titularidad.fromString(column);
        }
    }

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)  // GENERATED ALWAYS BY IDENTITY
    private Long id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Convert(converter = TitularidadConverter.class)
    private Titularidad titularidad;

    @OneToMany(mappedBy = "centro")
    private List<Estudiante> estudiantes;

    public Centro() {
        super();
    }

    public Centro cargarDatos(long id, String nombre, Titularidad titularidad) {
        setId(id);
        setNombre(nombre);
        setTitularidad(titularidad);
        return this;
    }

    public Centro(long id, String nombre, Titularidad titularidad) {
        cargarDatos(id, nombre, titularidad);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Titularidad getTitularidad() {
        return titularidad;
    }

    public void setTitularidad(Titularidad titularidad) {
        this.titularidad = titularidad;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", getNombre(), getId());
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }
}