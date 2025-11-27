package Main.java;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;

import Main.java.GROUP_CLASS;
import Main.java.ROOM;
import Main.java.RoomBookingService;
import Main.java.Trainer;



public class ClassManagementService {

    private EntityManager em;
    private RoomBookingService roomBookingService;

    public ClassManagementService(EntityManager em){
        this.em = em;
        this.roomBookingService = new RoomBookingService(em);
    }

    //Make new class
    public GROUP_CLASS createClass(String title, String description, LocalDateTime startTime,
                                LocalDateTime endTime, Integer capacity, 
                                String status, Trainer trainer, ROOM room) {

        GROUP_CLASS groupClass = new GROUP_CLASS(title, description, startTime, endTime, capacity, status, trainer, room);
        
        
        //Check if the room is available for a spifici time
        if(!roomBookingService.isRoomAvailable(room, startTime, endTime)){
            System.out.println("Cannot create class since Room is already booked.");
            return null;
        }

        try {
            em.getTransaction().begin();
            em.persist(groupClass);
            em.getTransaction().commit();
            return groupClass;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }

    }

    //Update the schedule and room of existing group class
    public boolean updateClassSchedule(GROUP_CLASS groupClass, LocalDateTime newStart,
                                    LocalDateTime newEnd, ROOM newRoom) 
    {
        if(!roomBookingService.isRoomAvailable(newRoom, newStart, newEnd)) {
            System.out.println("Cannot update class since Room is already booked.");
            return false;
        }

        //Update the class schedule in the db
        em.getTransaction().begin();

        try{
            groupClass.setStartTime(newStart);
            groupClass.setEndTime(newEnd);
            groupClass.setRoom(newRoom);
            em.getTransaction().commit();
            return true;
        } catch(Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }


}