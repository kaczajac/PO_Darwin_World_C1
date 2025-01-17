package assets.model.exceptions;

import assets.model.records.Vector2d;

public class IllegalPositionException extends Exception{

    public IllegalPositionException(Vector2d position) {
        super("Position " + position + " is not correct");
    }

}
