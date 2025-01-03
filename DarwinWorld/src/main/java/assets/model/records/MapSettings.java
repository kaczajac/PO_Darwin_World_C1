package assets.model.records;

import assets.model.enums.MapType;

public record MapSettings(  // Map size
                            int mapHeight,
                            int mapWidth,

                            // Tile Generator parameters
                            MapType mapType,
                            Double mapWaterLevel) {
}
