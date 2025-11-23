package Main.java;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "group_class")
public class GROUP_CLASS {

    //Primary Key

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer classId;


    //Columns
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "status", nullable = false, length = 50)
    private String status;



    //Relations
    //GROUP_CLASS(one) -> CLASS_ENROLLMENT(many)
    @OneToMany(mappedBy = "groupClass")
    private List<CLASS_ENROLLMENT> classEnrollments = new ArrayList<>();

    //GROUP_CLASS(many) -> TRAINER(one)
    @ManyToOne
    @JoinColumn(
        name = "trainer_id", //FK
        nullable = false, 
        foreignKey = @ForeignKey(name = "fk_group_class_trainer"))
    private Trainer trainer;

    //GROUP_CLASS(many) -> ROOM(one)
    @ManyToOne
    @JoinColumn(
        name = "room_id", //FK
        nullable = false, 
        foreignKey = @ForeignKey(name = "fk_group_class_room"))
    private ROOM room;


    //Constructors

    public GROUP_CLASS() {}

    public GROUP_CLASS(
        String title, String description, LocalDateTime startTime, LocalDateTime endTime,
        Integer capacity, String status,Trainer trainer, ROOM room) {
        
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.status = status;
        this.trainer = trainer;
        this.room = room;
    }


    //Getters & Setters

    public Integer getClassId() { return classId; }

    public String getTitle() { return title; } 
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public ROOM getRoom() { return room; }
    public void setRoom(ROOM room) { this.room = room; }

    public List<CLASS_ENROLLMENT> getClassEnrollments() { return classEnrollments; }
}
