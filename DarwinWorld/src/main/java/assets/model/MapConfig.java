package assets.model;

public record MapConfig(int mapHeight,
                        int mapWidth,
                        double mapWaterLevel,
                        int grassDaily,
                        int grassEnergy,
                        int animalStartNumber,
                        int animalStartEnergy,
                        int animalGenomeLength,
                        int animalMinFedEnergy,
                        int animalBirthCost) {
}
