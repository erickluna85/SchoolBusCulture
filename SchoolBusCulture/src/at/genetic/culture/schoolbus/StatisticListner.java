package at.genetic.culture.schoolbus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.beans.Transient;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.MinMaxCategoryRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import jenes.AlgorithmEventListener;
import jenes.GenerationEventListener;
import jenes.GeneticAlgorithm;
import jenes.chromosome.Chromosome;
import jenes.chromosome.PermutationChromosome;
import jenes.population.Fitness;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.population.Population.Statistics;
import jenes.population.Population.Statistics.Group;
import jenes.tutorials.utils.Utils;

public class StatisticListner<T extends Chromosome> implements
		GenerationEventListener<T>, AlgorithmEventListener<T> {

	private SchoolArea area;

	public StatisticListner(SchoolArea area) {
		this.area = area;
		openGui();
	}

	private void openGui() {
		JFrame frame = new JFrame("School Bus Culture – Map");
		JPanel p = new JPanel();
		// frame.setLayout(new GridLayout());
		BoxLayout layout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(layout);

		// Add some drawing panel and display the area map
		// add another drawing panel and print live stats
		// we need threads for this

		p.add(new MapPanel(area));

		frame.add(p, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		

		JFrame frame2 = new JFrame("School Bus Culture – Stats");
		JPanel p2 = new JPanel();
		// frame.setLayout(new GridLayout());
		BoxLayout layout2 = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(layout2);

		// Add some drawing panel and display the area map
		// add another drawing panel and print live stats
		// we need threads for this

		p2.add(getStatPanel());

		frame2.add(p2, BorderLayout.CENTER);

		frame2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame2.pack();
		frame2.setVisible(true);
	}

	@Override
	public void onAlgorithmStart(GeneticAlgorithm<T> ga, long time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAlgorithmStop(GeneticAlgorithm<T> ga, long time) {

		Population.Statistics stats = ga.getCurrentPopulation().getStatistics();
		GeneticAlgorithm.Statistics algostats = ga.getStatistics();

		Fitness<PermutationChromosome> fit = ga.getFitness();
		System.out.println("Objective: "
				+ (fit.getBiggerIsBetter()[0] ? "Max! (All true)"
						: "Min! (None true)"));
		System.out.println(fit.getBiggerIsBetter().length);

		Group legals = stats.getGroup(Population.LEGALS);

		Individual solution = legals.get(0);

		System.out.println("Solution: ");
		System.out.println(solution.toCompleteString());
		System.out.format("found in %d ms.\n", algostats.getExecutionTime());
		System.out.println();

		Utils.printStatistics(stats);
	}

	@Override
	public void onAlgorithmInit(GeneticAlgorithm<T> ga, long time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGeneration(GeneticAlgorithm<T> ga, long time) {

		Statistics stat = ga.getCurrentPopulation().getStatistics();
		Group legals = stat.getGroup(Population.LEGALS);
		System.out.println("Current generation: " + ga.getGeneration());
		System.out.println("\tBest score: " + legals.getMin()[0]);
		System.out.println("\tAvg score : " + legals.getMean()[0]);
	}

	public class MapPanel extends JPanel {

		private SchoolArea area;
		private int w = 800;
		private int h = 800;

		public MapPanel(SchoolArea area) {
			this.area = area;
		}

		@Override
		@Transient
		public Dimension getPreferredSize() {
			// TODO Auto-generated method stub
			return new Dimension(w + 40, h + 40);
			// return super.getPreferredSize();
		}

		public void paint(Graphics g) {

			BusStop[] stops = area.getAllStops();

			int[] max = getMax(stops);

			System.out.println("Differenz der Punkte: " + max[0] + ", shift: "
					+ max[1] + ", " + max[2]);

			Graphics2D g2 = (Graphics2D) g;

			Point p;
			g2.setPaint(Color.BLUE);
			for (int i = 1; i < stops.length; i++) {
				p = project(stops[i], max[0], max[1], max[2]);
				int size = stops[i].numberOfPupils;
				g2.fillOval(p.x, p.y, size, size);
			}

			p = project(stops[0], max[0], max[1], max[2]);
			g2.setPaint(Color.RED);
			g2.fillOval(p.x, p.y, 10, 10);

		}

		private int[] getMax(BusStop[] stops) {
			int maxX = 0, minX = 0, maxY = 0, minY = 0;
			for (BusStop busStop : stops) {
				if (busStop.x > maxX)
					maxX = busStop.x;
				else if (busStop.x < minX)
					minX = busStop.x;
				if (busStop.y > maxY)
					maxY = busStop.y;
				else if (busStop.y < minY)
					minY = busStop.y;
			}
			int min = (minX < minY ? minX : minY);
			int max = (maxX > maxY ? maxX : maxY);
			int diff = max - min;

			int xoffset = minX + (maxX - minX) / 2;
			int yoffset = minY + (maxY - minY) / 2;

			return new int[] { diff, xoffset, yoffset };
		}

		private Point project(BusStop stop, int diff, int xoffset, int yoffset) {
			double scale = (double) w / (double) diff;
			double shift = ((double) w / 2.0) + 20;

			int x = (int) (((double) stop.x - (double) xoffset) * scale + shift);
			int y = (int) (((double) stop.y - (double) yoffset) * scale + shift);

			Point p = new Point(x, y);
			return p;
		}
	}

	public ChartPanel getStatPanel() {

		// create a dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(1.0, "First", "Category 1");
		dataset.addValue(4.0, "First", "Category 2");
		dataset.addValue(3.0, "First", "Category 3");
		dataset.addValue(5.0, "First", "Category 4");
		dataset.addValue(5.0, "First", "Category 5");
		dataset.addValue(7.0, "First", "Category 6");
		dataset.addValue(7.0, "First", "Category 7");
		dataset.addValue(8.0, "First", "Category 8");
		dataset.addValue(5.0, "Second", "Category 1");
		dataset.addValue(7.0, "Second", "Category 2");
		dataset.addValue(6.0, "Second", "Category 3");
		dataset.addValue(8.0, "Second", "Category 4");
		dataset.addValue(4.0, "Second", "Category 5");
		dataset.addValue(4.0, "Second", "Category 6");
		dataset.addValue(2.0, "Second", "Category 7");
		dataset.addValue(1.0, "Second", "Category 8");
		dataset.addValue(4.0, "Third", "Category 1");
		dataset.addValue(3.0, "Third", "Category 2");
		dataset.addValue(2.0, "Third", "Category 3");
		dataset.addValue(3.0, "Third", "Category 4");
		dataset.addValue(6.0, "Third", "Category 5");
		dataset.addValue(3.0, "Third", "Category 6");
		dataset.addValue(4.0, "Third", "Category 7");
		dataset.addValue(3.0, "Third", "Category 8");

		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(
				"Min/Max Category Plot", // chart title
				"Category", // domain axis label
				"Value", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.yellow);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setRenderer(new MinMaxCategoryRenderer());
		// OPTIONAL CUSTOMISATION COMPLETED.

		// add the chart to a panel...
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		return chartPanel;
	}
}
