package mia.modmod.features.impl.general;

import mia.modmod.core.KeyBindCategories;
import mia.modmod.features.Categories;
import mia.modmod.features.Feature;
import mia.modmod.features.impl.internal.server.ServerManager;
import mia.modmod.features.listeners.impl.*;
import mia.modmod.render2d.hud_screens.InGameHudManager;
import mia.modmod.render2d.hud_screens.impl.NodeSwitcherScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

public final class NodeSwitcher extends Feature implements RegisterKeyBindEvent, TickEvent, RenderHUD {
    public KeyMapping nodeSwitcherKeybind;
    private NodeSwitcherScreen nodeSwitcherScreen;

    public NodeSwitcher(Categories category) {
        super(category, "Node Switcher", "nodeswitcher", "Screen for quickly selecting nodes");
        nodeSwitcherKeybind = new KeyMapping("Open Node Switcher", GLFW.GLFW_KEY_K, KeyBindCategories.GENERAL.getCategory());
    }

    @Override
    public void renderHUD(GuiGraphics context, DeltaTracker tickCounter) {
    }

    @Override
    public void registerKeyBind() {
        KeyBindingHelper.registerKeyBinding(nodeSwitcherKeybind);
    }

    @Override
    public void tickR(int tick) {
        if (ServerManager.isNotOnDiamondFire()) return;
        boolean keybindDown = nodeSwitcherKeybind.isDown();

        if (nodeSwitcherScreen == null && keybindDown) InGameHudManager.setInGameHudScreen(nodeSwitcherScreen = new NodeSwitcherScreen());


        if (nodeSwitcherScreen != null && !keybindDown) {
            nodeSwitcherScreen.close();
            nodeSwitcherScreen = null;
        }
    }

    @Override
    public void tickF(int tick) {

    }
}
