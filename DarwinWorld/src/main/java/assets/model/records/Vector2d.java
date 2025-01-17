package assets.model.records;

public record Vector2d(int x, int y) {

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean precedes(Vector2d other) {
        return (this.x <= other.x) && (this.y <= other.y);
    }

    public boolean follows(Vector2d other) {
        return (this.x >= other.x) && (this.y >= other.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vector2d other))
            return false;
        if (x != other.x()) return false;
        return y == other.y();
    }

}
