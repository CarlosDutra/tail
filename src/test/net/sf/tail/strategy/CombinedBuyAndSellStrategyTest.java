package net.sf.tail.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.tail.Indicator;
import net.sf.tail.Operation;
import net.sf.tail.OperationType;
import net.sf.tail.Strategy;
import net.sf.tail.sample.SampleIndicator;

import org.junit.Test;

public class CombinedBuyAndSellStrategyTest {

	private Operation[] enter;

	private Operation[] exit;

	private FakeStrategy buyStrategy;

	private FakeStrategy sellStrategy;

	private CombinedBuyAndSellStrategy combined;

	@Test
	public void testeShoudEnter() {

		enter = new Operation[] { new Operation(0, OperationType.BUY), null, new Operation(2, OperationType.BUY), null,
				new Operation(4, OperationType.BUY) };
		exit = new Operation[] { null, null, null, null, null };

		buyStrategy = new FakeStrategy(enter, null);
		sellStrategy = new FakeStrategy(null, exit);

		combined = new CombinedBuyAndSellStrategy(buyStrategy, sellStrategy);

		assertTrue(combined.shouldEnter(0));
		assertFalse(combined.shouldEnter(1));
		assertTrue(combined.shouldEnter(2));
		assertFalse(combined.shouldEnter(3));
		assertTrue(combined.shouldEnter(4));

		assertFalse(combined.shouldExit(0));
		assertFalse(combined.shouldExit(1));
		assertFalse(combined.shouldExit(2));
		assertFalse(combined.shouldExit(3));
		assertFalse(combined.shouldExit(4));

	}

	@Test
	public void testeShoudExit() {

		exit = new Operation[] { new Operation(0, OperationType.SELL), null, new Operation(2, OperationType.SELL),
				null, new Operation(4, OperationType.SELL) };

		enter = new Operation[] { null, null, null, null, null };

		buyStrategy = new FakeStrategy(enter, null);
		sellStrategy = new FakeStrategy(null, exit);

		combined = new CombinedBuyAndSellStrategy(buyStrategy, sellStrategy);

		assertTrue(combined.shouldExit(0));
		assertFalse(combined.shouldExit(1));
		assertTrue(combined.shouldExit(2));
		assertFalse(combined.shouldExit(3));
		assertTrue(combined.shouldExit(4));

		assertFalse(combined.shouldEnter(0));
		assertFalse(combined.shouldEnter(1));
		assertFalse(combined.shouldEnter(2));
		assertFalse(combined.shouldEnter(3));
		assertFalse(combined.shouldEnter(4));
	}

	@Test
	public void testWhenBuyStrategyAndSellStrategyAreEquals() {
		Indicator<Double> first = new SampleIndicator(new double[] { 4, 7, 9, 6, 3, 2 });
		Indicator<Double> second = new SampleIndicator(new double[] { 3, 6, 10, 8, 2, 1 });

		Strategy crossed = new IndicatorCrossedIndicatorStrategy(first, second);

		combined = new CombinedBuyAndSellStrategy(crossed, crossed);

		for (int index = 0; index < 6; index++) {
			assertEquals(crossed.shouldEnter(index), combined.shouldEnter(index));
			assertEquals(crossed.shouldExit(index), combined.shouldExit(index));
		}
	}
}
