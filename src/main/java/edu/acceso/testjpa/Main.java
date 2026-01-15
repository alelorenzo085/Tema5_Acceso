package edu.acceso.testjpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.acceso.testjpa.domain.Centro;
import edu.acceso.testjpa.domain.Estudiante;
import edu.acceso.testjpa.domain.Titularidad;

import ch.qos.logback.classic.Level;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
        private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        ch.qos.logback.classic.Logger hibernateLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.hibernate");
        hibernateLogger.setLevel(Level.WARN);

        String bd = "jdbc:sqlite:centro.db";
        
        Map<String, String> props = new HashMap<>();
        //props.put("jakarta.persistence.jdbc.url", bd);

        // Se crea un objeto para realizar transacciones
        int idx = JpaBackend.createEntityManagerFactory("InstitutoPersistente", props);

        // Transacción sin resultado para agregar un centro
        JpaBackend.transaction(idx, em -> {
            Centro centro = new Centro(11004866, "IES Castillo de Luna",Titularidad.PUBLICA);
            em.persist(centro);
        });

        // Transacción con resultado (se aplica a la bbdd existente)
        Centro centro = JpaBackend.transactionR(idx, em -> {
            Centro c = em.find(Centro.class, 11004866);
            c.setNombre("I.E.S. Castillo de Luna");
            return c;
        });

        // Transacción sin resultado para agregar un centro
        JpaBackend.transaction(idx, em -> {
            Estudiante[] estudiantes = new Estudiante[] {
                new Estudiante("Manolo", LocalDate.of(2000, 01, 01), centro),
                new Estudiante("Marisa", LocalDate.of(2004, 10, 12), centro)
            };

            // Por cada estudiante de la lista, se añade a la base de datos (acción persist)
            for(Estudiante e: estudiantes) em.persist(e);
        });

        // Comprobar estudiantes
        List<Estudiante> estudiantes = JpaBackend.transactionR(em -> {
            
            Centro c = em.find(Centro.class, 11004866);
            List<Estudiante> lista = c.getEstudiantes();
            //for(estudiante e: ee) {
            // )}
            return lista;
        });

        System.out.printf("-- Estudiantes de '%s' --\n ", centro.getNombre() + ":");
        estudiantes.forEach(System.out::println);

        JpaBackend.reset();
    }
}