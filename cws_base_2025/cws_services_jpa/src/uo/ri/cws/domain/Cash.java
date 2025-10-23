package uo.ri.cws.domain;

public class Cash extends PaymentMean {

    Client client;

    public Cash(Client client) {
        Associations.Holds.link(this, client);
    }

    /**
     * A cash can always pay
     */
    @Override
    public boolean canPay(Double amount) {
        return true;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "Cash [client=" + client + "]";
    }

}
