package Main.java;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


public class TrainerService {

    private final EntityManager em;

    public TrainerService(EntityManager em) {
        this.em = em;
    }

    // 1. Set trainer availability with overlap prevention
    public boolean setAvailability(int trainerId,
                                   Timestamp startTime,
                                   Timestamp endTime,
                                   String recurrencePattern) {

        if (endTime.before(startTime) || endTime.equals(startTime)) {
            System.out.println("End time must be after start time.");
            return false;
        }

        Trainer trainer = em.find(Trainer.class, trainerId);
        if (trainer == null) {
            System.out.println("Trainer not found.");
            return false;
        }

        // Check for overlapping availability slots
        Long overlapping = em.createQuery(
                        "SELECT COUNT(a) FROM Trainer_Availability a " +
                                "WHERE a.trainer = :trainer " +
                                "AND a.startTime < :endTime " +
                                "AND a.endTime > :startTime",
                        Long.class)
                .setParameter("trainer", trainer)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .getSingleResult();

        if (overlapping > 0) {
            System.out.println("Availability overlaps with an existing slot.");
            return false;
        }

        em.getTransaction().begin();
        try {
            Trainer_Availability availability =
                    new Trainer_Availability(trainer, startTime, endTime, recurrencePattern);
            em.persist(availability);
            em.getTransaction().commit();
            System.out.println("Availability added for trainer " + trainer.getName());
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    // 2. View trainer schedule (PT sessions + group classes)
    public void viewSchedule(int trainerId,
                             LocalDateTime from,
                             LocalDateTime to) {

        Trainer trainer = em.find(Trainer.class, trainerId);
        if (trainer == null) {
            System.out.println("Trainer not found.");
            return;
        }

        System.out.println("\n=== Schedule for Trainer: " + trainer.getName() + " ===");

        // PT sessions (Timestamp)
        TypedQuery<PT_Session> ptQuery = em.createQuery(
                "SELECT s FROM PT_Session s " +
                        "WHERE s.trainer = :trainer " +
                        "AND s.startTime >= :fromTs " +
                        "AND s.startTime <= :toTs " +
                        "ORDER BY s.startTime",
                PT_Session.class
        );
        ptQuery.setParameter("trainer", trainer);
        ptQuery.setParameter("fromTs", Timestamp.valueOf(from));
        ptQuery.setParameter("toTs", Timestamp.valueOf(to));

        List<PT_Session> sessions = ptQuery.getResultList();

        System.out.println("\nPersonal Training Sessions:");
        if (sessions.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (PT_Session s : sessions) {
                System.out.println("  Session " + s.getSessionId() +
                        " with member " + s.getMember().getName() +
                        " at " + s.getStartTime() + " in room " +
                        (s.getRoom() != null ? s.getRoom().getName() : "N/A") +
                        " [" + s.getStatus() + "]");
            }
        }

        // Group classes (LocalDateTime)
        TypedQuery<GROUP_CLASS> classQuery = em.createQuery(
                "SELECT gc FROM GROUP_CLASS gc " +
                        "WHERE gc.trainer = :trainer " +
                        "AND gc.startTime >= :from " +
                        "AND gc.startTime <= :to " +
                        "ORDER BY gc.startTime",
                GROUP_CLASS.class
        );
        classQuery.setParameter("trainer", trainer);
        classQuery.setParameter("from", from);
        classQuery.setParameter("to", to);

        List<GROUP_CLASS> classes = classQuery.getResultList();

        System.out.println("\nGroup Classes:");
        if (classes.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (GROUP_CLASS gc : classes) {
                System.out.println("  Class " + gc.getClassId() + " - " + gc.getTitle() +
                        " at " + gc.getStartTime() +
                        " in room " + gc.getRoom().getName() +
                        " [Status: " + gc.getStatus() + "]");
            }
        }

        System.out.println("=================================\n");
    }

    // 3. Member lookup (read-only: current goal + last metric)
    public void lookupMemberByName(String nameFragment) {
        String pattern = "%" + nameFragment.toLowerCase() + "%";

        List<MEMBER> members = em.createQuery(
                        "SELECT m FROM MEMBER m " +
                                "WHERE LOWER(m.name) LIKE :pattern",
                        MEMBER.class)
                .setParameter("pattern", pattern)
                .getResultList();

        if (members.isEmpty()) {
            System.out.println("No members found matching: " + nameFragment);
            return;
        }

        System.out.println("\n=== Member Lookup (Trainer View) ===");
        for (MEMBER m : members) {
            System.out.println("\nMember: " + m.getName() + " (ID: " + m.getMemberId() + ")");

            // Active goals
            List<FITNESS_GOAL> goals = em.createQuery(
                            "SELECT g FROM FITNESS_GOAL g " +
                                    "WHERE g.member.memberId = :mid " +
                                    "AND g.status = 'Active'",
                            FITNESS_GOAL.class)
                    .setParameter("mid", m.getMemberId())
                    .getResultList();

            if (goals.isEmpty()) {
                System.out.println("  Active goals: (none)");
            } else {
                System.out.println("  Active goals:");
                for (FITNESS_GOAL g : goals) {
                    System.out.println("    - " + g.getGoalType() +
                            " (Target: " + g.getTargetValue() + ")");
                }
            }

            // Last health metric
            List<HEALTH_METRIC> metrics = em.createQuery(
                            "SELECT h FROM HEALTH_METRIC h " +
                                    "WHERE h.member.memberId = :mid " +
                                    "ORDER BY h.recordedAt DESC",
                            HEALTH_METRIC.class)
                    .setParameter("mid", m.getMemberId())
                    .setMaxResults(1)
                    .getResultList();

            if (metrics.isEmpty()) {
                System.out.println("  Last metric: (none)");
            } else {
                HEALTH_METRIC last = metrics.get(0);
                System.out.println("  Last metric: " + last.getMetricType() +
                        " = " + last.getValue() +
                        " at " + last.getRecordedAt());
            }
        }
        System.out.println("====================================\n");
    }
}

