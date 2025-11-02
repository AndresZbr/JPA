package uo.ri.cws.domain;

import java.time.LocalDateTime;
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
@Table(name = "TWORKORDERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "date", "vehicle_id" }) })
public class WorkOrder extends BaseEntity {
    public enum WorkOrderState {
        OPEN, ASSIGNED, FINISHED, INVOICED
    }

    // natural attributes
    private LocalDateTime date;
    private String description;
    private double amount = 0.0;
    private WorkOrderState state = WorkOrderState.OPEN;

    // accidental attributes
    @ManyToOne
    private Vehicle vehicle;
    @ManyToOne
    private Mechanic mechanic;
    @ManyToOne
    private Invoice invoice;

    @OneToMany(mappedBy = "workOrder")
    private Set<Intervention> interventions = new HashSet<>();

    WorkOrder() {
    }

    public WorkOrder(Vehicle vehicle) {
        this(vehicle, LocalDateTime.now(), "Trabar en ");
    }

    public WorkOrder(Vehicle vehicle, String description) {
        this(vehicle, LocalDateTime.now(), description);
    }

    public WorkOrder(Vehicle vehicle, LocalDateTime now) {
        this(vehicle, now, "Trabar en ");
    }

    public WorkOrder(Vehicle vehicle, LocalDateTime date, String description) {
        ArgumentChecks.isNotBlank(description);
        ArgumentChecks.isNotNull(date);
        ArgumentChecks.isNotNull(vehicle);

        this.date = date;
        this.description = description;
        this.date = date.withNano(date.getNano() / 1_000_000 * 1_000_000);
        Associations.Fixes.link(vehicle, this);
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public WorkOrderState getState() {
        return state;
    }

    public void setState(WorkOrderState state) {
        this.state = state;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Changes it to INVOICED state given the right conditions This method is
     * called from Invoice.addWorkOrder(...)
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not FINISHED, or -
     *                               The work order is not linked with the
     *                               invoice
     */
    public void markAsInvoiced() {
        if (state != WorkOrderState.FINISHED) {
            throw new IllegalStateException(
                    "WorkOrder must be FINISHED to be invoiced");
        }
        if (invoice == null) {
            throw new IllegalStateException(
                    "WorkOrder must be linked to an invoice");
        }
        state = WorkOrderState.INVOICED;
    }

    /**
     * Given the right conditions unlinks the workorder and the mechanic,
     * changes the state to FINISHED and computes the amount
     *
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in ASSIGNED
     *                               state, or
     */
    public void markAsFinished() {
        if (state != WorkOrderState.ASSIGNED) {
            throw new IllegalStateException(
                    "WorkOrder must be ASSIGNED to be finished");
        }
        state = WorkOrderState.FINISHED;
        mechanic._getAssigned()
                .remove(this);
        mechanic = null;
        computeAmount();
    }

    private void computeAmount() {
        double total = 0.0;
        for (Intervention i : interventions) {
            total += i.getAmount();
        }
        this.amount = total;
    }

    /**
     * Changes it back to FINISHED state given the right conditions This method
     * is called from Invoice.removeWorkOrder(...)
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not INVOICED, or
     */
    public void markBackToFinished() {
        if (state != WorkOrderState.INVOICED) {
            throw new IllegalStateException(
                    "WorkOrder must be INVOICED to revert back");
        }
        state = WorkOrderState.FINISHED;
        invoice = null;
    }

    /**
     * Links (assigns) the work order to a mechanic and then changes its state
     * to ASSIGNED
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in OPEN state,
     *                               or
     */
    public void assignTo(Mechanic mechanic) {
        ArgumentChecks.isNotNull(mechanic);
        if (state != WorkOrderState.OPEN) {
            throw new IllegalStateException(
                    "The work order is not in OPEN state");
        }
        this.mechanic = mechanic;
        mechanic._getAssigned()
                .add(this);
        this.state = WorkOrderState.ASSIGNED;

    }

    /**
     * Unlinks (deassigns) the work order and the mechanic and then changes its
     * state back to OPEN
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in ASSIGNED
     *                               state
     */
    public void unassign() {
        if (state != WorkOrderState.ASSIGNED) {
            throw new IllegalStateException(
                    "WorkOrder must be ASSIGNED to unassign");
        }
        mechanic._getAssigned()
                .remove(this);
        mechanic = null;
        state = WorkOrderState.OPEN;
    }

    /**
     * In order to assign a work order to another mechanic it first have to be
     * moved back to OPEN state.
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in FINISHED
     *                               state
     */
    public void reopen() {
        if (state != WorkOrderState.FINISHED) {
            throw new IllegalStateException(
                    "WorkOrder must be FINISHED to reopen");
        }
        state = WorkOrderState.OPEN;
        mechanic = null;
    }

    public Set<Intervention> getInterventions() {
        return new HashSet<>(interventions);
    }

    Set<Intervention> _getInterventions() {
        return interventions;
    }

    void _setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    void _setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    void _setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public boolean isFinished() {
        if (state.equals(WorkOrderState.FINISHED)) {
            return true;
        }
        return false;
    }

    public boolean isAssigned() {
        return state == WorkOrderState.ASSIGNED && mechanic != null;
    }

    public boolean isOpen() {
        return state == WorkOrderState.OPEN;
    }

    public boolean isInvoiced() {
        return state == WorkOrderState.INVOICED;
    }

}
