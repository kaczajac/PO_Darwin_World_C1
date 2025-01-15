package assets.model.records;

import assets.model.map.AbstractMap;

public record SimulationConfig(AbstractMap map,
                               int mapFlowsDuration,
                               int grassDaily,
                               int grassEnergy,
                               int animalStartNumber,
                               int animalStartEnergy,
                               int animalGenomeLength,
                               int animalMinFedEnergy,
                               int animalBirthCost) {
}
