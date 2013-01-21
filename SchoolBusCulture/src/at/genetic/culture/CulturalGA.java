package at.genetic.culture;

import java.util.ArrayList;
import java.util.List;

import jenes.GeneticAlgorithm;
import jenes.chromosome.Chromosome;
import jenes.population.Fitness;
import jenes.population.Population;
import jenes.stage.AbstractStage;
import jenes.stage.operator.common.OnePointCrossover;
import jenes.stage.operator.common.RouletteWheelSelector;
import jenes.stage.operator.common.SimpleMutator;

public class CulturalGA<T extends Chromosome<?>> extends GeneticAlgorithm<T> {

	public List<T> beliefspace;

	public CulturalGA(final Fitness<?> fitness, final Population<T> pop,
			boolean withGuru, final int genlimit, double mutationProp,
			double crossoverProp) {
		super(fitness, pop, genlimit);

		beliefspace = new ArrayList<T>();

		AbstractStage<T> selection = new RouletteWheelSelector<T>();
		AbstractStage<T> crossover = null;
		AbstractStage<T> beliefup = null;
		if (withGuru) {
			beliefup = new BeliefspaceUpdate<T>(this);
			crossover = new GuruCrossover<T>(crossoverProp,
					this);
		} else {
			crossover = new OnePointCrossover<T>(crossoverProp);
		}
		AbstractStage<T> mutation = new SimpleMutator<T>(mutationProp);
		this.addStage(selection);
		if (withGuru) this.addStage(beliefup);
		this.addStage(crossover);
		this.addStage(mutation);

		this.setElitism(1);
	}
}
