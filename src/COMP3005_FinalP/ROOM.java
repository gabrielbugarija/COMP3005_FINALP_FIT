package COMP3005_FinalP;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import COMP3005_FinalP.EQUIPMENT;


@Entity
@Table(name = "room")
public class ROOM {

  
    //Primary Key
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;


    //Columns
    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;


    //Relationships

    //ROOM(one) -> PT_SESSION(many) 
    @OneToMany(mappedBy = "room")
    private List<PT_SESSION> ptSessions = new ArrayList<>();

    //ROOM(one) -> GROUP_CLASS(many)
    @OneToMany(mappedBy = "room")
    private List<GROUP_CLASS> groupClasses = new ArrayList<>();

    //ROOM(one) -> EQUIPMENT(many)
    @OneToMany(mappedBy = "room")
    private List<EQUIPMENT> equipments = new ArrayList<>();


    //Contructors
    public ROOM() {}

    public ROOM(String name, String location, Integer capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    
    //Getters & Setters
    public Integer getRoomId() { return roomId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public List<PT_SESSION> getPtSessions() { return ptSessions; }
    public List<GROUP_CLASS> getGroupClasses() { return groupClasses; }
    public List<EQUIPMENT> getEquipments() { return equipments; }


}
