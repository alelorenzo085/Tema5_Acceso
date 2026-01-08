package edu.acceso.testjpa;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.text.html.parser.Entity;

import org.hibernate.validator.internal.util.logging.LoggerFactory;

import edu.acceso.testjpa.domain.Centro;
import edu.acceso.testjpa.domain.Centro.Titularidad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        
        Map<String, String> properties = new HashMap<>();
        
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