package Main.java;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TRAINER_AVAILABILITY")
public class Trainer_Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private Integer availabilityId;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;

    @Column(name = "recurrence_pattern", length = 50)
    private String recurrencePattern;

    // Constructors
    public Trainer_Availability() {}

    public Trainer_Availability(Trainer trainer, Timestamp startTime,
                                Timestamp endTime, String recurrencePattern) {
        this.trainer = trainer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
    }

    // Getters & Setters
    public Integer getAvailabilityId() { return availabilityId; }

    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public String getRecurrencePattern() { return recurrencePattern; }
    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }
}