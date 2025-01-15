package assets.model.contract;

import assets.model.map.AbstractMap;

@FunctionalInterface
public interface MapChangeListener {

    void mapChanged(AbstractMap map);

}
