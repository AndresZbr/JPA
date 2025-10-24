package uo.ri.cws.domain;

import java.time.LocalDate;

import uo.ri.util.assertion.ArgumentChecks;

public class CreditCard extends PaymentMean {
    private String number;
    private String type;
    private LocalDate validThru;

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
    public boolean canPay(Double amount) {
        // TODO Auto-generated method stub
        return false;
    }

}
