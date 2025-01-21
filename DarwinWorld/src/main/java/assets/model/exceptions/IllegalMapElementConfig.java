package assets.model.exceptions;

public class IllegalMapElementConfig extends Exception {
    /**
     * Throws when settings parsed for Animal or Grass are
     * invalid to create a new Simulation instance
     */
    public IllegalMapElementConfig() {
        super("Given map element parameters are not correct");
    }
}
