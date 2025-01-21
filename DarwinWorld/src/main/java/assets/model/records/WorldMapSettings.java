package assets.model.records;

import assets.model.enums.WorldMapType;

/**
 * Record responsible for storing all the parameters
 * for map generation.
 * <p>
 * <h6> Note </h6>
 * <p>
 * It is recommended that <b><i>mapWaterLevel</i></b> should maintain constant values for
 * the following map sizes:
 * <ul>
 *     <li> 10x10 to 20x20 -> <b>0.2</b> </li>
 *     <li> 20x20 to 30x30 -> <b>0.3</b> </li>
 *     <li> 40x40 and higher -> <b>0.4</b> </li>
 * </ul>
 * <p>
 * The reason for this is how the map generation algorithm works.
 * <p>
 *     When a new instance of <b><i>WaterMap</i></b> is created, the algorithm
 *     iterates over every cell of the 2d matrix deciding whether to put water or solid ground there.
 *     It follows a simple probability rule: when mapWaterLevel is equal to ex. 0.1 it means
 *     that a cell has a probability of 0.1 to become water. The next step in the procedure is to
 *     go to every cell of the matrix again and then decide the state of the cell base on its neighbors.
 *     When the majority of cell's neighbors is for ex. water then the cell itself should be water.
 * </p>
 * <p>
 *     Statistically when <b><i>mapWaterLevel</i></b> is equal to 0.5 then a half of the map should be under water, but
 *     in practice it is much more than that.
 * </p>
 */
public record WorldMapSettings(
                            int mapHeight,
                            int mapWidth,
                            WorldMapType mapType,
                            Double mapWaterLevel) {
}
