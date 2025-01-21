package assets.model.contract;

import assets.model.map.AbstractWorldMap;

@FunctionalInterface
public interface MapChangeListener {

    void mapChanged(AbstractWorldMap map);

}
