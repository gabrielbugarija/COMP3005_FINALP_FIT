package com.team5;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "trainer_availability")
public class Trainer_Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int availability_id;        // INT PRIMARY KEY AUTOINCREMENTED

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer_id;             // FOREIGN KEY

    private Timestamp start_time;       // TIMESTAMP
    private Timestamp end_time;         // TIMESTAMP
    private String recurrence_pattern;  // VARCHAR

    // Default constructor
    public Trainer_Availability() { }

    // Standard constructor (ID is autoincremented)
    public Trainer_Availability(Trainer trainer, Timestamp start_time, Timestamp end_time, String recurrence_pattern) {
        setTrainerID(trainer);
        setStartTime(start_time);
        setEndTime(end_time);
        setPattern(recurrence_pattern);
    }

    // In-class getter/setter functions
    public int getID() {return availability_id;}
    public int getTrainerID() {return trainer_id.getID();}
    public Timestamp getStartTime() {return start_time;}
    public Timestamp getEndTime() {return end_time;}
    public String getPattern() {return recurrence_pattern;}

    public void setTrainerID(Trainer trainer) {this.trainer_id = trainer;}
    public void setStartTime(Timestamp start_time) {this.start_time = start_time;}
    public void setEndTime(Timestamp end_time) {this.end_time = end_time;}
    public void setPattern(String recurrence_pattern) {this.recurrence_pattern = recurrence_pattern;};
}
