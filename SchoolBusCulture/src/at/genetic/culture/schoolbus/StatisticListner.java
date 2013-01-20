package at.genetic.culture.schoolbus;

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

	public StatisticListner() {

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

}
