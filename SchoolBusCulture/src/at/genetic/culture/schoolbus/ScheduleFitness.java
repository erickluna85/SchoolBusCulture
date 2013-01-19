package at.genetic.culture.schoolbus;

import jenes.chromosome.PermutationChromosome;
import jenes.population.Fitness;
import jenes.population.Individual;

public class ScheduleFitness extends Fitness<PermutationChromosome> {

	private SchoolArea area;

	public ScheduleFitness(SchoolArea area) {
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
		//System.out.println("score: kmCosts: " + score[0] + ", busCosts: "
		//		+ score[1] + ", sum = " + (score[0] + score[1]));

		// small score is better
		individual.setScore(score[0] + score[1]);
		//individual.setScore(score);
	}
}