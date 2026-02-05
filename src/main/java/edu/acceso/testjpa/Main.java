package edu.acceso.testjpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.acceso.testjpa.domain.Centro;
import edu.acceso.testjpa.domain.Estudiante;
import edu.acceso.testjpa.domain.Estudiante_;
import edu.acceso.testjpa.domain.Titularidad;

import ch.qos.logback.classic.Level;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        ch.qos.logback.classic.Logger hibernateLogger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger("org.hibernate");
        hibernateLogger.setLevel(Level.WARN);

        String bd = "jdbc:sqlite:centro.db";

        Map<String, String> props = new HashMap<>();
        // props.put("jakarta.persistence.jdbc.url", bd);

        // Se crea un objeto para realizar transacciones
        int idx = JpaBackend.createEntityManagerFactory("InstitutoPersistente", props);

        // Transacción sin resultado para agregar un centro Castillo de Luna
        JpaBackend.transaction(idx, em -> {
            Centro centro = new Centro(11004866, "IES Castillo de Luna", Titularidad.PUBLICA);
            em.persist(centro);
        });

        // Transacción sin resultado para agregar un centro Lara
        JpaBackend.transaction(idx, em -> {
            Centro centro = new Centro(11700603, "IES Lara del Rey", Titularidad.PUBLICA);
            em.persist(centro);
        });

        // Transacción con resultado (se aplica a la bbdd existente)
        Centro castillo = JpaBackend.transactionR(idx, em -> {
            // Busca el centro en la base datos
            Centro c = em.find(Centro.class, 11004866);
            c.setNombre("I.E.S. Castillo de Luna");
            return c;
        });

        Centro lara = JpaBackend.transactionR(em -> {
            return em.find(Centro.class, 11700603);
        });

        // Transacción sin resultado para agregar una lista de estudiantes
        JpaBackend.transaction(idx, em -> {
            Estudiante[] estudiantes = new Estudiante[] {
                    new Estudiante("Manolo", LocalDate.of(2000, 01, 01), castillo),
                    new Estudiante("Marisa", LocalDate.of(2004, 10, 12), castillo),
                    new Estudiante("Alberto", LocalDate.of(2003, 5, 22), castillo),
                    new Estudiante("Lucía", LocalDate.of(2002, 7, 2), lara),
                    new Estudiante("Paco", LocalDate.of(2004, 7, 21), null)
            };

            // Por cada estudiante de la lista, se añade a la base de datos (acción persist)
            for (Estudiante e : estudiantes)
                em.persist(e);
        });

        // Comprobar estudiantes
        List<Estudiante> estudiantes = JpaBackend.transactionR(em -> {
            TypedQuery<Estudiante> query = em.createQuery("SELECT e FROM Estudiante e", Estudiante.class);
            return query.getResultList();
        });

        System.out.printf("-- Estudiantes de '%s' --\n ", castillo.getNombre() + ":");
        estudiantes.forEach(System.out::println);

        // Creamos una transacción nueva para obtener únicamente los nombres de los
        // estudiantes
        JpaBackend.transaction(em -> {
            // Se establece una consulta a la tabla de Estudiante donde obtenemos solo su
            // nombre
            TypedQuery<String> query = em.createQuery("SELECT e.nombre FROM Estudiante e", String.class);

            // Y se establece una lista con los nombres obtenidos
            List<String> nombres = query.getResultList();
            System.out.println("-- Listado de nombres de alumno --");
            nombres.forEach(System.out::println);
        });

        // Transacción con consulta JOIN para extraer todos los estudiantes con centro
        estudiantes = JpaBackend.transactionR(em -> {
            TypedQuery<Estudiante> ee = em.createQuery(
                    "SELECT e FROM Estudiante e JOIN e.centro c", Estudiante.class);
            return ee.getResultList();
        });

        System.out.println("--- Lista de estudiantes con centro ---");
        estudiantes.forEach(System.out::println);

        // Transacción con consulta JOIN para extraer todos los estudiantes
        estudiantes = JpaBackend.transactionR(em -> {
            TypedQuery<Estudiante> ee = em.createQuery(
                    "SELECT e FROM Estudiante e LEFT JOIN e.centro c", Estudiante.class);
            return ee.getResultList();
        });

        System.out.println("--- Lista de todos los estudiantes ---");
        for (Estudiante e : estudiantes) {
            Centro centro = e.getCentro();
            System.out.printf("%s: %s.\n", e, centro != null ? centro.getNombre() : "***");
        }

        // TRANSACCIÓN USANDO CRITERIA API
        JpaBackend.transaction(em -> {

            // LISTA DE ESTUDIANTES COMPLETA
            System.out.println("\n\n--- Lista de estudiantes con Criteria ---");
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Estudiante> criteria = cb.createQuery(Estudiante.class);
            Root<Estudiante> root = criteria.from(Estudiante.class);
            criteria.select(root);

            TypedQuery<Estudiante> tq = em.createQuery(criteria);
            tq.getResultList().forEach(System.out::println);

            // LISTA DE NOMBRES DE LOS ESTUDIANTES
            System.out.println("\n-- Lista de nombres de estudiantes con Criteria ---");
            CriteriaQuery<String> criteriaN = cb.createQuery(String.class);
            root = criteriaN.from(Estudiante.class);
            criteriaN.select(root.get(Estudiante_.nombre));

            TypedQuery<String> tqN = em.createQuery(criteriaN);
            tqN.getResultList().forEach(System.out::println);

            // LISTA DE NOMBRES E IDs DE LOS ESTUDIANTES
            System.out.println("\n-- Lista de nombres e IDs de estudiantes con Criteria ---");
            CriteriaQuery<Tuple> criteriaT = cb.createTupleQuery();
            root = criteriaT.from(Estudiante.class);
            criteriaT.select(cb.tuple(
                    root.get(Estudiante_.nombre).alias("nombre"),
                    root.get(Estudiante_.id).alias("id")));

            TypedQuery<Tuple> tqT = em.createQuery(criteriaT);
            tqT.getResultList().forEach(t -> {
                String nombre = t.get(Estudiante_.NOMBRE, String.class);
                Long id = t.get(Estudiante_.ID, Long.class);
                System.out.printf("%d: %s.\n", id, nombre);
            });

            // LISTA DE ESTUDIANTES NO MATRICULADOS USANDO CONDICIONES WHERE
            CriteriaQuery<Estudiante> criteriaNM = cb.createQuery(Estudiante.class);
            Root<Estudiante> rootNM = criteriaNM.from(Estudiante.class);
            criteriaNM.select(rootNM);
            // CONDICIÓN
            criteriaNM.where(cb.not(cb.isNull(rootNM.get(Estudiante_.CENTRO))));

            System.out.println("\n-- Lista de estudiantes sin matrícula ---");
            TypedQuery<Estudiante> te = em.createQuery(criteriaNM);
            te.getResultList().forEach(System.out::println);
        });

        JpaBackend.transaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Estudiante> query = cb.createQuery(Estudiante.class);
            Root<Estudiante> root = query.from(Estudiante.class);
            query.select(root);
            query.orderBy(cb.desc(root.get(Estudiante_.nacimiento)));

            System.out.println("\n -- Lista de estudiantes ordenados por edad --");
            TypedQuery<Estudiante> tq = em.createQuery(query);
            tq.getResultList().forEach(System.out::println);
        });

        JpaBackend.transaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
            Root<Estudiante> root = query.from(Estudiante.class);
            query.select(cb.tuple(
                root.get(Estudiante_.CENTRO).alias("centro"),
                cb.count(root).alias("cantidad")
            ));

            query.groupBy(root.get(Estudiante_.CENTRO));
            query.orderBy(cb.asc(cb.count(root)));

            System.out.println("\n-- Lista de centros --");
            TypedQuery<Tuple> tq = em.createQuery(query);
            tq.getResultList().forEach(t -> {
                Centro centro = t.get("centro", Centro.class);
                Long cantidad = t.get("cantidad", Long.class);
                System.out.printf("%s: %d alumnos.\n", centro, cantidad);
            });
        });
    }
}