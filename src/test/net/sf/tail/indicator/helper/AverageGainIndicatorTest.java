package net.sf.tail.indicator.helper;

import static org.junit.Assert.assertEquals;
import net.sf.tail.TimeSeries;
import net.sf.tail.indicator.simple.ClosePriceIndicator;
import net.sf.tail.sample.SampleTimeSeries;

import org.junit.Before;
import org.junit.Test;

public class AverageGainIndicatorTest {

	private TimeSeries data;

	@Before
	public void prepare() throws Exception {
		data = new SampleTimeSeries(new double[] { 1, 2, 3, 4, 3, 4, 5, 4, 3, 3, 4, 3, 2 });
	}

	@Test
	public void testAverageGainUsingTimeFrame5UsingClosePrice() throws Exception {
		AverageGainIndicator averageGain = new AverageGainIndicator(new ClosePriceIndicator(data), 5);

		assertEquals(4d / 5d, averageGain.getValue(5), 0.01);
		assertEquals(4d / 5d, averageGain.getValue(6), 0.01);
		assertEquals(3d / 5d, averageGain.getValue(7), 0.01);
		assertEquals(2d / 5d, averageGain.getValue(8), 0.01);
		assertEquals(2d / 5d, averageGain.getValue(9), 0.01);
		assertEquals(2d / 5d, averageGain.getValue(10), 0.01);
		assertEquals(1d / 5d, averageGain.getValue(11), 0.01);
		assertEquals(1d / 5d, averageGain.getValue(12), 0.01);
	}

	@Test
	public void testAverageGainShouldWorkJumpingIndexes() {
		AverageGainIndicator averageGain = new AverageGainIndicator(new ClosePriceIndicator(data), 5);
		assertEquals(2d / 5d, averageGain.getValue(10), 0.01);
		assertEquals(1d / 5d, averageGain.getValue(12), 0.01);
	}

	@Test
	public void testAverageGainMustReturnZeroWhenTheDataDoesntGain() {
		AverageGainIndicator averageGain = new AverageGainIndicator(new ClosePriceIndicator(data), 3);
		assertEquals(0, averageGain.getValue(9), 0.01);
	}

	@Test
	public void testAverageGainWhenTimeFrameIsGreaterThanIndicatorDataShouldBeCalculatedWithDataSize() {
		AverageGainIndicator averageGain = new AverageGainIndicator(new ClosePriceIndicator(data), 1000);
		assertEquals(6d / data.getSize(), averageGain.getValue(12), 0.01);
	}

	@Test
	public void testAverageGainWhenIndexIsZeroMustBeZero() {
		AverageGainIndicator averageGain = new AverageGainIndicator(new ClosePriceIndicator(data), 10);
		assertEquals(0, averageGain.getValue(0), 0.01);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testIndexGreatterThanTheIndicatorLenghtShouldThrowException() {
		AverageGainIndicator averageGain = new AverageGainIndicator(new ClosePriceIndicator(data), 5);
		assertEquals(3d, averageGain.getValue(300));
	}

	@Test
	public void testGetName() {
		AverageGainIndicator averageGain = new AverageGainIndicator(new ClosePriceIndicator(data), 5);
		assertEquals("AverageGainIndicator timeFrame: 5", averageGain.getName());
	}

}
