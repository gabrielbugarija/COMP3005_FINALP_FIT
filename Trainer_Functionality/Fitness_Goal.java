package com.team5;

import jakarta.persistence.*;

@Entity
@Table(name = "fitness_goal")
public class Fitness_Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int goal_id;    // INT PRIMARY KEY AUTOINCREMENTED

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member_id;  // FOREIGN KEY

    // Default constructor
    public Fitness_Goal() { }

    // Standard constructor (ID is autoincremented)
    public Fitness_Goal(Member member) {
        setMemberID(member);
    }

    // In-class getter/setter functions
    public int getID() {return goal_id;}
    public int getMemberID() {return member_id.getID();}
    public void setMemberID(Member member) {this.member_id = member;}
}
