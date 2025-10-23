package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

public abstract class PaymentMean {
	// natural attributes
	private double accumulated = 0.0;

	// accidental attributes
	private Client client;
	private Set<Charge> charges = new HashSet<>();

	public abstract boolean canPay(Double amount);

	public void pay(double importe) {
		this.accumulated += importe;
	}

	void _setClient(Client client) {
		this.client = client;
	}

	public Set<Charge> getCharges() {
		return new HashSet<>( charges );
	}

	Set<Charge> _getCharges() {
		return charges;
	}

}
