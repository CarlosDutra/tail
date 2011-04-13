package net.sf.tail.report;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.tail.AnalysisCriterion;
import net.sf.tail.Operation;
import net.sf.tail.OperationType;
import net.sf.tail.TimeSeriesSlicer;
import net.sf.tail.Trade;
import net.sf.tail.analysis.criteria.NumberOfTicksCriterion;
import net.sf.tail.analysis.criteria.TotalProfitCriterion;
import net.sf.tail.analysis.evaluator.Decision;
import net.sf.tail.report.html.ReportHTMLGenerator;
import net.sf.tail.runner.HistoryRunner;
import net.sf.tail.sample.SampleTimeSeries;
import net.sf.tail.series.FullyMemorizedSlicer;
import net.sf.tail.series.RegularSlicer;
import net.sf.tail.strategy.FakeStrategy;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

public class CompleteReportGeneratorTest {

	private List<Decision> decisions;

	private TimeSeriesSlicer slicer;

	private List<AnalysisCriterion> criteria;

	private List<String> urls;

	@Before
	public void setUp() throws Exception {
		SampleTimeSeries series = new SampleTimeSeries();
		slicer = new RegularSlicer(series,new Period().withYears(2000));
		
		List<Trade> trades = new ArrayList<Trade>();
		trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(2, OperationType.SELL)));
		trades.add(new Trade(new Operation(3, OperationType.BUY), new Operation(5, OperationType.SELL)));

		decisions = new ArrayList<Decision>();
		
		TimeSeriesSlicer slicer = new RegularSlicer(series,new Period().withYears(2000));
		
		Decision decision = new Decision(new FakeStrategy(new Operation[0], new Operation[0]), slicer,0,
				new TotalProfitCriterion(), trades, new HistoryRunner(slicer,new FakeStrategy(new Operation[0], new Operation[0])));
		decisions.add(decision);
		slicer = new FullyMemorizedSlicer(series, new Period().withYears(1));

		criteria = new ArrayList<AnalysisCriterion>();
		criteria.add(new NumberOfTicksCriterion());
		criteria.add(new TotalProfitCriterion());

		urls = new ArrayList<String>();
		urls.add("report.html");
	}

	@Test
	public void testSMAGenerate() throws IOException {
		Report report = new Report(null ,new TotalProfitCriterion(), slicer, decisions);

		StringBuffer html = new ReportHTMLGenerator().generate(report, criteria, "", urls);

		assertTrue(html.toString().contains("NumberOfTicks"));
		assertTrue(html.toString().contains("4"));
		assertTrue(html.toString().contains("report.html"));
	}

	@Test
	public void testSMAGenerateWithoutCriteria() throws IOException {
		Report report = new Report(null,new TotalProfitCriterion(), slicer, decisions);

		StringBuffer html = new ReportHTMLGenerator().generate(report, "", urls);

		assertTrue(html.toString().contains("TotalProfitCriterion"));
		assertTrue(html.toString().contains("report.html"));
	}
}
