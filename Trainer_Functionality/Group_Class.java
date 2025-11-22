package com.team5;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "group_class")
public class Group_Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int class_id;    // INT PRIMARY KEY AUTOINCREMENTED

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer_id;  // FOREIGN KEY

    private Timestamp start_time;  // TIMESTAMP
    private Timestamp end_time;    // TIMESTAMP

    // Default constructor
    public Group_Class() { }

    // Standard constructor (ID is autoincremented)
    public Group_Class(Trainer trainer, Timestamp start_time, Timestamp end_time) {
        setTrainerID(trainer);
        setStartTime(start_time);
        setEndTime(end_time);
    }

    // In-class getter/setter functions
    public int getID() {return class_id;}
    public int getTrainerID() {return trainer_id.getID();}
    public Timestamp getStartTime() {return start_time;}
    public Timestamp getEndTime() {return end_time;}

    public void setTrainerID(Trainer trainer) {this.trainer_id = trainer;}
    public void setStartTime(Timestamp start_time) {this.start_time = start_time;}
    public void setEndTime(Timestamp end_time) {this.end_time = end_time;}
}
