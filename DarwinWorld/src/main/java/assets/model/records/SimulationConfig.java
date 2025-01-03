package assets.model.records;

public record SimulationConfig(MapSettings mapSettings,
                               int mapFlowsDuration,
                               int grassDaily,
                               int grassEnergy,
                               int animalStartNumber,
                               int animalStartEnergy,
                               int animalGenomeLength,
                               int animalMinFedEnergy,
                               int animalBirthCost) {
}
