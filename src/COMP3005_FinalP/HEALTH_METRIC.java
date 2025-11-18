package COMP3005_FinalP;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HEALTH_METRIC")
public class HEALTH_METRIC {

    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Integer metricId;

    // FOREIGN KEY → MEMBER(MEMBER_id)
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "MEMBER_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_health_metric_MEMBER")
    )
    private MEMBER MEMBER;

    // Fields
    @Column(name = "metric_type", nullable = false, length = 255)
    private String metricType;

    @Column(name = "value", nullable = false)
    private Float value;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    // ----- Constructors -----
    public HEALTH_METRIC() {}

    public HEALTH_METRIC(MEMBER MEMBER, String metricType, Float value, LocalDateTime recordedAt) {
        this.MEMBER = MEMBER;
        this.metricType = metricType;
        this.value = value;
        this.recordedAt = recordedAt;
    }

    // ----- Getters & Setters -----
    public Integer getMetricId() {
        return metricId;
    }

    public MEMBER getMEMBER() {
        return MEMBER;
    }

    public void setMEMBER(MEMBER MEMBER) {
        this.MEMBER = MEMBER;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}

