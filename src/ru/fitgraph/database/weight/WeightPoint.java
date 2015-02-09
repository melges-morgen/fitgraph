package ru.fitgraph.database.weight;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.fitgraph.database.marshals.WeightDateJsonDeserializer;
import ru.fitgraph.database.marshals.WeightDateJsonSerializer;
import ru.fitgraph.database.users.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Class for store information about users weight.
 * Information stored as points (time_moment, weight).
 *
 * @author Morgen Matvey
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "weight_points",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}, name = "datePerUser"))
@NamedQueries({
        @NamedQuery(name = "WeightPoint.getUserPointBetween", query = "select point from WeightPoint point " +
            "where point.owner = :owner and point.date > :startDate and point.date < :endDate"),
        @NamedQuery(name = "WeightPoint.getVkUserPointBetween", query = "select point " +
                "from WeightPoint point " +
                "where point.owner = (select user from User user where user.vkUserId = :ownerVkId) " +
                "and point.date > :startDate and point.date < :endDate")
})
public class WeightPoint {
    /**
     * Unique id of point.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long id;

    /**
     * Date when weight was registered.
     */
    @NotNull
    @XmlElement(required = true)
    @JsonSerialize(using = WeightDateJsonSerializer.class)
    @JsonDeserialize(using = WeightDateJsonDeserializer.class)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    /**
     * Registered weight.
     */
    @NotNull
    @XmlElement(required = true)
    @Column(name = "weight")
    private Double weight;

    /**
     * The user whose weight is registered.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = false, updatable = false)
    User owner;

    /**
     * Create unfilled point without owner
     */
    public WeightPoint() {
        this.id = null;
        this.weight = 0.0;
        this.owner = null;
        this.date = new Date();
    }

    /**
     * Create point, for weight registered right now.
     * @param weight registered weight.
     * @param owner user who register his weight.
     */
    public WeightPoint(Double weight, User owner) {
        this.id = null;
        this.weight = weight;
        this.owner = owner;
        this.date = new Date();
    }

    /**
     * Create point for specified with specified weight at specified moment
     * @param date moment when weight was registered.
     * @param weight registered weight.
     * @param owner user, who register weight.
     */
    public WeightPoint(Date date, Double weight, User owner) {
        this.date = date;
        this.weight = weight;
        this.owner = owner;
        this.date = date;
    }


    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Double getWeight() {
        return weight;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
