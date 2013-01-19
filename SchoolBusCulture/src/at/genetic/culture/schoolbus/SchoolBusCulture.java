package at.genetic.culture.schoolbus;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;
import com.sanityinc.jargs.CmdLineParser.OptionException;

import jenes.GeneticAlgorithm;
import jenes.chromosome.PermutationChromosome;
import jenes.population.Fitness;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.population.Population.Statistics.Group;
import jenes.stage.AbstractStage;
import jenes.stage.operator.common.OnePointCrossover;
import jenes.stage.operator.common.SimpleMutator;
import jenes.stage.operator.common.TournamentSelector;
import jenes.tutorials.utils.Utils;
import at.genetic.culture.CulturalGA;

public class SchoolBusCulture {

	private SchoolArea area;
	private CulturalGA<PermutationChromosome> ga;

	/**
	 * @param args
	 * @throws OptionException
	 * @throws IOException
	 */
	public static void main(String[] args) throws OptionException, IOException {

		CmdLineParser cmd = new CmdLineParser();
		Option<String> areaMapOption = cmd.addStringOption('m', "map");
		cmd.parse(args);

		String areaMapPath = cmd.getOptionValue(areaMapOption);

		SchoolArea areaMap = loadMapFromPath(areaMapPath);

		SchoolBusCulture culture = new SchoolBusCulture(areaMap);
		culture.run();
		culture.printStats();
	}

	public SchoolBusCulture(SchoolArea area) {
		this.area = area;

		PermutationChromosome sampleChrom = new PermutationChromosome(
				area.countStops());
		Individual<PermutationChromosome> sample = new Individual<PermutationChromosome>(
				sampleChrom);
		Population<PermutationChromosome> pop = new Population<PermutationChromosome>(
				sample, 10);

		Fitness<PermutationChromosome> fit = new ScheduleFitness(area);

		ga = new CulturalGA<PermutationChromosome>(fit, pop, 1);

		AbstractStage<PermutationChromosome> selection = new TournamentSelector<PermutationChromosome>(
				3);
		AbstractStage<PermutationChromosome> crossover = new OnePointCrossover<PermutationChromosome>(
				0.8);
		AbstractStage<PermutationChromosome> mutation = new SimpleMutator<PermutationChromosome>(
				0.2);
		ga.addStage(selection);
		ga.addStage(crossover);
		ga.addStage(mutation);

		ga.setElitism(1);
	}

	private void run() {
		ga.evolve();
	}
	
	private void printStats() {
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
	
	private void openGui() {
		JFrame frame = new JFrame("School Bus Culture");

		JPanel p = new JPanel();
		BoxLayout layout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(layout);

		// Add some drawing panel and display the map
		// add another drawing panel and print live stats
		// we need threads for this

		frame.add(p, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private static SchoolArea loadMapFromPath(String areaPath)
			throws IOException {
		File file = new File(areaPath);
		FileReader reader = new FileReader(file);
		BufferedReader breader = new BufferedReader(reader);
		String line;

		line = breader.readLine();
		String[] pos = line.split(",");
		int x = new Integer(pos[0].trim());
		int y = new Integer(pos[1].trim());
		BusStop school = new BusStop(x, y, 0);

		// BusStop[] busStops = new BusStop[10];

		// ArrayList<BusStop> busStops = new ArrayList<BusStop>();
		ArrayList<BusStop> busStops = new ArrayList<BusStop>();

		while ((line = breader.readLine()) != null) {
			pos = line.split(",");
			x = new Integer(pos[0].trim());
			y = new Integer(pos[1].trim());
			int kids = new Integer(pos[2].trim());
			busStops.add(new BusStop(x, y, kids));
		}
		breader.close();

		return new SchoolArea(school, busStops.toArray(new BusStop[busStops
				.size()]), 60);
	}
}
