package Main.java;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Health & Fitness Club Management System...");

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            // Create EntityManagerFactory (this will create tables)
            System.out.println(" Creating database connection...");
            emf = Persistence.createEntityManagerFactory("HealthFitnessClubPU");
            em = emf.createEntityManager();

            System.out.println(" Database connected successfully!");
            System.out.println("Tables created in PostgreSQL!");

            // Start transaction
            em.getTransaction().begin();

            // Create a test member
            System.out.println("\n Creating test member...");
            MEMBER member = new MEMBER(
                    "John Doe",
                    LocalDate.of(1995, 5, 15),
                    "Male",
                    "john.doe@email.com",
                    "613-555-0123",
                    "123 Main St, Ottawa",
                    LocalDateTime.now()
            );

            em.persist(member);
            em.getTransaction().commit();

            System.out.println("✅ Member created with ID: " + member.getMemberId());

            // Query it back
            MEMBER foundMember = em.find(MEMBER.class, member.getMemberId());
            System.out.println("✅ Found member: " + foundMember.getName() + " (" + foundMember.getEmail() + ")");

            System.out.println("\nSystem is working perfectly!");

        } catch (Exception e) {
            System.err.println(" Error occurred:");
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
            System.out.println("\nShutting down...");
        }
    }
}