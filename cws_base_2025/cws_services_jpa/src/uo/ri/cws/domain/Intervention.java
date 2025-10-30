package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TINTERVENTIONS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "date", "mechanic_id",
                "workorder_id" }) })
public class Intervention extends BaseEntity {
    // natural attributes
    private LocalDateTime date;
    private int minutes;

    // accidental attributes
    @ManyToOne
    private WorkOrder workOrder;
    @ManyToOne
    private Mechanic mechanic;

    @OneToMany(mappedBy = "Intervention")
    private Set<Substitution> substitutions = new HashSet<>();

    void _setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Intervention() {
    }

    public Intervention(Mechanic mechanic, WorkOrder workOrder, int minutes) {

        this(mechanic, workOrder, LocalDateTime.now(), minutes);
    }

    public Intervention(Mechanic mechanic, WorkOrder workOrder,
            LocalDateTime date, int minutes) {
        ArgumentChecks.isNotNull(mechanic);
        ArgumentChecks.isNotNull(workOrder);
        ArgumentChecks.isNotNull(date);
        ArgumentChecks.isTrue(minutes >= 0);
        this.date = date;
        this.minutes = minutes;
        Associations.Intervenes.link(workOrder, this, mechanic);
    }

    void _setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public Set<Substitution> getSubstitutions() {
        return new HashSet<>(substitutions);
    }

    Set<Substitution> _getSubstitutions() {
        return substitutions;
    }

    @Override
    public String toString() {
        return "Intervention [date=" + date + ", minutes=" + minutes
                + ", workOrder=" + workOrder + ", mechanic=" + mechanic + "]";
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public Double getAmount() {
        double labor = workOrder.getVehicle()
                                .getVehicleType()
                                .getPricePerHour()
                * minutes / 60.0;

        double spares = substitutions.stream()
                                     .mapToDouble(s -> s.getSparePart()
                                                        .getPrice()
                                             * s.getQuantity())
                                     .sum();

        return labor + spares;
    }

    public LocalDateTime getDate() {
        return date.truncatedTo(ChronoUnit.MILLIS);
    }

    public int getMinutes() {
        return minutes;
    }

}
