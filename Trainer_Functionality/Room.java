package com.team5;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int room_id;  // INT PRIMARY KEY AUTOINCREMENTED

    // Standard constructor (ID is autoincremented)
    public Room() { }

    // In-class getter/setter functions
    public int getID() {return room_id;}
}
