package mia.modmod.render.util;

public class DrawBinding {
    private final AxisBinding xBinding;
    private final AxisBinding yBinding;
    public DrawBinding(AxisBinding xBinding, AxisBinding yBinding) {
        this.xBinding = xBinding;
        this.yBinding = yBinding;
    }

    public Point pointMultiply(Point point) {
        if (point == null) return new Point(0,0);
        return new Point((int) (point.x()*xBinding.getScale()), (int) (point.y()*yBinding.getScale()));
    }
}
