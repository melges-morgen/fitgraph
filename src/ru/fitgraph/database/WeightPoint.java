package ru.fitgraph.database;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.fitgraph.database.marshals.WeightDateJsonSerializer;
import ru.fitgraph.database.users.User;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by melges on 16.01.15.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "weight_points")
public class WeightPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long id;

    @XmlElement
    @JsonSerialize(using = WeightDateJsonSerializer.class)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @XmlElement
    @Column(name = "weight")
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = false, updatable = false)
    User owner;

    public WeightPoint() {
        this.id = null;
        this.weight = 50.0;
        this.owner = null;
        this.date = new Date();
    }

    public WeightPoint(Double weight, User owner) {
        this.id = null;
        this.weight = weight;
        this.owner = owner;
        this.date = new Date();
    }

    public WeightPoint(Date date, Double weight, User owner) {
        this.date = date;
        this.weight = weight;
        this.owner = owner;
        this.date = date;
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
}
