package com.team5;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int member_id;  // INT PRIMARY KEY AUTOINCREMENTED

    private String name;    // VARCHAR

    // Default constructor
    public Member() {}

    // Standard constructor (ID is autoincremented)
    public Member(String first_name, String laString) {setName(first_name);}

    // In-class getter/setter functions
    public int getID() {return member_id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
