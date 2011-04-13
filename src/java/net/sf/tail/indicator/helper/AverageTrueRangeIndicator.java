package net.sf.tail.indicator.helper;

import net.sf.tail.TimeSeries;
import net.sf.tail.indicator.cache.CachedIndicator;

public class AverageTrueRangeIndicator extends CachedIndicator<Double> {

	private final int timeFrame;
	private final TrueRangeIndicator tr;
	public AverageTrueRangeIndicator(TimeSeries series, int timeFrame) {
		this.timeFrame = timeFrame;
		this.tr = new TrueRangeIndicator(series);
	}
	
	@Override
	protected Double calculate(int index) {
		if(index == 0)
			return 1d;
		return ((getValue(index - 1) * (timeFrame-1)) / timeFrame) + tr.getValue(index) / timeFrame;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName() + " timeFrame: " + this.timeFrame;
	}

}
