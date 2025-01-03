package assets.model.contract;

import assets.model.map.WorldMap;

public interface MapChangeListener {

    void mapChanged(WorldMap map, int day);

}
