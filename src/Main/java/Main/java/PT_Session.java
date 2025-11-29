package Main.java;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "PT_SESSION")
public class PT_Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer sessionId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MEMBER member;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ROOM room;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;

    @Column(name = "status", length = 50)
    private String status;

    // Constructors
    public PT_Session() {}

    public PT_Session(MEMBER member, Trainer trainer, ROOM room,Timestamp startTime, Timestamp endTime, String status) {
        this.member = member;
        this.trainer = trainer;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // Getters & Setters
    public Integer getSessionId() { return sessionId; }

    public MEMBER getMember() { return member; }
    public void setMember(MEMBER member) { this.member = member; }

    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public ROOM getRoom() { return room; }
    public void setRoom(ROOM room) { this.room = room; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}