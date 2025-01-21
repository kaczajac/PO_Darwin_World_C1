package assets.model.mapelement;


import assets.model.records.Vector2d;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class WorldElement {

    // Images
    protected final Map<Integer, Image> ANIMAL_IMAGES = new HashMap<>();
    {
        Image ANIMAL_IMAGE_1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/animals/animal_1.png")),64, 64, false, false);
        Image ANIMAL_IMAGE_2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/animals/animal_2.png")),64, 64, false, false);
        Image ANIMAL_IMAGE_3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/animals/animal_3.png")),64, 64, false, false);
        ANIMAL_IMAGES.put(1, ANIMAL_IMAGE_1);
        ANIMAL_IMAGES.put(2, ANIMAL_IMAGE_2);
        ANIMAL_IMAGES.put(3, ANIMAL_IMAGE_3);
    }
    protected final Image GRASS_IMAGE = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grass/grass_1.png")),64, 64, false, false);
    //

    static ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        imageView.setEffect(null);
        return imageView;
    }

    public abstract Vector2d getPosition();

    public abstract UUID getID();

    public abstract ImageView getImageRepresentation();

}
