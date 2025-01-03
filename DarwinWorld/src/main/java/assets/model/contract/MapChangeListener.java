package assets.model.contract;

import assets.model.WorldMap;

public interface MapChangeListener {

    void mapChanged(WorldMap map, int day);

}
