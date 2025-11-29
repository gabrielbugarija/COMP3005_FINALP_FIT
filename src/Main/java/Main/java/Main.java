

package Main.java;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;


import java.sql.Timestamp;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        System.out.println("Starting Health & Fitness Club Management System...");

        EntityManagerFactory emf = null;
        EntityManager em = null;
        Scanner scanner = new Scanner(System.in);

        try {
            //Setup ORM / DB
            emf = Persistence.createEntityManagerFactory("HealthFitnessClubPU");
            em = emf.createEntityManager();

            Member_Service memberService = new Member_Service(em);
            RoomBookingService roomBookingService = new RoomBookingService(em);
            ClassManagmentService classService = new ClassManagmentService(em, roomBookingService);
            TrainerService trainerService = new TrainerService(em);

            System.out.println("Database connected successfully!");
            System.out.println("Tables created in PostgreSQL!");

            //demo data
            em.getTransaction().begin();

            MEMBER member = new MEMBER(
                    "John Doe",
                    LocalDate.of(1995, 5, 15),
                    "Male",
                    "john.doe@email.com",
                    "613-555-0123",
                    "123 Main St, Ottawa",
                    LocalDateTime.now()
            );

            Trainer trainer = new Trainer(
                    "Jane Trainer",
                    "Strength & Conditioning",
                    "jane.trainer@email.com",
                    "613-555-7777",
                    "Active"
            );

            ROOM room = new ROOM(
                    "Studio A",
                    "First Floor",
                    20
            );

            em.persist(member);
            em.persist(trainer);
            em.persist(room);
            em.getTransaction().commit();

            int memberId = member.getMemberId();
            int trainerId = trainer.getID();
            int roomId = room.getRoomId();

            // Create one class in the future for demo
            LocalDateTime classStart = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime classEnd   = classStart.plusHours(1);

            GROUP_CLASS yogaClass = classService.createClass(
                    "Evening Yoga",
                    "Relaxing full-body stretch and breathing",
                    classStart,
                    classEnd,
                    10,
                    "Scheduled",
                    trainer,
                    room
            );

            System.out.println("\n=== Demo Data Seeded ===");
            System.out.println(" Member:   " + member.getName() + " (ID: " + memberId + ")");
            System.out.println(" Trainer:  " + trainer.getName() + " (ID: " + trainerId + ")");
            System.out.println(" Room:     " + room.getName() + " (ID: " + roomId + ")");
            if (yogaClass != null) {
                System.out.println(" Class:    " + yogaClass.getTitle() + " (ID: " + yogaClass.getClassId() + ")");
            }
            System.out.println("========================\n");


            boolean running = true;
            while (running) {
                System.out.println("=== Main Menu ===");
                System.out.println("1) Login as Member");
                System.out.println("2) Login as Trainer");
                System.out.println("3) Login as Admin");
                System.out.println("0) Exit");
                System.out.print("Select an option: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        runMemberFlow(scanner, memberService, memberId, trainerId, roomId, yogaClass);
                        break;
                    case "2":
                        runTrainerFlow(scanner, trainerService, trainerId);
                        break;
                    case "3":
                        runAdminFlow(scanner, classService, roomBookingService, trainer, room);
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.\n");
                }
            }

            System.out.println("\nExiting system. Goodbye!");

        } catch (Exception e) {
            System.err.println("Error occurred:");
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }

    // ================= MEMBER =================

    private static void runMemberFlow(Scanner scanner,
                                      Member_Service memberService,
                                      int memberId,
                                      int trainerId,
                                      int roomId,
                                      GROUP_CLASS yogaClass) {

        boolean inMemberMenu = true;
        while (inMemberMenu) {
            System.out.println("\n=== Member Menu (John Doe) ===");
            System.out.println("1) View Dashboard");
            System.out.println("2) Log Health Metric");
            System.out.println("3) Add Fitness Goal");
            System.out.println("4) Register for Group Class");
            System.out.println("5) Schedule PT Session");
            System.out.println("0) Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    memberService.viewDashboard(memberId);
                    break;

                case "2":
                    System.out.print("Enter metric type (e.g., Weight): ");
                    String type = scanner.nextLine();
                    System.out.print("Enter value (e.g., 80.5): ");
                    float value;
                    try {
                        value = Float.parseFloat(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number.");
                        break;
                    }
                    memberService.logHealthMetric(memberId, type, value);
                    break;

                case "3":
                    System.out.print("Enter goal type (e.g., Weight Loss): ");
                    String goalType = scanner.nextLine();
                    System.out.print("Enter target value (e.g., 75.0): ");
                    double target;
                    try {
                        target = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number.");
                        break;
                    }
                    memberService.addFitnessGoal(memberId, goalType, target);
                    break;

                case "4":
                    if (yogaClass == null) {
                        System.out.println("No class available to register.");
                        break;
                    }
                    memberService.registerForClass(memberId, yogaClass.getClassId());
                    break;

                case "5":
                    try {
                        System.out.println("Scheduling PT in Studio A with Jane Trainer (demo).");
                        System.out.print("Enter start hour (24h, e.g., 19): ");
                        int hour = Integer.parseInt(scanner.nextLine());
                        LocalDateTime ptStart = LocalDateTime.now().plusDays(1)
                                .withHour(hour).withMinute(0).withSecond(0).withNano(0);
                        LocalDateTime ptEnd = ptStart.plusHours(1);

                        boolean booked = memberService.schedulePTSession(
                                memberId,
                                trainerId,
                                roomId,
                                ptStart,
                                ptEnd
                        );
                        System.out.println("PT booking success: " + booked);
                    } catch (Exception e) {
                        System.out.println("Failed to schedule PT session: " + e.getMessage());
                    }
                    break;

                case "0":
                    inMemberMenu = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ================= TRAINER FLOW =================

    private static void runTrainerFlow(Scanner scanner,
                                       TrainerService trainerService,
                                       int trainerId) {

        boolean inTrainerMenu = true;
        while (inTrainerMenu) {
            System.out.println("\n=== Trainer Menu (Jane Trainer) ===");
            System.out.println("1) Set Availability");
            System.out.println("2) View Schedule (Next 7 Days)");
            System.out.println("3) Lookup Member by Name");
            System.out.println("0) Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    try {
                        System.out.print("Enter start hour (24h, e.g., 17): ");
                        int startHour = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter end hour (24h, e.g., 20): ");
                        int endHour = Integer.parseInt(scanner.nextLine());

                        LocalDateTime start = LocalDateTime.now().plusDays(1)
                                .withHour(startHour).withMinute(0).withSecond(0).withNano(0);
                        LocalDateTime end = LocalDateTime.now().plusDays(1)
                                .withHour(endHour).withMinute(0).withSecond(0).withNano(0);

                        boolean ok = trainerService.setAvailability(
                                trainerId,
                                Timestamp.valueOf(start),
                                Timestamp.valueOf(end),
                                "Once"
                        );
                        System.out.println("Availability added: " + ok);
                    } catch (Exception e) {
                        System.out.println("Failed to set availability: " + e.getMessage());
                    }
                    break;

                case "2":
                    LocalDateTime from = LocalDateTime.now();
                    LocalDateTime to = LocalDateTime.now().plusDays(7);
                    trainerService.viewSchedule(trainerId, from, to);
                    break;

                case "3":
                    System.out.print("Enter part of member name (e.g., 'john'): ");
                    String fragment = scanner.nextLine();
                    trainerService.lookupMemberByName(fragment);
                    break;

                case "0":
                    inTrainerMenu = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ================= ADMIN FLOW =================

    private static void runAdminFlow(Scanner scanner,
                                     ClassManagmentService classService,
                                     RoomBookingService roomBookingService,
                                     Trainer trainer,
                                     ROOM room) {

        boolean inAdminMenu = true;
        while (inAdminMenu) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1) Create New Group Class");
            System.out.println("2) Check Room Availability (Demo)");
            System.out.println("0) Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    try {
                        System.out.print("Enter class title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter description: ");
                        String desc = scanner.nextLine();
                        System.out.print("Enter capacity (e.g., 15): ");
                        int cap = Integer.parseInt(scanner.nextLine());

                        LocalDateTime start = LocalDateTime.now().plusDays(2)
                                .withHour(18).withMinute(0).withSecond(0).withNano(0);
                        LocalDateTime end = start.plusHours(1);

                        GROUP_CLASS gc = classService.createClass(
                                title,
                                desc,
                                start,
                                end,
                                cap,
                                "Scheduled",
                                trainer,
                                room
                        );

                        if (gc != null) {
                            System.out.println("Created class with ID: " + gc.getClassId());
                        } else {
                            System.out.println("Failed to create class (room unavailable).");
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to create class: " + e.getMessage());
                    }
                    break;

                case "2":
                    LocalDateTime checkStart = LocalDateTime.now().plusDays(1)
                            .withHour(18).withMinute(0).withSecond(0).withNano(0);
                    LocalDateTime checkEnd = checkStart.plusHours(1);
                    boolean available = roomBookingService.isRoomAvailable(room, checkStart, checkEnd);
                    System.out.println("Room " + room.getName() + " available at "
                            + checkStart + "? " + available);
                    break;

                case "0":
                    inAdminMenu = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}

//*