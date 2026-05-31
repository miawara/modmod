package mia.modmod.mixin.input;

import mia.modmod.Mod;
import mia.modmod.render2d.hud_screens.InGameHudManager;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
public abstract class MMouseHandler {

    @Inject(at = @At("HEAD"), method = "onButton", cancellable = true)
    private void onButton(long l, MouseButtonInfo mouseButtonInfo, int i, CallbackInfo ci) {
        InGameHudManager.onMouseButton(l, mouseButtonInfo, i, ci);
    }

    @Inject(at = @At("HEAD"), method = "onScroll", cancellable = true)
    private void onScroll(long l, double d, double e, CallbackInfo ci) {
        Mod.MC.getFramerateLimitTracker().onInputReceived();
        boolean bl = Mod.MC.options.discreteMouseScroll().get();
        double f = Mod.MC.options.mouseWheelSensitivity().get();
        double g = (bl ? Math.signum(d) : d) * f;
        double h = (bl ? Math.signum(e) : e) * f;
        InGameHudManager.onScroll(l, g, h, ci);
    }
}