package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.util.assertion.ArgumentChecks;

public class Intervention {
    // natural attributes
    private LocalDateTime date;
    private int minutes;

    // accidental attributes
    private WorkOrder workOrder;
    private Mechanic mechanic;
    private Set<Substitution> substitutions = new HashSet<>();

    void _setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
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
    public int hashCode() {
        return Objects.hash(date, mechanic, workOrder);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Intervention other = (Intervention) obj;
        return Objects.equals(date, other.date)
                && Objects.equals(mechanic, other.mechanic)
                && Objects.equals(workOrder, other.workOrder);
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

}
