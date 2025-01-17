package assets.model.util;

import assets.model.enums.MapType;
import assets.model.exceptions.IllegalMapSettingsException;
import assets.model.map.DefaultMap;
import assets.model.map.WaterMap;
import assets.model.map.AbstractMap;
import assets.model.records.MapSettings;

public class MapBuilder {

    // Mandatory map parameters
    private int height = 15;
    private int width = 15;
    private MapType type = MapType.DEFAULT;

    // Optional parameters for map generation
    private Double waterLevel = 0.0;

    public MapBuilder changeSettings(MapSettings settings) {
        this.height = settings.mapHeight();
        this.width = settings.mapWidth();
        this.type = settings.mapType();
        this.waterLevel = settings.mapWaterLevel();
        return this;
    }

    public MapBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public MapBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public MapBuilder setType(MapType type) {
        this.type = type;
        return this;
    }

    public MapBuilder setWaterLevel(Double waterLevel) {
        this.waterLevel = waterLevel;
        return this;
    }

    public AbstractMap build() throws IllegalMapSettingsException {

        if (incorrectSettings()) {
            throw new IllegalMapSettingsException();
        }

        MapSettings settings = new MapSettings(height, width, type, waterLevel);

        return switch (type) {
            case DEFAULT -> new DefaultMap(settings);
            case WATER -> new WaterMap(settings);
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
