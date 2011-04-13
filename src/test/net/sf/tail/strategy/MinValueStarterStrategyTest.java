package net.sf.tail.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.tail.Operation;
import net.sf.tail.OperationType;
import net.sf.tail.Strategy;
import net.sf.tail.Trade;
import net.sf.tail.sample.SampleIndicator;

import org.junit.Before;
import org.junit.Test;

public class MinValueStarterStrategyTest {

	private SampleIndicator indicator;

	private int startValue;

	private Operation[] enter;

	private Operation[] exit;

	private Strategy alwaysBuy;

	private Strategy starter;

	@Before
	public void setUp() throws Exception {
		indicator = new SampleIndicator(new double[] { 90, 92, 96, 95, 92 });
		startValue = 93;
		enter = new Operation[] { new Operation(0, OperationType.BUY), new Operation(1, OperationType.BUY),
				new Operation(2, OperationType.BUY), new Operation(3, OperationType.BUY),
				new Operation(4, OperationType.BUY) };
		exit = new Operation[] { null, null, null, null, null };
		alwaysBuy = new FakeStrategy(enter, exit);
		starter = new MinValueStarterStrategy(indicator, alwaysBuy, startValue);
	}

	@Test
	public void testStrategyShouldBuy() {
		Trade trade = new Trade();

		Operation buy = new Operation(2, OperationType.BUY);
		assertTrue(starter.shouldOperate(trade, 2));
		trade.operate(2);
		assertEquals(buy, trade.getEntry());

		trade = new Trade();
		buy = new Operation(3, OperationType.BUY);

		assertTrue(starter.shouldOperate(trade, 3));
		trade.operate(3);

		assertFalse(starter.shouldOperate(trade, 3));
		assertEquals(buy, trade.getEntry());
	}

	@Test
	public void testStrategyShouldNotBuyEvenIfFakeIsSayingTo() {
		Trade trade = new Trade();
		assertFalse(starter.shouldOperate(trade, 0));
		assertFalse(starter.shouldOperate(trade, 1));
		assertFalse(starter.shouldOperate(trade, 4));
	}
}
