package assets.model.contract;

import assets.model.map.BaseMap;

public interface MapChangeListener {

    void mapChanged(BaseMap map, int day);

}
