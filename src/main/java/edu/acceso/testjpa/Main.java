package edu.acceso.testjpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
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
        
        try(EntityManagerFactory emf = Persistence.createEntityManagerFactory("InstitutoPersistente", props)) {

            // Agregamos un centro a la base de datos
            try(EntityManager em = emf.createEntityManager()) {
                EntityTransaction tr = em.getTransaction();
                try {
                    tr.begin();
                    Centro centro = new Centro(11004866, "IES Castillo de Luna",Titularidad.PUBLICA);
                    em.persist(centro);
                    tr.commit();
                } catch (Exception e) {
                    if (tr != null && tr.isActive()) tr.rollback();
                    logger.error("Imposible añadir un centro a la base de datos");
                }
            }

            // Recuperamos el centro y hacemos una modificación
            Centro centro = null;
            try(EntityManager em = emf.createEntityManager()) {
                Centro centroRecuperado = em.find(Centro.class, 11004866);
                System.out.println("Centro recuperado: " + centroRecuperado.getNombre() + ", " + centroRecuperado.getTitularidad());

                EntityTransaction tr = em.getTransaction();
                try {
                    tr.begin();
                    centroRecuperado.setNombre("I.E.S. Castillo de Luna");
                    tr.commit();
                } catch (Exception e) {
                    if (tr != null && tr.isActive()) tr.rollback();
                    logger.error("Imposible obtener el centro y su nombre");
                }
            }

            Estudiante[] estudiantes = new Estudiante[] {
                new Estudiante("Manolo", LocalDate.of(2000, 01, 01), centro),
                new Estudiante("Marisa", LocalDate.of(2004, 10, 12), centro)
            };

            try(EntityManager em = emf.createEntityManager()) {
                EntityTransaction tr = em.getTransaction();
                try {
                    tr.begin();
                    for (Estudiante e : estudiantes) {
                        em.persist(e);
                    }
                    tr.commit();
                } catch (Exception e) {
                    if (tr != null && tr.isActive()) tr.rollback();
                    logger.error("No se han podido guardar los estudiantes", e);
                }
            }

            try(EntityManager em = emf.createEntityManager()) {
                EntityTransaction tr = em.getTransaction();
                try {
                    tr.begin();
                    centro = em.find(Centro.class, 11004866);
                    System.out.printf("--- Estudiantes del centro %s ---\n", centro.getNombre());
                    for(Estudiante e: centro.getEstudiantes()) {
                        System.out.println(e);
                    }
                    tr.commit();
                } catch (Exception e) {
                    if (tr != null && tr.isActive()) tr.rollback();
                    e.printStackTrace();
                }
            }
        }
    }
}