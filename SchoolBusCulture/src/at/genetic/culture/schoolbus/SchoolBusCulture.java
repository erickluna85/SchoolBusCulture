package at.genetic.culture.schoolbus;

import jenes.GeneticAlgorithm;
import jenes.chromosome.BooleanChromosome;
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

public class SchoolBusCulture extends CulturalGA<PermutationChromosome> {

	public SchoolBusCulture() {

		BusStop school = new BusStop(0, 0, 0);
		
		BusStop[] busStops = new BusStop[10];
		
		int[] row = new int[] {9,0,8,2,6,1,5,7,4,3};
		int[] row2 = new int[] {60,20,50,10,10,10,55,11,40,5};
		
		for (int i = 0; i < 10; i += 1) {
			BusStop stop = new BusStop(0, (i+1)*50, row2[i]);
			busStops[row[i]] = stop;
		}
		
		SchoolArea area = new SchoolArea(school, busStops, 60);
		

		double[] score = area.getCostsForSchedule(new int[] {0,1,2,3,4,5,6,7,8,9});
		System.out.println("opt1: score: kmCosts: " + score[0] + ", busCosts: " + score[1] + ", sum = " + (score[0] + score[1]));
		score = area.getCostsForSchedule(row);
		System.out.println("opt2: score: kmCosts: " + score[0] + ", busCosts: " + score[1] + ", sum = " + (score[0] + score[1]));
		

		PermutationChromosome sampleChrom = new PermutationChromosome(busStops.length);
		
		Individual<PermutationChromosome> sample = new Individual<PermutationChromosome>(sampleChrom);
		Population<PermutationChromosome> pop = new Population<PermutationChromosome>(
				sample, 10);

		Fitness<PermutationChromosome> fit = new ScheduleFitness(area);
		CulturalGA<PermutationChromosome> ga = new CulturalGA<PermutationChromosome>(fit, pop, 1);

		 AbstractStage<PermutationChromosome> selection = new TournamentSelector<PermutationChromosome>(3);
		 AbstractStage<PermutationChromosome> crossover = new OnePointCrossover<PermutationChromosome>(0.8);
		 AbstractStage<PermutationChromosome> mutation = new SimpleMutator<PermutationChromosome>(0.2);
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
	
	public SchoolBusCulture(Fitness fitness,
			Population<PermutationChromosome> pop, int genlimit) {
		super(fitness, pop, genlimit);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SchoolBusCulture();
	}

	public class ScheduleFitness extends Fitness<PermutationChromosome> {
		
		private SchoolArea area;
		
		public ScheduleFitness (SchoolArea area) {
			super(false);
			this.area = area;
		}
		
		@Override
		public void evaluate(Individual<PermutationChromosome> individual) {
			PermutationChromosome chrom = individual.getChromosome();
			
			int[] schedule = new int[chrom.length()];
			for (int i = 0; i < chrom.length(); i++) {
				schedule[i] = chrom.getElementAt(i);
			}
			
			double[] score = area.getCostsForSchedule(schedule);
			System.out.println("score: kmCosts: " + score[0] + ", busCosts: " + score[1] + ", sum = " + (score[0] + score[1]));
			
			// small score is better
			individual.setScore(score[0] + score[1]);
		}
	}
}
