package org.xpa.example.data.orders;

import java.math.BigDecimal;

import org.xpaframework.xml.annotation.XmlAttribute;
import org.xpaframework.xml.annotation.XmlValue;
import org.xpaframework.xml.annotation.XmlValueAdapter;

public class Price {

	@XmlValue
    private BigDecimal value;
	
	@XmlAttribute
	@XmlValueAdapter(adapter = Currency.Adapter.class)
    private Currency currency;
	
    public BigDecimal getValue() {
		return value;
	}
	
    public void setValue(BigDecimal value) {
		this.value = value;
	}
	
    public Currency getCurrency() {
		return currency;
	}
	
    public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
