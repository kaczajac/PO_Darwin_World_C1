package assets.model.records;

import assets.model.map.AbstractWorldMap;

public record SimulationConfig(AbstractWorldMap map,
                               int mapFlowsDuration,
                               int grassDaily,
                               int grassEnergy,
                               int animalStartNumber,
                               int animalStartEnergy,
                               int animalGenomeLength,
                               int animalMinFedEnergy,
                               int animalBirthCost,
                               int animalMinMutations,
                               int animalMaxMutations,
                               float animalMutationChance) {
}
