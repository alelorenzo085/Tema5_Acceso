package edu.acceso.testjpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

import edu.acceso.testjpa.domain.Centro;
import edu.acceso.testjpa.domain.Centro.Titularidad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
        private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        ch.qos.logback.classic.Logger hibernateLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.hibernate");
        hibernateLogger.setLevel(ch.qos.logback.classic.Level.WARN);
        
        String bd = "jdbc:sqlite:centro.db";
        
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", bd);
        
        try(EntityManagerFactory emf = Persistence.createEntityManagerFactory("InstitutoPersistente")) {

            try(EntityManager em = emf.createEntityManager()) {
                EntityTransaction tr = em.getTransaction();

                try {
                    tr.begin();
                    Centro centro = new Centro(11004866, "IES Castillo de Luna",Titularidad.PUBLICA);
                    em.persist(centro);
                    tr.commit();
                } catch (Exception e) {
                    if (tr != null && tr.isActive()) tr.rollback();
                    e.printStackTrace();
                }
            }

            // Recuperamos el centro y hacemos una modificación
            try(EntityManager em = emf.createEntityManager()) {
                Centro centroRecuperado = em.find(Centro.class, 11004866);
                System.out.println("Centro recuperado: " + centroRecuperado.getNombre() + ", " + centroRecuperado.getTitularidad());

                EntityTransaction tr = em.getTransaction();
                try {
                    tr.begin();
                    centroRecuperado.setNombre("IES Nuevo Nombre");
                    tr.commit();
                } catch (Exception e) {
                    if (tr != null && tr.isActive()) tr.rollback();
                    e.printStackTrace();
                }
            }
        }
    }
}