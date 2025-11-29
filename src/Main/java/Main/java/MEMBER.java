
package Main.java;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "MEMBER")
public class MEMBER {


    //PRIMARY KEY


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;


    //FIELDS

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "email", nullable = false, length = 255)
    private String email; // Unique via table constraint

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    //RELATIONSHIPS


    // MEMBER 1 → * FITNESS_GOAL
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FITNESS_GOAL> fitnessGoals = new ArrayList<>();

    // MEMBER 1 → * HEALTH_METRIC
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HEALTH_METRIC> healthMetrics = new ArrayList<>();

    // MEMBER 1 → * PT_SESSION
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PT_Session> ptSessions = new ArrayList<>();

    // MEMBER 1 → * CLASS_ENROLLMENT (bridge table for classes)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CLASS_ENROLLMENT> classEnrollments = new ArrayList<>();


    // CONSTRUCTORS


    public MEMBER() {}

    public MEMBER(String name,
                  LocalDate dateOfBirth,
                  String gender,
                  String email,
                  String phone,
                  String address,
                  LocalDateTime registrationDate) {

        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = registrationDate;
    }


    // GETTERS / SETTERS


    public Integer getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<FITNESS_GOAL> getFitnessGoals() {
        return fitnessGoals;
    }

    public void setFitnessGoals(List<FITNESS_GOAL> fitnessGoals) {
        this.fitnessGoals = fitnessGoals;
    }

    public List<HEALTH_METRIC> getHealthMetrics() {
        return healthMetrics;
    }

    public void setHealthMetrics(List<HEALTH_METRIC> healthMetrics) {
        this.healthMetrics = healthMetrics;
    }

    public List<PT_Session> getPtSessions() {
        return ptSessions;
    }

    public void setPtSessions(List<PT_Session> ptSessions) {
        this.ptSessions = ptSessions;
    }

    public List<CLASS_ENROLLMENT> getClassEnrollments() {
        return classEnrollments;
    }

    public void setClassEnrollments(List<CLASS_ENROLLMENT> classEnrollments) {
        this.classEnrollments = classEnrollments;
    }
}

