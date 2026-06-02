package mia.modmod.render2d.util;

import org.joml.Vector2i;

public class DrawBinding {
    public final AxisBinding xBinding;
    public final AxisBinding yBinding;
    public DrawBinding(AxisBinding xBinding, AxisBinding yBinding) {
        this.xBinding = xBinding;
        this.yBinding = yBinding;
    }

    public Vector2i pointMultiply(Vector2i point) {
        if (point == null) return new Vector2i(0,0);
        return new Vector2i((int) (point.x()*xBinding.getScale()*1f), (int) (point.y()*yBinding.getScale()*1f));
    }


    public static final DrawBinding NONE = new DrawBinding(AxisBinding.NONE, AxisBinding.NONE);
    public static final DrawBinding MIDDLE_MIDDLE = new DrawBinding(AxisBinding.MIDDLE, AxisBinding.MIDDLE);
    public static final DrawBinding NONE_MIDDLE = new DrawBinding(AxisBinding.NONE, AxisBinding.MIDDLE);
}
