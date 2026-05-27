package mia.modmod.render.util;

public abstract class EasingFunctions {
    public static float easeInOutCircular(float x) {
        return (float) (x < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2 : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2);
    }

    public static double easeInOutSine(double x) {
        return - (Math.cos(Math.PI * x) - 1) / 2;
    }
}
