package Main.java;

import jakarta.persistence.*;

@Entity
@Table(name = "TRAINER")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trainer_id;            // INT PRIMARY KEY AUTOINCREMENTED

    private String name;               // VARCHAR
    private String speciality;         // VARCHAR
    private String email;              // VARCHAR UNIQUE
    private String phone;              // VARCHAR
    private String employment_status;  // VARCHAR

    // Default constructor
    public Trainer() { }

    // Standard constructor (ID is autoincremented)
    public Trainer(String name, String speciality, String email, String phone, String employment_status) {
        setName(name);
        setSpeciality(speciality);
        setEmail(email);
        setPhone(phone);
        setStatus(employment_status);
    }

    // In-class getter/setter functions
    public int getID() {return trainer_id;}
    public String getName() {return name;}
    public String getSpeciality() {return speciality;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getStatus() {return employment_status;}

    public void setName(String name) {this.name = name;}
    public void setSpeciality(String speciality) {this.speciality = speciality;}
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setStatus(String employment_status) {this.employment_status = employment_status;}
}
