package uo.ri.cws.domain;

import java.time.LocalDate;

public class CreditCard extends PaymentMean {
	private String number;
	private String type;
	private LocalDate validThru;
	
	
	/**
	 * A credit card can pay if is not outdated 
	 */
	@Override
	public boolean canPay(Double amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
