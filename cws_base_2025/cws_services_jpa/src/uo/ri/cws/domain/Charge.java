package uo.ri.cws.domain;

public class Charge {
    // natural attributes
    private double amount = 0.0;

    // accidental attributes
    private Invoice invoice;
    private PaymentMean paymentMean;

    public Invoice getInvoice() {
        return invoice;
    }

    public PaymentMean getPaymentMean() {
        return paymentMean;
    }

    public Charge(Invoice invoice, PaymentMean pm, double amount) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        if (pm == null) {
            throw new IllegalArgumentException("PaymentMean cannot be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        // Validación de si el payment mean puede pagar
        if (!pm.canPay(amount)) {
            throw new IllegalStateException(
                    "Cannot pay this amount with the given payment mean");
        }

        this.amount = amount;

        // Aplica el pago
        pm.pay(amount);

        // Enlaza charge, invoice y payment mean
        Associations.Settles.link(invoice, this, pm);
    }

    /**
     * Unlinks this charge and restores the accumulated to the payment mean
     * 
     * @throws IllegalStateException if the invoice is already settled
     */
    public void rewind() {
        // asserts the invoice is not in PAID status decrements the payment mean
        // accumulated ( paymentMean.pay( -amount) ) unlinks invoice, this and
        // paymentMean
        if (invoice.getState() == Invoice.InvoiceState.PAID) {
            throw new IllegalStateException(
                    "Cannot rewind a charge from a paid invoice");
        }
        paymentMean.pay(-amount);
        Associations.Settles.unlink(this);
    }

    public void _setInvoice(Invoice invoice) {
        this.invoice = invoice;

    }

    public void _setPaymentMean(PaymentMean mp) {
        this.paymentMean = mp;
    }

    public double getAmount() {
        return amount;
    }

}
