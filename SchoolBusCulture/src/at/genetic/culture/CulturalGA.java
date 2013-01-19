package at.genetic.culture;

import jenes.GeneticAlgorithm;
import jenes.chromosome.Chromosome;
import jenes.population.Fitness;
import jenes.population.Population;
import jenes.stage.AbstractStage;
import jenes.stage.operator.common.OnePointCrossover;
import jenes.stage.operator.common.SimpleMutator;
import jenes.stage.operator.common.TournamentSelector;

public class CulturalGA<T extends Chromosome<?>> extends GeneticAlgorithm<T> {

	public CulturalGA(final Fitness<?> fitness, final Population<T> pop,
			final int genlimit, double mutationProp, double crossoverProp) {
		super(fitness, pop, genlimit);

		AbstractStage<T> selection = new TournamentSelector<T>(3);
		AbstractStage<T> crossover = new OnePointCrossover<T>(crossoverProp);
		AbstractStage<T> mutation = new SimpleMutator<T>(mutationProp);
		this.addStage(selection);
		this.addStage(crossover);
		this.addStage(mutation);

		this.setElitism(1);
	}
}
