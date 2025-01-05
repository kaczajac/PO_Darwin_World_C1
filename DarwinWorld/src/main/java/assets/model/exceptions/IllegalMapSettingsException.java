package assets.model.exceptions;

public class IllegalMapSettingsException extends Exception {

    public IllegalMapSettingsException() {
        super("Given map settings are not correct");
    }

}
