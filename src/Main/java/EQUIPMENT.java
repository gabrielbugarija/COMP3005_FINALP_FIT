package Main.java;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "equipment")
public class EQUIPMENT {


    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer equipmentId;


    //Columns
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "type", nullable = false, length = 255)
    private String type;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "last_maintenance_date", nullable = false)
    private LocalDate lastMaintenanceDate;


    //Relations
    //EQUIPMENT(many) -> ROOM(one)
    @ManyToOne
    @JoinColumn(
        name = "room_id", //FK
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_equipment_room"))
    private ROOM room;


    //Constructors
    public EQUIPMENT() {}

    public EQUIPMENT(
        String name, String type, String status, 
        LocalDate lastMaintenanceDate, ROOM room) {

        this.name = name;
        this.type = type;
        this.status = status;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.room = room;
    }


    //Getters & Setters
    public Integer getEquipmentId() { return equipmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) { 
        this.lastMaintenanceDate = lastMaintenanceDate; 
    }

    public ROOM getRoom() { return room; }
    public void setRoom(ROOM room) { this.room = room; }
}
