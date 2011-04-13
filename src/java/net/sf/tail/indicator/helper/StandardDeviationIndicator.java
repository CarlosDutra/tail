package net.sf.tail.indicator.helper;

import net.sf.tail.Indicator;
import net.sf.tail.indicator.tracker.SMAIndicator;

public class StandardDeviationIndicator implements Indicator<Double> {

	private Indicator<? extends Number> indicator;

	private int timeFrame;

	private SMAIndicator sma;

	public StandardDeviationIndicator(Indicator<? extends Number> indicator, int timeFrame) {
		this.indicator = indicator;
		this.timeFrame = timeFrame;
		sma = new SMAIndicator(indicator, timeFrame);
	}

	public Double getValue(int index) {
		double standardDeviation = 0.0;
		double average = sma.getValue(index);
		for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
			standardDeviation += Math.pow(indicator.getValue(i).doubleValue() - average, 2.0);
		}
		return Math.sqrt(standardDeviation);
	}

	public String getName() {
		return getClass().getSimpleName() + " timeFrame: " + timeFrame;
	}
}
