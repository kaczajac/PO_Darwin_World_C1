package assets.model.util;

import assets.model.enums.WorldMapType;
import assets.model.exceptions.IllegalMapSettingsException;
import assets.model.map.DefaultWorldMap;
import assets.model.map.WaterWorldMap;
import assets.model.map.AbstractWorldMap;
import assets.model.records.WorldMapSettings;

public class WorldMapBuilder {

    // Mandatory map parameters
    private int height = 15;
    private int width = 15;
    private WorldMapType type = WorldMapType.DEFAULT;

    // Optional parameters for map generation
    private Double waterLevel = 0.0;

    public WorldMapBuilder changeSettings(WorldMapSettings settings) {
        this.height = settings.mapHeight();
        this.width = settings.mapWidth();
        this.type = settings.mapType();
        this.waterLevel = settings.mapWaterLevel();
        return this;
    }

    public WorldMapBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public WorldMapBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public WorldMapBuilder setType(WorldMapType type) {
        this.type = type;
        return this;
    }

    public WorldMapBuilder setWaterLevel(Double waterLevel) {
        this.waterLevel = waterLevel;
        return this;
    }

    public AbstractWorldMap build() throws IllegalMapSettingsException {

        if (incorrectSettings()) {
            throw new IllegalMapSettingsException();
        }

        WorldMapSettings settings = new WorldMapSettings(height, width, type, waterLevel);

        return switch (type) {
            case DEFAULT -> new DefaultWorldMap(settings);
            case WATER -> new WaterWorldMap(settings);
        };

    }

//// Helper functions

    private boolean incorrectSettings() {
        return height < 0 || height > 100
                || width < 0 || width > 100
                || type == null
                || waterLevel < 0 || waterLevel > 0.5;
    }
}
