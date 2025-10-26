package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class Invoice {
    public enum InvoiceState {
        NOT_YET_PAID, PAID
    }

    // natural attributes
    private Long number;
    private LocalDate date;
    private double amount;
    private double vat;
    private InvoiceState state = InvoiceState.NOT_YET_PAID;

    // accidental attributes
    private Set<WorkOrder> workOrders = new HashSet<>();
    private Set<Charge> charges = new HashSet<>();

    public Invoice(Long number) {
        this(number, LocalDate.now(), List.of());
    }

    public Invoice(Long number, LocalDate date) {
        this(number, date, List.of());
    }

    public Invoice(Long number, List<WorkOrder> workOrders) {
        this(number, LocalDate.now(), workOrders);
    }

    // full constructor
    public Invoice(Long number, LocalDate date, List<WorkOrder> workOrders) {
        // check arguments (always), through IllegalArgumentException store the
        // number add every work order calling addWorkOrder( w )
        if (number == null) {
            throw new IllegalArgumentException("Invoice number cannot be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Invoice date cannot be null");
        }
        if (workOrders == null) {
            throw new IllegalArgumentException(
                    "WorkOrders list cannot be null");
        }

        this.number = number;
        this.date = date;

        for (WorkOrder wo : workOrders) {
            addWorkOrder(wo);
        }

        computeAmount(); // calculamos amount y vat iniciales
    }

    /**
     * Computes amount and vat (vat depends on the date)
     */
    private void computeAmount() {
        double sum = 0.0;
        for (WorkOrder wo : workOrders) {
            sum += wo.getAmount();
        }
        double rate = ( date.isBefore(LocalDate.of(2012, 7, 1)) ) ? 0.18 : 0.21;
        this.vat = rate;
        this.amount = sum * ( 1 + rate );
    }

    /**
     * Adds (double links) the workOrder to the invoice and updates the amount
     * and vat
     * 
     * @param workOrder
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
     * @throws IllegalStateException if the workorder status is not FINISHED
     */
    public void addWorkOrder(WorkOrder workOrder) {
        if (workOrder == null)
            throw new IllegalArgumentException("workOrder cannot be null");

        if (!state.equals(InvoiceState.NOT_YET_PAID))
            throw new IllegalStateException("Invoice not NOT_YET_PAID");

        if (!workOrder.isFinished())
            throw new IllegalStateException("workorder not finished");

        workOrders.add(workOrder);
        workOrder._setInvoice(this);

        workOrder.markAsInvoiced();
        computeAmount();
    }

    /**
     * Removes a work order from the invoice, updates the workorder state and
     * recomputes amount and vat
     * 
     * @param workOrder
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException    if the invoice status is not
     *                                  NOT_YET_PAID
     * @throws IllegalArgumentException if the invoice does not contain the
     *                                  workorder
     */
    public void removeWorkOrder(WorkOrder workOrder) {
        if (workOrder == null) {
            throw new IllegalArgumentException(
                    "Cannot remove a null work order");
        }
        if (!state.equals(InvoiceState.NOT_YET_PAID)) {
            throw new IllegalStateException(
                    "Cannot remove work order from a settled invoice");
        }
        if (!workOrders.contains(workOrder)) {
            throw new IllegalArgumentException(
                    "Invoice does not contain the work order");
        }
        workOrders.remove(workOrder);

        workOrder.markBackToFinished();

        computeAmount();
    }

    /**
     * Marks the invoice as PAID, but
     * 
     * @throws IllegalStateException if - Is already settled - Or the amounts
     *                               paid with charges to payment means do not
     *                               cover the total of the invoice
     */
    public void settle() {
        if (state == InvoiceState.PAID) {
            throw new IllegalStateException("Invoice already settled");
        }

        double totalPaid = 0.0;
        for (Charge c : charges) {
            totalPaid += c.getAmount();
        }

        if (totalPaid < amount) {
            throw new IllegalStateException(
                    "Charges do not cover invoice amount");
        }

        state = InvoiceState.PAID;

    }

    public Set<WorkOrder> getWorkOrders() {
        return new HashSet<>(workOrders);
    }

    Set<WorkOrder> _getWorkOrders() {
        return workOrders;
    }

    public Set<Charge> getCharges() {
        return new HashSet<>(charges);
    }

    Set<Charge> _getCharges() {
        return charges;
    }

    public Long getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public double getVat() {
        return vat;
    }

    public InvoiceState getState() {
        return state;
    }

    public BooleanSupplier isNotSettled() {
        return () -> state == InvoiceState.NOT_YET_PAID;
    }

}
