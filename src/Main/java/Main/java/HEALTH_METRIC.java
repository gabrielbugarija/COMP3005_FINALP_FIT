package Main.java;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HEALTH_METRIC")
public class HEALTH_METRIC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Integer metricId;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "member_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_health_metric_member")
    )
    private MEMBER member;

    @Column(name = "metric_type", nullable = false, length = 255)
    private String metricType;

    @Column(name = "value", nullable = false)
    private Float value;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    // Constructors
    public HEALTH_METRIC() {}

    public HEALTH_METRIC(MEMBER member, String metricType, Float value, LocalDateTime recordedAt) {
        this.member = member;
        this.metricType = metricType;
        this.value = value;
        this.recordedAt = recordedAt;
    }

    // Getters & Setters
    public Integer getMetricId() { return metricId; }

    public MEMBER getMember() { return member; }
    public void setMember(MEMBER member) { this.member = member; }

    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }

    public Float getValue() { return value; }
    public void setValue(Float value) { this.value = value; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
