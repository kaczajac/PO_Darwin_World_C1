package assets.model.exceptions;

public class IllegalMapSettingsException extends Exception {
    /**
     * Throws when MapBuilder class instance fails to
     * build a new map
     */
    public IllegalMapSettingsException() {
        super("Given map settings are not correct");
    }

}
