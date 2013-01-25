package at.genetic.culture.schoolbus;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jenes.AlgorithmEventListener;
import jenes.GenerationEventListener;
import jenes.GeneticAlgorithm;
import jenes.chromosome.Chromosome;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.population.Population.Statistics;
import jenes.population.Population.Statistics.Group;
import jenes.tutorials.utils.Utils;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class StatisticListener<T extends Chromosome<?>> implements
		GenerationEventListener<T>, AlgorithmEventListener<T> {

	private SchoolArea area;
	private MapPanel mp;
	private XYSeries min;
	private XYSeries mean;
	private JButton show;

	public StatisticListener(SchoolArea area) {
		this.area = area;
		openGuiStats();
		
		openGuiMap();
	}

	private void openGuiMap() {
		final JFrame frame = new JFrame("School Bus Culture – Map");
		final JPanel p = new JPanel();
		BoxLayout layout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(layout);
		show = new JButton("Show route");
		p.add(show);
		mp = new MapPanel(area);
		show.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mp.paintRoute();
			}
		});

		p.add(mp);

		frame.add(p, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void openGuiStats() {

		JFrame frame2 = new JFrame("School Bus Culture – Stats");
		JPanel p2 = new JPanel();
		BoxLayout layout2 = new BoxLayout(p2, BoxLayout.PAGE_AXIS);
		p2.setLayout(layout2);

		p2.add(getStatPanel());

		frame2.add(p2, BorderLayout.CENTER);

		frame2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame2.pack();
		frame2.setVisible(true);
	}

	@Override
	public void onAlgorithmStart(GeneticAlgorithm<T> ga, long time) {
		show.setEnabled(false);
		show.setText("Calculating route …");
	}

	@Override
	public void onAlgorithmStop(GeneticAlgorithm<T> ga, long time) {

		Population.Statistics<?> stats = ga.getCurrentPopulation()
				.getStatistics();
		GeneticAlgorithm.Statistics algostats = ga.getStatistics();

		Group<?> legals = stats.getGroup(Population.LEGALS);

		Individual<?> solution = legals.get(0);
		Integer[] path = (Integer[]) solution.getChromosome().toArray();
		
		System.out.println("Solution: ");
		System.out.println(solution.toCompleteString());
		System.out.format("found in %d ms.\n", algostats.getExecutionTime());
		System.out.println();

		Utils.printStatistics(stats);
		
		mp.addPath(path);
		show.setText("Show route");
		show.setEnabled(true);

	}

	@Override
	public void onAlgorithmInit(GeneticAlgorithm<T> ga, long time) {
	}

	@Override
	public void onGeneration(GeneticAlgorithm<T> ga, long time) {
		Statistics<?> stat = ga.getCurrentPopulation().getStatistics();
		Group<?> legals = stat.getGroup(Population.LEGALS);
		System.out.println("Generation: " + ga.getGeneration() + "\tbest: "
				+ legals.getMin()[0] + "\tavg: " + legals.getMean()[0]);

		min.add(ga.getGeneration(), legals.getMin()[0]);
		mean.add(ga.getGeneration(), legals.getMean()[0]);
	}

	public ChartPanel getStatPanel() {
		min = new XYSeries("min");
		mean = new XYSeries("mean");

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(min);
		dataset.addSeries(mean);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		NumberAxis xAxis = new NumberAxis("generation");
		NumberAxis yAxis = new NumberAxis("cost");
		yAxis.setAutoRangeIncludesZero(false);
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);

		JFreeChart chart = new JFreeChart(plot);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 700));
		return chartPanel;
	}
}
