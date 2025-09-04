package astaro.midmmo.core.GUI.utils;

public class LayoutHelper {
    public static int getPercentX(int screenWidth, float percent) {
        return (int) (screenWidth * percent);
    }

    public static int getPercentY(int screenHeight, float percent) {
        return (int) (screenHeight * percent);
    }

    public static int getCenterX(int screenWidth, int elementWidth) {
        return (screenWidth - elementWidth) / 2;
    }

    public static int getCenterY(int screenHeight, int elementHeight) {
        return (screenHeight - elementHeight) / 2;
    }
}
