package at.univie.test1;

import jenes.GeneticAlgorithm;
import jenes.chromosome.BooleanChromosome;
import jenes.population.Fitness;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.population.Population.Statistics.Group;
import jenes.stage.AbstractStage;
import jenes.stage.operator.common.OnePointCrossover;
import jenes.stage.operator.common.SimpleMutator;
import jenes.stage.operator.common.TournamentSelector;
import jenes.tutorials.utils.Utils;

public class FirstTest {

	private static int POPULATION_SIZE = 50;
	private static int CHROMOSOME_LENGTH = 500;
	private static int GENERATION_LIMIT = 10000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Individual<BooleanChromosome> sample = new Individual<BooleanChromosome>(
				new BooleanChromosome(CHROMOSOME_LENGTH));
		Population<BooleanChromosome> pop = new Population<BooleanChromosome>(
				sample, POPULATION_SIZE);

		Fitness<BooleanChromosome> fit = new Fitness<BooleanChromosome>(false) {
			@Override
			public void evaluate(Individual<BooleanChromosome> individual) {
				BooleanChromosome chrom = individual.getChromosome();
				int count = 0;
				int length = chrom.length();
				for (int i = 0; i < length; i++)
					if (chrom.getValue(i))
						count++;

				individual.setScore(count);
			}

		};
		GeneticAlgorithm<BooleanChromosome> ga = new GeneticAlgorithm<BooleanChromosome>(
				fit, pop, GENERATION_LIMIT);

		 AbstractStage<BooleanChromosome> selection = new TournamentSelector<BooleanChromosome>(3);
		 AbstractStage<BooleanChromosome> crossover = new OnePointCrossover<BooleanChromosome>(0.8);
		 AbstractStage<BooleanChromosome> mutation = new SimpleMutator<BooleanChromosome>(0.02);
		 ga.addStage(selection);
		 ga.addStage(crossover);
		 ga.addStage(mutation);
		 
		 ga.setElitism(1);
		 ga.evolve();

		 Population.Statistics stats = ga.getCurrentPopulation().getStatistics();
		 GeneticAlgorithm.Statistics algostats = ga.getStatistics();
		 
		 System.out.println("Objective: " + (fit.getBiggerIsBetter()[0] ? "Max! (All true)" : "Min! (None true)"));
		 System.out.println(fit.getBiggerIsBetter().length);
		 
		 Group legals = stats.getGroup(Population.LEGALS);
		 
		 Individual solution = legals.get(0);
		         
		 System.out.println("Solution: ");
		 System.out.println( solution.toCompleteString() );
		 System.out.format("found in %d ms.\n", algostats.getExecutionTime() );
		 System.out.println();
		 
		 Utils.printStatistics(stats);
	}

}
