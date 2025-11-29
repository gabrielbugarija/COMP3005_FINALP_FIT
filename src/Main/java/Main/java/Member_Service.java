package Main.java;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

import java.sql.Timestamp;
import Main.java.Trainer;
import Main.java.ROOM;
import Main.java.PT_Session;
import Main.java.Trainer_Availability;
import Main.java.RoomBookingService;
import Main.java.GROUP_CLASS;
import Main.java.CLASS_ENROLLMENT;

public class Member_Service {

    private EntityManager em;

    public Member_Service (EntityManager em) {
        this.em = em;
    }

    // User Registration
    public void registerMember(String name, LocalDate dob, String gender,
                               String email, String phone, String address) {
        em.getTransaction().begin();
        MEMBER member = new MEMBER(name, dob, gender, email, phone, address, LocalDateTime.now());
        em.persist(member);
        em.getTransaction().commit();
        System.out.println(" Member registered! ID: " + member.getMemberId());
    }

    // Update Profile
    public void updateProfile(int memberId, String newPhone, String newAddress) {
        em.getTransaction().begin();
        MEMBER member = em.find(MEMBER.class, memberId);
        if (member != null) {
            member.setPhone(newPhone);
            member.setAddress(newAddress);
            System.out.println(" Profile updated!");
        } else {
            System.out.println("Member not found");
        }
        em.getTransaction().commit();
    }

    // Log Health Metric
    public void logHealthMetric(int memberId, String metricType, float value) {
        em.getTransaction().begin();
        MEMBER member = em.find(MEMBER.class, memberId);
        if (member != null) {
            HEALTH_METRIC metric = new HEALTH_METRIC(member, metricType, value, LocalDateTime.now());
            em.persist(metric);
            System.out.println(" Health metric logged: " + metricType + " = " + value);
        }
        em.getTransaction().commit();
    }

    // Add Fitness Goal
    public void addFitnessGoal(int memberId, String goalType, double targetValue) {
        em.getTransaction().begin();
        MEMBER member = em.find(MEMBER.class, memberId);
        if (member != null) {
            FITNESS_GOAL goal = new FITNESS_GOAL(
                    member, goalType, BigDecimal.valueOf(targetValue),
                    LocalDate.now(), LocalDate.now().plusMonths(3), "Active"
            );
            em.persist(goal);
            System.out.println(" Goal added: " + goalType);
        }
        em.getTransaction().commit();
    }

    //View Dashboard
    public void viewDashboard(int memberId) {
        MEMBER member = em.find(MEMBER.class, memberId);
        if (member == null) {
            System.out.println("Member not found");
            return;
        }

        System.out.println("\n========== DASHBOARD ==========");
        System.out.println("Name: " + member.getName());
        System.out.println("Email: " + member.getEmail());

        // Recent metrics
        List<HEALTH_METRIC> metrics = em.createQuery(
                        "SELECT h FROM HEALTH_METRIC h WHERE h.member.memberId = :id ORDER BY h.recordedAt DESC",
                        HEALTH_METRIC.class)
                .setParameter("id", memberId)
                .setMaxResults(3)
                .getResultList();

        System.out.println("\nRecent Metrics:");
        for (HEALTH_METRIC m : metrics) {
            System.out.println("  " + m.getMetricType() + ": " + m.getValue());
        }

        // Active goals
        List<FITNESS_GOAL> goals = em.createQuery(
                        "SELECT g FROM FITNESS_GOAL g WHERE g.member.memberId = :id AND g.status = 'Active'",
                        FITNESS_GOAL.class)
                .setParameter("id", memberId)
                .getResultList();

        System.out.println("\nActive Goals:");
        for (FITNESS_GOAL g : goals) {
            System.out.println("  " + g.getGoalType() + ": Target " + g.getTargetValue());
        }
        System.out.println("===============================\n");
    }

    // Register for Group Class
    public void registerForClass(int memberId, int classId) {
        em.getTransaction().begin();

        MEMBER member = em.find(MEMBER.class, memberId);
        GROUP_CLASS groupClass = em.find(GROUP_CLASS.class, classId);

        if (member == null || groupClass == null) {
            System.out.println(" Member or Class not found");
            em.getTransaction().rollback();
            return;
        }

        // Check capacity
        long enrolled = em.createQuery(
                        "SELECT COUNT(e) FROM CLASS_ENROLLMENT e WHERE e.groupClass.classId = :classId",
                        Long.class)
                .setParameter("classId", classId)
                .getSingleResult();

        if (enrolled >= groupClass.getCapacity()) {
            System.out.println(" Class is full!");
        } else {
            CLASS_ENROLLMENT enrollment = new CLASS_ENROLLMENT(groupClass, member);
            em.persist(enrollment);
            System.out.println(" Registered for class: " + groupClass.getTitle());
        }

        em.getTransaction().commit();
    }

    public boolean schedulePTSession(int memberId,
                                     int trainerId,
                                     int roomId,
                                     LocalDateTime start,
                                     LocalDateTime end) {

        if (end.isBefore(start) || end.equals(start)) {
            System.out.println("End time must be after start time.");
            return false;
        }

        MEMBER member = em.find(MEMBER.class, memberId);
        Trainer trainer = em.find(Trainer.class, trainerId);
        ROOM room = em.find(ROOM.class, roomId);

        if (member == null || trainer == null || room == null) {
            System.out.println("Member, trainer, or room not found.");
            return false;
        }

        Timestamp startTs = Timestamp.valueOf(start);
        Timestamp endTs = Timestamp.valueOf(end);

        em.getTransaction().begin();
        try {
            //Check trainer availability window
            Long availableSlots = em.createQuery(
                            "SELECT COUNT(a) FROM Trainer_Availability a " +
                                    "WHERE a.trainer = :trainer " +
                                    "AND a.startTime <= :startTs " +
                                    "AND a.endTime >= :endTs",
                            Long.class)
                    .setParameter("trainer", trainer)
                    .setParameter("startTs", startTs)
                    .setParameter("endTs", endTs)
                    .getSingleResult();

            if (availableSlots == 0) {
                System.out.println("Trainer is not available in that time window.");
                em.getTransaction().rollback();
                return false;
            }

            // Member overlapping PT sessions
            Long overlappingMemberSessions = em.createQuery(
                            "SELECT COUNT(s) FROM PT_Session s " +
                                    "WHERE s.member = :member " +
                                    "AND s.startTime < :endTs " +
                                    "AND s.endTime > :startTs",
                            Long.class)
                    .setParameter("member", member)
                    .setParameter("startTs", startTs)
                    .setParameter("endTs", endTs)
                    .getSingleResult();

            if (overlappingMemberSessions > 0) {
                System.out.println("Member already has a PT session at that time.");
                em.getTransaction().rollback();
                return false;
            }

            //  Trainer overlapping PT sessions
            Long overlappingTrainerSessions = em.createQuery(
                            "SELECT COUNT(s) FROM PT_Session s " +
                                    "WHERE s.trainer = :trainer " +
                                    "AND s.startTime < :endTs " +
                                    "AND s.endTime > :startTs",
                            Long.class)
                    .setParameter("trainer", trainer)
                    .setParameter("startTs", startTs)
                    .setParameter("endTs", endTs)
                    .getSingleResult();

            //Trainer overlapping group classes
            Long overlappingTrainerClasses = em.createQuery(
                            "SELECT COUNT(gc) FROM GROUP_CLASS gc " +
                                    "WHERE gc.trainer = :trainer " +
                                    "AND gc.startTime < :end " +
                                    "AND gc.endTime > :start",
                            Long.class)
                    .setParameter("trainer", trainer)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getSingleResult();

            if (overlappingTrainerSessions + overlappingTrainerClasses > 0) {
                System.out.println("Trainer is already booked at that time.");
                em.getTransaction().rollback();
                return false;
            }

            // Room availability (re-use existing RoomBookingService)
            RoomBookingService bookingService = new RoomBookingService(em);
            if (!bookingService.isRoomAvailable(room, start, end)) {
                System.out.println("Room is not available at that time.");
                em.getTransaction().rollback();
                return false;
            }

            // All checks passed => create PT session
            PT_Session session = new PT_Session(
                    member,
                    trainer,
                    room,
                    startTs,
                    endTs,
                    "Scheduled"
            );
            em.persist(session);

            em.getTransaction().commit();
            System.out.println("PT session scheduled with ID: " + session.getSessionId());
            return true;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}

