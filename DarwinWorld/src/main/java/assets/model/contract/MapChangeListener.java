package assets.model.contract;

import assets.model.map.AbstractMap;

public interface MapChangeListener {

    void mapChanged(AbstractMap map, int day);

}
