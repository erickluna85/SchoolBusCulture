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

import jenes.GeneticAlgorithm;
import jenes.chromosome.PermutationChromosome;
import jenes.population.Fitness;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.population.Population.Statistics.Group;
import jenes.tutorials.utils.Utils;
import at.genetic.culture.CulturalGA;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;
import com.sanityinc.jargs.CmdLineParser.OptionException;

public class SchoolBusCulture {

	private SchoolArea area;
	private CulturalGA<PermutationChromosome> ga;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		CmdLineParser cmd = new CmdLineParser();
		Option<String> areaMapOption = cmd.addStringOption('a', "area");
		Option<Integer> numGenOption = cmd.addIntegerOption('n', "numgen");
		Option<Integer> populationSizeOption = cmd.addIntegerOption('p', "pop");
		Option<Double> mutationPropOption = cmd
				.addDoubleOption('m', "mutation");
		Option<Double> crossoverPropOption = cmd.addDoubleOption('c',
				"crossover");
		Option<Double> costPerBusOption = cmd.addDoubleOption('b', "buscost");
		Option<Double> costPerKmOption = cmd.addDoubleOption('k', "kmcost");
		Option<Boolean> helpOption = cmd.addBooleanOption('h', "help");
		cmd.parse(args);

		boolean help = cmd.getOptionValue(helpOption, false);
		
		if (help) {
			printHelp();
			return;
		}
		
		String areaMapPath = cmd.getOptionValue(areaMapOption);
		int numGen = cmd.getOptionValue(numGenOption, 100);
		int popSize = cmd.getOptionValue(populationSizeOption, 10);
		double mutationProp = cmd.getOptionValue(mutationPropOption, 0.02);
		double crossoverProp = cmd.getOptionValue(crossoverPropOption, 0.8);
		Double costPerBus = cmd.getOptionValue(costPerBusOption);
		Double costPerKm = cmd.getOptionValue(costPerKmOption);

		SchoolArea areaMap = loadMapFromPath(areaMapPath);

		if (costPerBus != null) {
			areaMap.setCostPerBus(costPerBus);
		}

		if (costPerKm != null) {
			areaMap.setCostPerKm(costPerKm);
		}

		SchoolBusCulture culture = new SchoolBusCulture(areaMap, numGen,
				popSize, mutationProp, crossoverProp);
		culture.run();
	}

	private static void printHelp() {
		System.out.println("School Bus Culture");
		System.out.println();
		System.out.println("Possible Options are:");
		System.out.println();
		System.out.println("Problem specific options:");
		System.out.println("	-a --area	Spezify an file with are setup [mandatory]");
		System.out.println("	-b --buscost	Cost per bus (1000)");
		System.out.println("	-k --kmcost	Cost per km (0.7)");
		System.out.println();
		System.out.println("Genetic algorithm options:");
		System.out.println("	-n --numgen	Number of generations (100)");
		System.out.println("	-p --pop	Size of population (10)");
		System.out.println("	-m --mutation	Probability for mutation (0.02)");
		System.out.println("	-c --crossover	Probability for crossover (0.8)");
		System.out.println();
		System.out.println("General options:");
		System.out.println("	-h --help	This help text");

	}

	public SchoolBusCulture(SchoolArea area, int numGen, int popSize, double mutationProp, double crossoverProp) {
		this.area = area;

		PermutationChromosome sampleChrom = new PermutationChromosome(
				area.countStops());
		Individual<PermutationChromosome> sample = new Individual<PermutationChromosome>(
				sampleChrom);
		Population<PermutationChromosome> pop = new Population<PermutationChromosome>(
				sample, popSize);
		Fitness<PermutationChromosome> fit = new ScheduleFitness(area);

		ga = new CulturalGA<PermutationChromosome>(fit, pop, numGen, mutationProp, crossoverProp);
		
		StatisticListner listener = new StatisticListner(area);
		ga.addGenerationEventListener(listener);
		ga.addAlgorithmEventListener(listener);
	}

	private void run() {
		ga.evolve();
	}

	private static SchoolArea loadMapFromPath(String areaPath) throws Exception {
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
