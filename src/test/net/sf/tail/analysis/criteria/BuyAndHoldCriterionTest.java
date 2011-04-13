package net.sf.tail.analysis.criteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sf.tail.AnalysisCriterion;
import net.sf.tail.Operation;
import net.sf.tail.OperationType;
import net.sf.tail.TimeSeriesSlicer;
import net.sf.tail.Trade;
import net.sf.tail.analysis.evaluator.Decision;
import net.sf.tail.analysis.evaluator.DummyDecision;
import net.sf.tail.sample.SampleTimeSeries;
import net.sf.tail.series.RegularSlicer;

import org.joda.time.Period;
import org.junit.Test;

public class BuyAndHoldCriterionTest {

	@Test
	public void testCalculateOnlyWithGainTrades() {
		SampleTimeSeries series = new SampleTimeSeries(new double[] { 100, 105, 110, 100, 95, 105 });
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(2, OperationType.SELL)));
		trades.add(new Trade(new Operation(3, OperationType.BUY), new Operation(5, OperationType.SELL)));

		AnalysisCriterion buyAndHold = new BuyAndHoldCriterion();
		assertEquals(1.05, buyAndHold.calculate(series, trades));
	}

	@Test
	public void testSummarize() {
		SampleTimeSeries series = new SampleTimeSeries(new double[] { 100, 105, 110, 100, 95, 105 });
		TimeSeriesSlicer slicer = new RegularSlicer(series, new Period().withYears(2000));
		List<Decision> decisions = new LinkedList<Decision>();

		List<Trade> tradesToDummy1 = new LinkedList<Trade>();
		tradesToDummy1.add(new Trade(new Operation(0, OperationType.BUY), new Operation(2, OperationType.SELL)));
		Decision dummy1 = new DummyDecision(tradesToDummy1, slicer);
		decisions.add(dummy1);

		List<Trade> tradesToDummy2 = new LinkedList<Trade>();
		tradesToDummy2.add(new Trade(new Operation(3, OperationType.BUY), new Operation(5, OperationType.SELL)));
		Decision dummy2 = new DummyDecision(tradesToDummy2, slicer);
		decisions.add(dummy2);

		AnalysisCriterion buyAndHold = new BuyAndHoldCriterion();
		assertEquals(1.05, buyAndHold.summarize(series, decisions), 0.01);
	}

	@Test
	public void testCalculateOnlyWithLossTrades() {
		SampleTimeSeries series = new SampleTimeSeries(new double[] { 100, 95, 100, 80, 85, 70 });
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
		trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(5, OperationType.SELL)));

		AnalysisCriterion buyAndHold = new BuyAndHoldCriterion();
		assertEquals(0.7, buyAndHold.calculate(series, trades));
	}

	@Test
	public void testCalculateWithNoTrades() {
		SampleTimeSeries series = new SampleTimeSeries(new double[] { 100, 95, 100, 80, 85, 70 });
		List<Trade> trades = new ArrayList<Trade>();

		AnalysisCriterion buyAndHold = new BuyAndHoldCriterion();
		assertEquals(0.7, buyAndHold.calculate(series, trades));
	}
	
	@Test
	public void testCalculateWithOneTrade()
	{
		SampleTimeSeries series = new SampleTimeSeries(new double[] {100, 105 });
		Trade trade = new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL));
		AnalysisCriterion buyAndHold = new BuyAndHoldCriterion();
		assertEquals(105d/100, buyAndHold.calculate(series, trade));
		
	}
	
	@Test
	public void testEquals()
	{
		BuyAndHoldCriterion criterion = new BuyAndHoldCriterion();
		assertTrue(criterion.equals(criterion));
		assertTrue(criterion.equals(new BuyAndHoldCriterion()));
		assertFalse(criterion.equals(new TotalProfitCriterion()));
		assertFalse(criterion.equals(5d));
		assertFalse(criterion.equals(null));
	}

}
