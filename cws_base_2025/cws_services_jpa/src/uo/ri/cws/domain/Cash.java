package uo.ri.cws.domain;

public class Cash extends PaymentMean {

    public Cash(Client client) {
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
}
