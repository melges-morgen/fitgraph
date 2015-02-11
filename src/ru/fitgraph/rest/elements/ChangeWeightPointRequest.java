package ru.fitgraph.rest.elements;

import ru.fitgraph.database.weight.WeightPoint;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Element for serialize weight point change request
 *
 * @author Morgen Matvey
 */
@XmlRootElement
public class ChangeWeightPointRequest {
    @NotNull
    @XmlElement(name = "oldPoint", required = true, nillable = false)
    private WeightPoint oldPoint;

    @NotNull
    @XmlElement(name = "newPoint", required = true, nillable = false)
    private WeightPoint newPoint;

    public ChangeWeightPointRequest() {
    }

    public WeightPoint getOldPoint() {
        return oldPoint;
    }

    public WeightPoint getNewPoint() {
        return newPoint;
    }
}
