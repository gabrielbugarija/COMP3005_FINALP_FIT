package Main.java;

import java.io.Serializable;
import java.util.Objects;

import Main.java.GROUP_CLASS;
import Main.java.MEMBER;
import jakarta.persistence.*;


@Embeddable
public class CLASS_ENROLLMENT_ID implements Serializable{


    //Ids part of the composite
    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "member_id")
    private Integer memberId;



    //Constructors
    public CLASS_ENROLLMENT_ID() {}

    public CLASS_ENROLLMENT_ID(Integer classId, Integer memberId) {
        this.classId = classId;
        this.memberId = memberId;
    }

    
    //Getter & Setters
    public Integer getClassId() { return classId; }
    public void setClassId(Integer classId) { this.classId = classId; }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }



    //Check if two objects are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLASS_ENROLLMENT_ID)) return false;
        CLASS_ENROLLMENT_ID that = (CLASS_ENROLLMENT_ID) o;
        return Objects.equals(classId, that.classId) &&
               Objects.equals(memberId, that.memberId);
    }

    //Generate a hash for the composite key
    @Override
    public int hashCode() {
        return Objects.hash(classId, memberId);
    }

}