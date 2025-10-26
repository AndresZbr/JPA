package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

public abstract class PaymentMean {
    private double accumulated = 0.0;
    private Client client;
    private Set<Charge> charges = new HashSet<>();

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
