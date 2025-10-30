package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVEHICLETYPES")
public class VehicleType extends BaseEntity {
    // natural attributes
    @Column(unique = true)
    private String name;
    private double pricePerHour;

    // accidental attributes
    @OneToMany(mappedBy = "VehicleType")
    private Set<Vehicle> vehicles = new HashSet<>();

    public VehicleType() {
    }

    public VehicleType(String name, double pricePerHour) {
        ArgumentChecks.isNotBlank(name);
        ArgumentChecks.isTrue(pricePerHour >= 0);

        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    public VehicleType(String name2) {
        this(name2, 0.0);
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<>(vehicles);
    }

    Set<Vehicle> _getVehicles() {
        return vehicles;
    }

    public String getName() {
        return name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    @Override
    public String toString() {
        return "VehicleType [name=" + name + ", pricePerHour=" + pricePerHour
                + "]";
    }

}
