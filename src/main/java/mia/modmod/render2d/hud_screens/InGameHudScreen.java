package mia.modmod.render2d.hud_screens;

import mia.modmod.Mod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class InGameHudScreen {
    public InGameHudScreen() {
        releaseMouse();
        init();
    }

    protected abstract void init();

    public void onMouseButton(long l, MouseButtonEvent mouseButtonInfo, int i, CallbackInfo ci) {
        ci.cancel();
    };
    public void onScroll(long l, double scrollX, double scrollY, CallbackInfo ci) {
        ci.cancel();
    };
    public abstract void onRender(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci);
    public void renderCrosshair(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci) {
        ci.cancel();
    };

    public void releaseMouse() {
        Mod.MC.mouseHandler.releaseMouse();
    }

    public void grabMouse() {
        Mod.MC.mouseHandler.grabMouse();
    }

    public void close() {
        grabMouse();
        InGameHudManager.setInGameHudScreen(null);
    };

    public double getMouseX() { return Mod.MC.mouseHandler.xpos() / Mod.MC.getWindow().getGuiScale(); }
    public double getMouseY() { return Mod.MC.mouseHandler.ypos() / Mod.MC.getWindow().getGuiScale(); }

}

