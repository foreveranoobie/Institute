package levels;

public class DefaultLevel extends Level {
    public DefaultLevel(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object oppositeLevel) {
        return getName().trim().equals(((Level) oppositeLevel).getName().trim());
    }

    @Override
    public String toString() {
        return getName();
    }
}
