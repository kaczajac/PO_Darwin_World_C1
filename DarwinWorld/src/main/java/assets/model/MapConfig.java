package assets.model;

public record MapConfig(int mapHeight,
                          int mapWidth,
                          int grassDaily,
                          int grassEnergy,
                          int animalsStart,
                          int animalStartEnergy,
                          int animalGenomeLength) {
}
