package mia.modmod.render2d.hud_screens;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public final class InGameHudManager {
    private static InGameHudScreen inGameHudScreen;

    public static void setInGameHudScreen(InGameHudScreen screen) {
        inGameHudScreen = screen;
    }
    public static InGameHudScreen getInGameHudScreen() {
        return inGameHudScreen;
    }

    public static void onMouseButton(long l, MouseButtonInfo mouseButtonInfo, int i, CallbackInfo ci) {
        if (inGameHudScreen != null) inGameHudScreen.onMouseButton(l, new MouseButtonEvent(inGameHudScreen.getMouseX(), inGameHudScreen.getMouseY(), mouseButtonInfo), i, ci);
    };

    public static void onScroll(long l, double scrollX, double scrollY , CallbackInfo ci) {
        if (inGameHudScreen != null) inGameHudScreen.onScroll(l, scrollX, scrollY, ci);
    };
    public static void onRender(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (inGameHudScreen != null) inGameHudScreen.onRender(context, deltaTracker, ci);
    };
    public static void renderCrosshair(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (inGameHudScreen != null) inGameHudScreen.renderCrosshair(context, deltaTracker, ci);
    };


}
