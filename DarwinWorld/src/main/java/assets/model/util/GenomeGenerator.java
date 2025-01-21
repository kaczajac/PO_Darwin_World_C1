package assets.model.util;

import assets.model.mapelement.Animal;
import assets.model.records.SimulationConfig;

import java.util.ArrayList;

public class GenomeGenerator {

    public static int[] getRandomGenes(int animalGenomeLength) {
        if (animalGenomeLength == 0) {
            return new int[0];
        }

        int[] newGenome = new int[animalGenomeLength];
        for(int i = 0; i < animalGenomeLength; i++){
            newGenome[i] = (int) (Math.random() * 8);
        }
        return newGenome;
    }

    public static int[] getInheritedGenes(Animal parent1, Animal parent2) {
        int animalGenomeLength = parent1.getGenome().length; // parent1.genome.length == parent2.genome.length
        int energy1 = parent1.getEnergy();
        int energy2 = parent2.getEnergy();
        int maxEnergy = energy1 + energy2;

        // Determine the mid-point based on the energy levels
        int midPoint = (int) Math.floor(animalGenomeLength * (Math.max(energy1, energy2) / (double) maxEnergy));

        // Choose which gene set to use for the first and second part of the genome
        int[] firstSet = (energy1 > energy2) ? parent1.getGenome() : parent2.getGenome();
        int[] secondSet = (energy1 > energy2) ? parent2.getGenome() : parent1.getGenome();

        // Create the new genome by combining both gene sets
        int[] newGenome = new int[animalGenomeLength];
        System.arraycopy(firstSet, 0, newGenome, 0, midPoint);
        System.arraycopy(secondSet, midPoint, newGenome, midPoint, animalGenomeLength - midPoint);

        return newGenome;
    }

    public static void mutateGenes(SimulationConfig config, int[] newGenome) {

        int mutations = config.animalMinMutations() + (int)(Math.random() * (config.animalMaxMutations() - config.animalMinMutations()));
        while (mutations > 0) {
            if((int)(Math.random() * 2) == 1)
                geneShuffleMutation(newGenome);
            else geneShiftMutation(newGenome);
            mutations--;
        }

    }

    public static void geneShiftMutation(int[] genome){
        int shift = 1;
        if((int)(Math.random()*2) == 1) shift *= -1;
        int geneID = (int)(Math.random() * genome.length);
        genome[geneID] = (genome[geneID] + shift) % 8;
        if(genome[geneID] == -1) genome[geneID] = 7;
    }

    public static void geneShuffleMutation(int[] genome) {
        int geneID = (int)(Math.random() * genome.length);
        int[] possibleGenes = {0, 1, 2, 3, 4, 5, 6, 7};
        ArrayList<Integer> genePool = new ArrayList<>();
        for (int gene : possibleGenes) {
            if (gene != genome[geneID]) {
                genePool.add(gene);
            }
        }
        genome[geneID] = genePool.get((int)(Math.random() * genePool.size()));
    }

}
