package Model;

public class Obstacle {

    private String name;
    private int height;

    public Obstacle(String name, int height) {
        this.name = name;
        this.height = height;
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
}
