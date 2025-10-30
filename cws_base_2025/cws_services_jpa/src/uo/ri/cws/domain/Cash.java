package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;

@Entity

@Table(name = "TCASHES")
public class Cash extends PaymentMean {

    Cash() {
    }

    public Cash(Client client) {
        ArgumentChecks.isNotNull(client, "Client null");
        Associations.Holds.link(this, client);
    }

    /**
     * A cash can always pay
     */
    @Override
    public boolean canPay(double amount) {
        return true;
    }

    @Override
    public void pay(double amount) {
        addAccumulated(amount);
    }

    @Override
    public String toString() {
        return "Cash [toString()=" + super.toString();
    }
}
