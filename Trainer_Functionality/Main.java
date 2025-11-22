package com.team5;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Initialize entity manager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GymPU");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        //scheduleView(em);              // Schedule View test
        
        //memberLookup(em, scanner);     // Member Lookup test

        //setAvailability(em, scanner);  // Set Availability test

        // Close entity manager
        scanner.close();
        em.close();
        emf.close();
    }


    // Schedule View Function
    public static void scheduleView(EntityManager em) {

        System.out.print("\nSearching for any sessions...\n");

        // Get all existing sessions
        List<PT_Session> sessionsList = em
                .createQuery("SELECT session FROM PT_Session session", PT_Session.class)
                .getResultList();

        // Let user know if there are no existing sessions
        if (sessionsList.isEmpty()) {
            System.out.print("\nThere are no sessions found.\n");
        }

        else {

            System.out.print("\n\n  Sessions Schedule:\n\n");

            // Output each existing session
            for (PT_Session session : sessionsList) {
                System.out.println(
                    "Session ID:" + session.getID() +
                    " | Member ID: " + session.getMemberID() +
                    " | Trainer ID: " + session.getTrainerID() +
                    " | Room ID: " + session.getRoomID() +
                    " | Start Time: " + session.getStartTime() +
                    " | End Time: " + session.getEndTime() +
                    " | Status: " + session.getStatus()
                );
            }
        }

        System.out.print("\nSearching for any classes...\n");

        // Get all existing classes
        List<Group_Class> classesList = em
                .createQuery("SELECT class FROM Group_Class class", Group_Class.class)
                .getResultList();

        // Let user know if there are no existing sessions
        if (classesList.isEmpty()) {
            System.out.print("\nThere are no classes found.\n");
        }

        else {

            System.out.print("\n\n  Classes Schedule:\n\n");

            // Output each existing classes
            for (Group_Class group_class : classesList) {
                System.out.println(
                    "Class ID:" + group_class.getID() +
                    " | Trainer ID: " + group_class.getTrainerID() + "\n"
                );
            }
        }
    }


    // Member Lookup Function
    public static void memberLookup(EntityManager em, Scanner scanner) {
        
        // Receive a name of the searched member
        System.out.print("Enter member name to search (format 'Firstname Lastname'): ");
        String searchName = scanner.nextLine();

        System.out.print("\nSearching for " + searchName + "...\n");

        // Get all members by the entered name 
        List<Member> members = em.createQuery(
            "SELECT member FROM Member member WHERE LOWER(member.name) LIKE LOWER(:name)",
            Member.class
        )
        .setParameter("name", "%" + searchName + "%")
        .getResultList();

        // Let user know if there are no members having entered name
        if (members.isEmpty()) {
            System.out.println("\nMember " + searchName + " not found\n");
            return;
        }

        for (Member m : members) {

            System.out.print("\nSearching for any fitness goals...\n");

            // Get the first non-complete goal for the searched member 
            List<Fitness_Goal> goal = em.createQuery(
                "SELECT goal FROM Fitness_Goal goal WHERE goal.member_id = :mem",
                Fitness_Goal.class
            )
            .setParameter("mem", m)
            .setMaxResults(1)
            .getResultList();

            System.out.print("\nSearching for any health metrics...\n");

            // Get the last generated health metric for the searched member 
            List<Health_Metric> metric = em.createQuery(
                "SELECT metric FROM Health_Metric metric WHERE metric.member_id = :mem ORDER BY metric.metric_id DESC",
                Health_Metric.class
            )
            .setParameter("mem", m)
            .setMaxResults(1)
            .getResultList();

            // Output searching results
            System.out.println("\nMember: " + m.getName() + " | ID: " + m.getID());

            if (goal == null) {
                System.out.println("Current Goal ID: Not found");
            } else {
                System.out.println("Current Goal ID: " + goal.get(0).getID());
            }

            if (metric == null) {
                System.out.println("Last Health Metric ID: Not found");
            } else {
                System.out.println("Last Health Metric ID: " + metric.get(0).getID());
            }

            System.out.println();
        }
    }


    // Set Availability Function
    public static void setAvailability(EntityManager em, Scanner scanner) {
        
        // Receive the trainer ID
        System.out.print("Enter trainer ID: ");
        int trainerID = scanner.nextInt();
        scanner.nextLine();

        // Let user know if the chosen trainer does not exist
        System.out.println("\nSearching for the specified trainer...");
        Trainer trainer = em.find(Trainer.class, trainerID);
        if (trainer == null) {
            System.out.println("Trainer not found");
            return;
        }

        // Get all trainer's available windows
        System.out.println("\nSearching for trainer's available time...");
        List<Trainer_Availability> availabilityList = em
                .createQuery("SELECT availability FROM Trainer_Availability availability WHERE availability.trainer_id = :t",
                        Trainer_Availability.class)
                .setParameter("t", trainer)
                .getResultList();

        // Get all trainer's PT sessions
        System.out.println("\nSearching for trainer's PT sessions...");
        List<PT_Session> sessionList = em.createQuery(
                        "SELECT session FROM PT_Session session WHERE session.trainer_id = :t",
                        PT_Session.class)
                .setParameter("t", trainer)
                .getResultList();

        // Get all trainer's group classes
        System.out.println("\nSearching for trainer's group classes...");
        List<Group_Class> classList = em.createQuery(
                        "SELECT class FROM Group_Class class WHERE class.trainer_id = :t",
                        Group_Class.class)
                .setParameter("t", trainer)
                .getResultList();

        // Display all trainer's available windows
        System.out.println("\nCurrent trainer's availability schedule:");
        for (Trainer_Availability a : availabilityList) {
            System.out.println("Availability ID " + a.getID() +
                    " | " + a.getStartTime() + " - " + a.getEndTime() +
                    " | Pattern: " + a.getPattern());
        }

        // Display all trainer's PT sessions
        System.out.println("\nCurrent trainer's PT sessions:");
        for (PT_Session s : sessionList) {
            System.out.println("Session ID: " + s.getID() +
                    " | " + s.getStartTime() + " - " + s.getEndTime());
        }

        // Display all trainer's group classes
        System.out.println("\nCurrent trainer's group classes:");
        for (Group_Class c : classList) {
            System.out.println("Class ID: " + c.getID() +
                    " | " + c.getStartTime() + " - " + c.getEndTime());
        }

        // Get user's wished action choise
        System.out.print("\nChoose an action:\n1 - Add availability time\n2 - Remove availability time\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        // Add availability
        if (choice == 1) {

            // Get start and end dates
            System.out.print("Enter start datetime (YYYY-MM-DD HH:MM:SS): ");
            String startStr = scanner.nextLine();

            System.out.print("Enter end datetime (YYYY-MM-DD HH:MM:SS): ");
            String endStr = scanner.nextLine();

            Timestamp start = Timestamp.valueOf(startStr);
            Timestamp end = Timestamp.valueOf(endStr);

            // Check entered dates logic
            if (end.before(start)) {
                System.out.println("End time cannot be before start time.");
                return;
            }

            // Check for dates operlaps with the available time schedule
            for (Trainer_Availability a : availabilityList) {
                if (!(end.before(a.getStartTime()) || start.after(a.getEndTime()))) {
                    System.out.println("Conflict with availability (ID: " + a.getID() + ")");
                    return;
                }
            }

            // Check for dates operlaps with the PT sessions schedule
            for (PT_Session s : sessionList) {
                if (!(end.before(s.getStartTime()) || start.after(s.getEndTime()))) {
                    System.out.println("Conflict with PT session (ID: " + s.getID() + ")");
                    return;
                }
            }

            // Check for dates operlaps with the group classes schedule
            for (Group_Class c : classList) {
                if (!(end.before(c.getStartTime()) || start.after(c.getEndTime()))) {
                    System.out.println("Conflict with group class (ID: " + c.getID() + ")");
                    return;
                }
            }

            // Insert new availability time to the database table
            System.out.println("\nAdding new available time...");
            em.getTransaction().begin();
            Trainer_Availability newAvailability = new Trainer_Availability(trainer, start, end, "added");
            em.persist(newAvailability);
            em.getTransaction().commit();

            System.out.println("\nAvailability time added\n");
        }

        // Remove availability
        else if (choice == 2) {
            
            // Get availability ID
            System.out.print("Enter availability ID to remove: ");
            int availabilityID = scanner.nextInt();
            scanner.nextLine();

            // Attempt to find the chosen availability
            Trainer_Availability availability = em.find(Trainer_Availability.class, availabilityID);
            if (availability == null || availability.getTrainerID() != trainerID) {
                System.out.println("\nAvailability not found or does not belong to this trainer\n");
                return;
            }

            // Remove availability
            System.out.println("\nRemoving the available time...");
            em.getTransaction().begin();
            em.remove(availability);
            em.getTransaction().commit();

            System.out.println("\nAvailability time removed\n");
        }

        // Let user know non-existing option was chosen
        else {
            System.out.println("\nInvalid choice.\n");
        }
    }
}
