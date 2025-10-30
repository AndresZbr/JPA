package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "TPAYMENTMEANS")
public abstract class PaymentMean extends BaseEntity {
    private double accumulated = 0.0;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "PaymentMean")
    private Set<Charge> charges = new HashSet<>();

    PaymentMean() {

    }

    public abstract boolean canPay(double amount);

    public abstract void pay(double amount);

    public Double getAccumulated() {
        return accumulated;
    }

    protected void addAccumulated(double amount) { // para Cash y CreditCard
        this.accumulated += amount;
    }

    void _setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public Set<Charge> getCharges() {
        return new HashSet<>(charges);
    }

    Set<Charge> _getCharges() {
        return charges;
    }
}
