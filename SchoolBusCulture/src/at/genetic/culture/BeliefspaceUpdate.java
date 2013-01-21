package at.genetic.culture;

import jenes.chromosome.Chromosome;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.stage.AbstractStage;
import jenes.stage.StageException;

public class BeliefspaceUpdate<T extends Chromosome<?>> extends AbstractStage<T> {


	private CulturalGA<T> ga;
	
	public BeliefspaceUpdate (CulturalGA<T> ga) {
        this.ga = ga;
	}
	
	@Override
	public void process(Population<T> in, Population<T> out) throws StageException {
        out.setAs(in);
		in.sort(true);
		ga.beliefspace.clear();

        for (Individual<T> individual : in) {
			if (Population.BEST.pass(individual)) {
	    		ga.beliefspace.add(individual.getChromosome());
			}
		}
	}

}
