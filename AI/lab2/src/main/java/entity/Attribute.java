package entity;

import levels.Level;

public class Attribute {
    private Level level;

    public Attribute(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
