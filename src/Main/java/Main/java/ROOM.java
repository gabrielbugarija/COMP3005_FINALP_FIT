package Main.java;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import Main.java.EQUIPMENT;


@Entity
@Table(name = "room")
public class ROOM {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    // Relationships
    @OneToMany(mappedBy = "room")
    private List<PT_Session> ptSessions = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<GROUP_CLASS> groupClasses = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<EQUIPMENT> equipments = new ArrayList<>();

    // Constructors
    public ROOM() {}

    public ROOM(String name, String location, Integer capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    // Getters & Setters
    public Integer getRoomId() { return roomId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public List<PT_Session> getPtSessions() { return ptSessions; }
    public List<GROUP_CLASS> getGroupClasses() { return groupClasses; }
    public List<EQUIPMENT> getEquipments() { return equipments; }
}
