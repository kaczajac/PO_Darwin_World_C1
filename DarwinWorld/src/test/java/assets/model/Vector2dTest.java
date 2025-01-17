package assets.model;

import assets.model.records.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class Vector2dTest {

    @Test
    void addingTwoVectorsShouldReturnNewVectorWithValueEqualToSumOfTheirValues() {
        //given
        Vector2d vector = new Vector2d(28, 17);
        Vector2d other = new Vector2d(12, 31);
        //when
        Vector2d result = vector.add(other);
        //then
        assertEquals(new Vector2d(40, 48), result);
    }

    @Test
    void subtractingTwoVectorsShouldReturnNewVectorWithValueEqualToDifferenceOfTheirValues() {
        //given
        Vector2d vector = new Vector2d(28, 17);
        Vector2d other = new Vector2d(12, 31);
        //when
        Vector2d result = vector.subtract(other);
        //then
        assertEquals(new Vector2d(16, -14), result);
    }

    @Test
    void oppositeVectorValueShouldBeEqualToOriginalVectorValueMultipliedByNegativeOne() {
        //given
        Vector2d vector = new Vector2d(13, 41);
        //when
        Vector2d result = vector.opposite();
        //then
        assertEquals(new Vector2d((-1)*13, (-1)*41), result);
    }

    @Test
    void equalsVerify() {
        //All cases
        Vector2d vector1 = new Vector2d(28, 17);
        Vector2d vector2 = new Vector2d(28, 17);
        Vector2d vector3 = new Vector2d(14, 17);
        String vector4 = "test";

        // if (this == other) return True
        assertEquals(vector1, vector1);

        // Two vectors with the same value should be equal
        assertEquals(vector1, vector2);

        // Two vectors with different values should not be equal
        assertNotEquals(vector1, vector3);

        // if(!(other instanceof Vector2d)) return False
        assertNotEquals(vector1, vector4);

    }

}