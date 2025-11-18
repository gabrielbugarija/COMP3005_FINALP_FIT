package COMP3005_FinalP;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "FITNESS_GOAL")
public class FITNESS_GOAL {

    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Integer goalId;

    // FOREIGN KEY → MEMBER(member_id)
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "member_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_fitness_goal_member")
    )
    private MEMBER member;

    // Fields
    @Column(name = "goal_type", nullable = false, length = 255)
    private String goalType;

    @Column(name = "target_value", nullable = false)
    private BigDecimal targetValue;   // or Double/Float if you prefer

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status", length = 50)
    private String status;

    // ----- Constructors -----
    public FITNESS_GOAL() {}

    public FITNESS_GOAL(MEMBER member,
                       String goalType,
                       BigDecimal targetValue,
                       LocalDate startDate,
                       LocalDate endDate,
                       String status) {
        this.member = member;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // ----- Getters & Setters -----
    public Integer getGoalId() {
        return goalId;
    }

    public MEMBER getMember() {
        return member;
    }

    public void setMember(MEMBER member) {
        this.member = member;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(BigDecimal targetValue) {
        this.targetValue = targetValue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
