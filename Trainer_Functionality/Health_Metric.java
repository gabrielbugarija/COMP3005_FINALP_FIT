package com.team5;

import jakarta.persistence.*;

@Entity
@Table(name = "health_metric")
public class Health_Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int metric_id;  // INT PRIMARY KEY AUTOINCREMENTED

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member_id;  // FOREIGN KEY

    // Default constructor
    public Health_Metric() { }

    // Standard constructor (ID is autoincremented)
    public Health_Metric(Member member) {
        setMemberID(member);
    }

    // In-class getter/setter functions
    public int getID() {return metric_id;}
    public int getMemberID() {return member_id.getID();}
    public void setMemberID(Member member) {this.member_id = member;}
}
