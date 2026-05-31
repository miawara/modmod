package mia.modmod.mixin.render;

import mia.modmod.features.FeatureManager;
import mia.modmod.features.listeners.impl.RenderHUD;
import mia.modmod.render2d.hud_screens.InGameHudManager;
import mia.modmod.render2d.util.HudMatrixRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MGui {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void onRender(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci) {
        HudMatrixRegistry.setRenderHUDTickCounter(deltaTracker);
        FeatureManager.implementFeatureListener(RenderHUD.class, feature -> feature.renderHUD(context, deltaTracker));
        InGameHudManager.onRender(context, deltaTracker, ci);
    }

    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void renderCrosshair(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci) {
        InGameHudManager.renderCrosshair(context, deltaTracker, ci);
    }
}
