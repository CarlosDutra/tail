package net.sf.tail.indicator.tracker;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.sf.tail.sample.SampleTimeSeries;
import net.sf.tail.tick.DefaultTick;

import org.junit.Test;


public class DirectionalMovementIndicatorTest {

	@Test
	public void testGetValue()
	{
		List<DefaultTick> ticks = new ArrayList<DefaultTick>();
		
		ticks.add(new DefaultTick(0, 0, 10, 2));
		ticks.add(new DefaultTick(0, 0, 12, 2));
		ticks.add(new DefaultTick(0, 0, 15, 2));
		SampleTimeSeries series = new SampleTimeSeries(ticks);
		DirectionalMovementIndicator dm = new DirectionalMovementIndicator(series, 3);
		assertEquals(0d, dm.getValue(0));
		double dup = (2d / 3 + 2d/3) / (2d/3 + 12d/3);
		double ddown = (2d/3) /(2d/3 + 12d/3);
		assertEquals( (dup - ddown) /(dup + ddown) * 100d , dm.getValue(1));
		dup = ((2d / 3 + 2d/3) * 2d/3 + 1) / ((2d/3 + 12d/3) * 2d/3 + 15d/3);
		ddown = (4d/9) /((2d/3 + 12d/3) * 2d/3 + 15d/3);
		assertEquals( (dup - ddown) /(dup + ddown) * 100d , dm.getValue(2));
	}
}
