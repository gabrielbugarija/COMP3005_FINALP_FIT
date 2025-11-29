package Main.java;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import Main.java.GROUP_CLASS;
import Main.java.PT_Session;
import Main.java.ROOM;

public class RoomBookingService {

        private EntityManager em;

        public RoomBookingService(EntityManager em){
            this.em = em;
        }


        //Check if there is room available
        public boolean isRoomAvailable(ROOM room, LocalDateTime startTime, LocalDateTime endTime){

            //Check if group class already exists
            TypedQuery<Long> queryGC = em.createQuery(
                    "SELECT COUNT(gc) FROM GROUP_CLASS gc WHERE gc.room = :room " +
                            "AND (gc.startTime < :endTime AND gc.endTime > :startTime)", Long.class
            );

            queryGC.setParameter("room", room);
            queryGC.setParameter("startTime", startTime);
            queryGC.setParameter("endTime", endTime);
            long count1 = queryGC.getSingleResult();


            //Check for PT sessions
            TypedQuery<Long> queryPT = em.createQuery(
                    "SELECT COUNT(pt) FROM PT_Session pt WHERE pt.room = :room " +
                            "AND (pt.startTime < :endTime AND pt.endTime > :startTime)", Long.class
            );


            queryPT.setParameter("room", room);
            queryPT.setParameter("startTime", startTime);
            queryPT.setParameter("endTime", endTime);
            long count2 = queryPT.getSingleResult();

            return count1 + count2 == 0;

        }

        //Book a room for a group class if any available
        public boolean bookRoomForClass(GROUP_CLASS groupClass, ROOM room) {
            if(isRoomAvailable(room, groupClass.getStartTime(), groupClass.getEndTime())) {
                groupClass.setRoom(room);
                em.getTransaction().begin();
                em.persist(groupClass);
                em.getTransaction().commit();
                return true;
            } else{
                System.out.println("Room is already booked for that time.");
                return false;
            }
        }


}
