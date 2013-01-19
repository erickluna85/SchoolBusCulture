package at.genetic.culture;

import jenes.GeneticAlgorithm;
import jenes.chromosome.Chromosome;
import jenes.population.Fitness;
import jenes.population.Population;


public class CulturalGA<T extends Chromosome> extends GeneticAlgorithm<T> {

	public CulturalGA() {
		super();
	}
	
    public CulturalGA(final Fitness fitness, final Population<T> pop, final int genlimit) {
    	super(fitness, pop, genlimit);
    }
}
