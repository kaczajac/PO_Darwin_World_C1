package assets.model;

import assets.model.enums.TileState;
import assets.model.map.AbstractMap;
import assets.model.mapelement.MapElement;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Optional;


public class MapElementBox {

    private final Group box = new Group();

    private final Rectangle background;
    private final MapElement element;
    private final double size;

    public MapElementBox(AbstractMap map, Vector2d position, double size) {

        this.size = size;

        this.background = getBackground(map, position);
        box.getChildren().add(this.background);

        Optional<MapElement> elementOptional = map.objectAt(position);

        if (elementOptional.isPresent()) {

            this.element = elementOptional.get();

            ImageView elementImage = element.getImageRepresentation();
            box.getChildren().add(elementImage);
            elementImage.setFitWidth(Math.floor(size / 2) * 2);
            elementImage.setFitHeight(Math.floor(size / 2) * 2);

        }
        else {
            this.element = null;
        }

    }

    public Group display() {
        return box;
    }

    public MapElement getMapElement() {
        return element;
    }

//// Helpers

    private Rectangle getBackground(AbstractMap map, Vector2d position) {

        Rectangle background = new Rectangle(size, size);
        TileState positionTileState = map.getTileAt(position).getState();
        changeBackgroundColorBasedOnTileState(background, positionTileState);
        return background;

    }

    private void changeBackgroundColorBasedOnTileState(Rectangle background, TileState positionTileState) {

        switch (positionTileState) {
            case PLAINS ->  {
                background.setFill(Color.LIMEGREEN); background.setStroke(Color.GREEN);}
            case FOREST ->  {
                background.setFill(Color.FORESTGREEN); background.setStroke(Color.GREEN);}
            case WATER ->  {
                background.setFill(Color.DODGERBLUE); background.setStroke(Color.DARKCYAN);}
        }
        background.setStrokeWidth(1);

    }

    public void markAsSelected() {
        this.background.setFill(Color.SPRINGGREEN);
    }

    public void restoreDefaultBackground(AbstractMap map) {
        changeBackgroundColorBasedOnTileState(this.background, map.getTileAt(this.element.getPosition()).getState());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MapElementBox other)) return false;
        return this.element.getPosition().equals(other.element.getPosition()) && element.equals(other.element);
    }

}
