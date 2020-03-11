package Model;

public class Obstacle {

    private final String name;
    private final int height;
    private final int width;

    public Obstacle(String name, int height, int width) {
        this.name = name;
        this.height = height;
        this.width = width;
    }

    @Override
    public String toString() {
        return this.name + "(" + height + "m)";
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
