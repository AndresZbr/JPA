package uo.ri.cws.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;

@Entity

@Table(name = "TVOUCHERS")
public class Voucher extends PaymentMean {
    @Column(unique = true)
    private String code;
    private double available = 0.0;
    private String description;

    Voucher() {
    }

    public Voucher(String code, String description, double available) {
        ArgumentChecks.isNotBlank(code);
        ArgumentChecks.isNotBlank(description);
        ArgumentChecks.isNotNull(available);

        this.code = code;
        this.available = available;
        this.description = description;
    }

    /**
     * Augments the accumulated (super.pay(amount) ) and decrements the
     * available
     * 
     * @throws IllegalStateException if not enough available to pay
     */
    @Override
    public void pay(double amount) {
        if (!canPay(amount))
            throw new IllegalStateException("Not enough balance in voucher");
        addAccumulated(amount);
        available -= amount;
    }

    /**
     * A voucher can pay if it has enough available to pay the amount
     */
    @Override
    public boolean canPay(double amount) {
        if (amount <= available)
            return true;
        return false;
    }

    public String getCode() {
        return code;
    }

    public double getAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Voucher [code=" + code + ", available=" + available
                + ", description=" + description + "] " + super.toString();
    }

}
