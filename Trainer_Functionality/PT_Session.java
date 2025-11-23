package com.team5;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "pt_session")
public class PT_Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int session_id;        // INT PRIMARYY KEY AUTOINCREMENTED

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member_id;         // FOREIGN KEY

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer_id;        // FOREIGN KEY

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room_id;           // FOREIGN KEY

    private Timestamp start_time;  // TIMESTAMP
    private Timestamp end_time;    // TIMESTAMP
    private String status;         // VARCHAR

    // Default constructor
    public PT_Session() { }

    // Standard constructor (ID is autoincremented)
    public PT_Session(Member member, Trainer trainer, Room room, Timestamp start_time, Timestamp end_time, String status) {
        setMemberID(member);
        setTrainerID(trainer);
        setRoomID(room);
        setStartTime(start_time);
        setEndTime(end_time);
        setStatus(status);
    }

    // In-class getter/setter functions
    public int getID() {return session_id;}
    public int getMemberID() {return member_id.getID();}
    public int getTrainerID() {return trainer_id.getID();}
    public int getRoomID() {return room_id.getID();}
    public Timestamp getStartTime() {return start_time;}
    public Timestamp getEndTime() {return end_time;}
    public String getStatus() {return status;}

    public void setMemberID(Member member) {this.member_id = member;}
    public void setTrainerID(Trainer trainer) {this.trainer_id = trainer;}
    public void setRoomID(Room room) {this.room_id = room;}
    public void setStartTime(Timestamp start_time) {this.start_time = start_time;}
    public void setEndTime(Timestamp end_time) {this.end_time = end_time;}
    public void setStatus(String status) {this.status = status;};
}
