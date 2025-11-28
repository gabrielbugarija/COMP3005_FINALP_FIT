package Main.java;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import Main.java.GROUP_CLASS;
import Main.java.MEMBER;




//The entity class
@Entity
@Table(name = "class_enrollment")
public class CLASS_ENROLLMENT {
    
    @EmbeddedId
    private CLASS_ENROLLMENT_ID id;


    //Relationships
    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private GROUP_CLASS groupClass;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private MEMBER member;
    
    //Column
    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt;


    //Constructors
    public CLASS_ENROLLMENT() {}

    public CLASS_ENROLLMENT(GROUP_CLASS groupClass, MEMBER member){
        this.groupClass = groupClass;
        this.member = member;
        this.id = new CLASS_ENROLLMENT_ID(groupClass.getClassId(), member.getMemberId());
        this.enrolledAt = LocalDateTime.now();
    }

    //Getters
    public CLASS_ENROLLMENT_ID getId() { return id; }
    public GROUP_CLASS getGroupClass() { return groupClass; }
    public MEMBER getMember() { return member; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }

    //Setter
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
    




}
