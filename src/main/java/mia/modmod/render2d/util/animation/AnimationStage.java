package mia.modmod.render2d.util.animation;

public enum AnimationStage {
    OPENING(1),
    OPEN(0),
    CLOSING(-1),
    CLOSED(0);

    public final int direction;

    AnimationStage(int direction) {
        this.direction = direction;
    }
}
