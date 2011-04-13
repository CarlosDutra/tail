package net.sf.tail.indicator.helper;

import net.sf.tail.Indicator;
import net.sf.tail.TimeSeries;

public class TrueRangeIndicator implements Indicator<Double>{

	private TimeSeries series;

	public TrueRangeIndicator(TimeSeries series) {
		this.series = series;
		this.getValue(0);
	}
	public String getName() {
		return getClass().getSimpleName();
	}
	
	
	public Double getValue(int index) {
		
		double ts = series.getTick(index).getMaxPrice() - series.getTick(index).getMinPrice();
		double ys = index == 0? 0 : series.getTick(index).getMaxPrice() - series.getTick(index - 1).getClosePrice();
		double yst = index == 0? 0 : series.getTick(index - 1).getClosePrice() - series.getTick(index).getMinPrice();
		double max = Math.max(Math.abs(ts), Math.abs(ys));
		
		return Math.max(max, Math.abs(yst));
		
	}
	

}
