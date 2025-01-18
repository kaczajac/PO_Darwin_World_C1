package assets.model;

import assets.model.enums.TileState;
import assets.model.map.AbstractMap;
import assets.model.mapelement.Animal;
import assets.model.mapelement.Grass;
import assets.model.mapelement.MapElement;
import assets.model.records.Vector2d;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.Optional;


public class MapElementBox {

    private final Group box = new Group();

    private final Rectangle background;
    private final MapElement element;
    private final double size;
    private final Vector2d position;

    public MapElementBox(AbstractMap map, Vector2d position, double size) {

        this.size = size;
        this.position = position;
        this.background = getBackground(map);
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
        return this.box;
    }

    public MapElement getMapElement() {
        return this.element;
    }

    public Vector2d getPosition() {
        return this.position;
    }

//// Helpers

    private Rectangle getBackground(AbstractMap map) {

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
        this.background.setFill(Color.FIREBRICK);
    }

    public void restoreDefaultBackground(AbstractMap map) {
        changeBackgroundColorBasedOnTileState(this.background, map.getTileAt(this.position).getState());
    }

    public void markAsPopularGrassPosition() {
        this.background.setStrokeType(StrokeType.INSIDE);
        this.background.setStroke(Color.ORANGE);
        this.background.setStrokeWidth(1.5);
    }

    public boolean containsSelectedAnimal(MapElementBox selectedBox) {
        if (selectedBox == null) return false;
        if (this.containsAnimal() && selectedBox.containsAnimal()) {
            Animal selectedAnimal = (Animal) selectedBox.getMapElement();
            return this.element.equals(selectedAnimal);
        }
        return false;
    }

    public boolean containsAnimal() {
        if (this.isEmpty()) return false;
        return this.element.getClass().isAssignableFrom(Animal.class);
    }

    public boolean containsGrass() {
        if (this.isEmpty()) return false;
        return this.element.getClass().isAssignableFrom(Grass.class);
    }

    private boolean isEmpty() {
        return this.element == null;
    }


}
