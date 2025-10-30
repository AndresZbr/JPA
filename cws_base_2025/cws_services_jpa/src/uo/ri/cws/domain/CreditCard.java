package uo.ri.cws.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;

@Entity

@Table(name = "TCREDITCARDS")
public class CreditCard extends PaymentMean {
    @Column(unique = true)
    private String number;
    private String type;
    private LocalDate validThru;

    CreditCard() {

    }

    public CreditCard(String number, String type, LocalDate validThru) {
        ArgumentChecks.isNotBlank(number);
        ArgumentChecks.isNotBlank(type);
        ArgumentChecks.isNotNull(validThru);

        this.number = number;
        this.type = type;
        this.validThru = validThru;
    }

    public String getNumber() {
        return this.number;
    }

    public String getType() {
        return this.type;
    }

    public LocalDate getValidThru() {
        return this.validThru;
    }

    /**
     * A credit card can pay if is not outdated
     */
    @Override
    public boolean canPay(double amount) {
        if (LocalDate.now()
                     .isAfter(validThru))
            return false;
        return true;
    }

    @Override
    public void pay(double amount) {
        if (!canPay(amount))
            throw new IllegalStateException("Credit card expired");
        addAccumulated(amount);
    }

    @Override
    public String toString() {
        return "CreditCard [number=" + number + ", type=" + type
                + ", validThru=" + validThru + "] " + super.toString();
    }

}
