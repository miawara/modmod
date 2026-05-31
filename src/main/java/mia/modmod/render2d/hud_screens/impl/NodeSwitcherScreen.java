package mia.modmod.render2d.hud_screens.impl;

import mia.modmod.ColorBank;
import mia.modmod.Mod;
import mia.modmod.features.impl.internal.mode.LocationAPI;
import mia.modmod.features.impl.internal.mode.NodeCategory;
import mia.modmod.render2d.hud_screens.InGameHudScreen;
import mia.modmod.render2d.util.*;
import mia.modmod.render2d.util.elements.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class NodeSwitcherScreen extends InGameHudScreen {
    private DrawObject screenContainer;
    private ArrayList<DrawButton> buttons = new ArrayList<>();
    private ArrayList<DrawVerticalScrollContainer> scrollContainers = new ArrayList<>();

    public NodeSwitcherScreen() { }

    @Override
    protected void init() {
        Vector2i mouse = new Vector2i((int)getMouseX(), (int)getMouseY());
        Vector2i screen = new Vector2i(Mod.getScaledWindowWidth(), Mod.getScaledWindowHeight());
        this.buttons = new ArrayList<>();
        //this.scrollContainers = new DrawVerticalScrollContainer();

        screenContainer = new DrawRect(new Vector2i(0,0), screen,  new ARGB(ColorBank.BLACK, 0f));

        Optional<HashMap<NodeCategory, ArrayList<String>>> optionalNodeCategories = LocationAPI.getNodeCategories();

        if (optionalNodeCategories.isPresent()) {
            HashMap<NodeCategory, ArrayList<String>> nodeCategories = optionalNodeCategories.get();

            ArrayList<String> mainNodes = nodeCategories.get(NodeCategory.MAIN);
            ArrayList<String> privateNodes = nodeCategories.get(NodeCategory.PRIVATE);
            ArrayList<String> devNodes = nodeCategories.get(NodeCategory.DEV);
            ArrayList<String> miscEvent = nodeCategories.get(NodeCategory.MISC_EVENT);

            Mod.message(mainNodes.toString());
            Mod.message(privateNodes.toString());
            Mod.message(devNodes.toString());
            Mod.message(miscEvent.toString());


            DrawRect mainNodeContainer = new DrawRect(new Vector2i(0,0), new Vector2i(300,100),  new ARGB(ColorBank.BLACK, 0.45f), screenContainer);
            mainNodeContainer.setSelfBinding(DrawBinding.MIDDLE_MIDDLE);
            mainNodeContainer.setParentBinding(DrawBinding.MIDDLE_MIDDLE);




        } else {
            DrawText errorText = new DrawText(
                    new Vector2i(0,0),
                    Component.literal("Error: node list is null").withColor(ColorBank.MC_RED),
                    1f,
                    true,
                    screenContainer
            );
            errorText.setSelfBinding(DrawBinding.MIDDLE_MIDDLE);
            errorText.setParentBinding(DrawBinding.MIDDLE_MIDDLE);
        }
    }
    @Override
    public void onMouseButton(long l, MouseButtonEvent mouseButtonEvent, int i, CallbackInfo ci) {
        super.onMouseButton(l, mouseButtonEvent, i, ci);
        for (DrawButton button : buttons) button.mouseClick(mouseButtonEvent, false);
    }


    @Override
    public void onScroll(long l, double scrollX, double scrollY, CallbackInfo ci) {
        super.onScroll(l,scrollX,scrollY,ci);
        for (DrawVerticalScrollContainer scrollContainer : scrollContainers) scrollContainer.scroll(new Vector2i((int) getMouseX(), (int) getMouseY()), new Vector2d(scrollX, scrollY));
    };

    @Override
    public void onRender(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci) {
        // render2d screen
        screenContainer.render(context, (int) getMouseX(), (int) getMouseY());
    }

    @Override
    public void close() {
        super.close();
    }
}
