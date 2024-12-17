package assets.model;

import java.util.Objects;

public class Vector2d {
    private float x;
    private float y;

    public Vector2d(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2d getPosition(){
        return new Vector2d(x,y);
    }

    public Vector2d getPositionReference(){
        return this;
    }

    public Vector2d opposite(){
        return new Vector2d(-x , -y);
    }

    public void add(Vector2d add){
        x += add.getX();
        y += add.getY();
    }

    public void substract(Vector2d sub){
        x -= sub.getX();
        y -= sub.getY();
    }

    public boolean smallerThan(Vector2d other){
        if(x >= other.getX()) return false;
        return !(y >= other.getY());
    }

    public boolean biggerThan(Vector2d other){
        if(x <= other.getX()) return false;
        return !(y <= other.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vector2d other))
            return false;
        if(x != other.getX()) return false;
        return y == other.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
