package assets.model;

import assets.model.enums.TileState;
import assets.model.map.AbstractMap;
import assets.model.mapelement.MapElement;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapElementBox {

    private final Group cell = new Group();

    public MapElementBox(AbstractMap map, Vector2d position, double size) {

        TileState positionTileState = map.getTileAt(position).getState();
        Rectangle cellBackground = getBackground(positionTileState, size);
        cell.getChildren().add(cellBackground);

        MapElement positionElement = map.objectAt(position);
        ImageView cellImage = getImageForCell(positionElement);
        if (cellImage != null) {
            cell.getChildren().add(cellImage);
            cellImage.setFitWidth(Math.floor(size / 2) * 2);
            cellImage.setFitHeight(Math.floor(size / 2) * 2);
        }

    }

    private Rectangle getBackground(TileState positionTileState, double size) {
        Rectangle background = new Rectangle(size, size);

        switch(positionTileState) {
            case PLAINS ->  {
                background.setFill(Color.LIMEGREEN); background.setStroke(Color.GREEN);}
            case FOREST ->  {
                background.setFill(Color.FORESTGREEN); background.setStroke(Color.GREEN);}
            case WATER ->  {
                background.setFill(Color.DODGERBLUE); background.setStroke(Color.DARKCYAN);}
        }
        background.setStrokeWidth(1);
        return background;
    }

    private ImageView getImageForCell(MapElement element) {
        if (element == null) return null;
        else return element.getImageRepresentation();
    }

    public Group display() {
        return cell;
    }
}
