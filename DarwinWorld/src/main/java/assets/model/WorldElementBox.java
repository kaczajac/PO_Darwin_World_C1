package assets.model;

import assets.model.enums.TileState;
import assets.model.map.AbstractWorldMap;
import assets.model.mapelement.Animal;
import assets.model.mapelement.Grass;
import assets.model.mapelement.WorldElement;
import assets.model.records.Vector2d;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.Optional;


public class WorldElementBox {

    private final StackPane box = new StackPane();

    private final Rectangle background;
    private final WorldElement element;
    private final Vector2d position;
    private final double BOX_SIZE;


    public WorldElementBox(AbstractWorldMap map, Vector2d position, double BOX_SIZE) {

        this.BOX_SIZE = BOX_SIZE;
        this.position = position;
        this.background = addBackground(map);
        this.element = addMapElement(map);

    }

//// Getters

    public StackPane display() {
        return this.box;
    }

    public WorldElement getWorldElement() {
        return this.element;
    }

    public Vector2d getPosition() {
        return this.position;
    }

//// Helpers

    private Rectangle addBackground(AbstractWorldMap map) {

        Rectangle background = new Rectangle(BOX_SIZE, BOX_SIZE);
        TileState positionTileState = map.getTileAt(this.position).getState();
        changeBackgroundColorBasedOnTileState(background, positionTileState);
        box.getChildren().add(background);

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

    private WorldElement addMapElement(AbstractWorldMap map) {

        Optional<WorldElement> elementOptional = map.objectAt(this.position);

        if (elementOptional.isPresent()) {

            WorldElement element = elementOptional.get();
            ImageView elementImage = element.getImageRepresentation();
            elementImage.setFitWidth(Math.floor(BOX_SIZE / 2) * 2);
            elementImage.setFitHeight(Math.floor(BOX_SIZE / 2) * 2);
            box.getChildren().add(elementImage);

            return element;

        }
        return null;

    }


    public void restoreDefaultBackground(AbstractWorldMap map) {
        changeBackgroundColorBasedOnTileState(this.background, map.getTileAt(this.position).getState());
    }

    public void markAsSelectedAnimalBox() {
        this.background.setFill(Color.CORAL);
    }

    public void markAsPopularGrassPositionBox() {
        this.background.setStrokeType(StrokeType.INSIDE);
        this.background.setStroke(Color.ORANGE);
        this.background.setStrokeWidth(1.5);
    }

    public void markAsPopularGenomeAnimalBox() {
        this.background.setFill(Color.MEDIUMPURPLE);
    }

    public boolean containsSelectedAnimal(WorldElementBox selectedBox) {
        if (selectedBox == null) return false;
        if (this.containsAnimal() && selectedBox.containsAnimal()) {
            Animal selectedAnimal = (Animal) selectedBox.getWorldElement();
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
