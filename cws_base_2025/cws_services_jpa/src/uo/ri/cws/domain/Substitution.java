package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSUBSTITUTIONS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "intervention_id",
                "sparepart_id" }) })
public class Substitution extends BaseEntity {
    // natural attributes
    private int quantity;

    // accidental attributes
    @ManyToOne
    private SparePart sparePart;
    @ManyToOne
    private Intervention intervention;

    Substitution() {
    }

    public Substitution(SparePart sparePart, Intervention intervention,
            int quantity) {
        // validar
        ArgumentChecks.isNotNull(sparePart);
        ArgumentChecks.isNotNull(intervention);
        ArgumentChecks.isTrue(quantity > 0);

        this.quantity = quantity;
        Associations.Substitutes.link(sparePart, this, intervention);
    }

    public int getQuantity() {
        return quantity;
    }

    public SparePart getSparePart() {
        return sparePart;
    }

    public Intervention getIntervention() {
        return intervention;
    }

    @Override
    public String toString() {
        return "Substitution [quantity=" + quantity + ", sparePart=" + sparePart
                + ", intervention=" + intervention + "]";
    }

    void _setSparePart(SparePart sparePart) {
        this.sparePart = sparePart;
    }

    void _setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public double getAmount() {
        return sparePart.getPrice() * quantity;
    }

}
