package org.cz.json.fees;

public class Fee {

	private String name;
	private double amount;
	private double min;
	private double max;
	
	public Fee()
	{
	}
	
	public double calc(double traded)
	{
		if (name.equalsIgnoreCase("stamp"))
		{
			double fee = (traded / 1000.0) * amount;
			fee = Math.ceil(fee);
			if (fee > max)
				return max;
			return fee;
		}
		double fee = (traded * amount) / 100.0;
		if (max>0.0 && fee>max)
			fee=max;
		else
		if (min>0.0 && fee<min)
			fee=min;
		return fee;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	
	
}
