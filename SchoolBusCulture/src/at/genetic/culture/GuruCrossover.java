package at.genetic.culture;

import jenes.population.Individual;
import jenes.stage.operator.Crossover;
import jenes.chromosome.Chromosome;

/**
 * A two-points crossover operator. It is performed according to a specified probability.
 * It represents a 2-parents and 2-children crossover.
 * Two cross-points, cp1 and cp2, are randomly chosen and the genes in the range [cp1,cp2] are crossed.
 * <p>
 * @param <T> The class of chromosomes to work with.
 *
 * @version 2.0
 * @since 1.0
 * 
 * @see Individual
 * @see Chromosome
 */
public class GuruCrossover<T extends Chromosome> extends Crossover<T> {
	
	private T chromGuru;
	
    /**
     * Constructs a new two-points crossover with the specified probability
     *
     * @param probability the crossover probability
     */
    public GuruCrossover(double probability, T chromGuru) {
        super(probability);
        this.chromGuru = chromGuru;
    }
    
    /**
     * Returns the number of individuals involved by this crossover operator
     * <p>
     * @return the number of individuals required by crossover
     */
	@Override
	protected void cross(Individual<T> offsprings[]) {

        T chromC1 = offsprings[0].getChromosome();
        T chromC2 = offsprings[1].getChromosome();
        
        int s1 = chromC1.length();
        int s2 = chromC2.length();
        int s3 = chromGuru.length();
        
        int min = (s1 < s2) ? s1 : s2;
        min = (min < s3) ? min : s3;
        
        int pos1 = random.nextInt(0,min);
        int pos2 = random.nextInt(0,min);
        
        if(!(pos1 < pos2)) {
        	int a = pos1;
        	pos1 = pos2;
        	pos2 = a;
        }
        
    	Chromosome guru1 = chromGuru.clone();
    	Chromosome guru2 = chromGuru.clone();
    	
        chromC1.cross(chromC2,pos1,pos2);
    	chromC1.cross(guru1,pos2);
    	chromC2.cross(guru2,pos2);
	}

	@Override
	public int spread() {
		// TODO Auto-generated method stub
		return 2;
	}
}