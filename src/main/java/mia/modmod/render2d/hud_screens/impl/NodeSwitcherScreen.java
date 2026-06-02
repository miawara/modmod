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

import java.util.*;

public class NodeSwitcherScreen extends InGameHudScreen {
    private DrawObject screenContainer, mainContainer;
    private ArrayList<DrawButton> buttons;
    private ArrayList<DrawVerticalScrollContainer> scrollContainers;

    public NodeSwitcherScreen() {
        super();
    }

    @Override
    protected void init() {
        Vector2i mouse = new Vector2i((int)getMouseX(), (int)getMouseY());
        Vector2i screen = new Vector2i(Mod.getScaledWindowWidth(), Mod.getScaledWindowHeight());
        this.buttons = new ArrayList<>();
        this.scrollContainers = new ArrayList<>();

        screenContainer = new DrawRect(new Vector2i(0,0), screen,  new ARGB(ColorBank.BLACK, 0.0f));

        Optional<LinkedHashMap<NodeCategory, ArrayList<String>>> optionalNodeCategories = LocationAPI.getNodeCategories();

        if (optionalNodeCategories.isPresent()) {
            LinkedHashMap<NodeCategory, ArrayList<String>> nodeCategories = optionalNodeCategories.get();
            LinkedHashMap<NodeCategory, DrawVerticalScrollContainer> nodeDrawContainers = new LinkedHashMap<>();

            int nodeOptionWidth = 100;
            int nodeButtonMargin = 4;
            int nodeOptionHeight = Mod.MC.font.lineHeight + (nodeButtonMargin*2);
            int maxDisplayNodes = 15;
            int containerMargin = 4;

            mainContainer = new DrawRect(new Vector2i(0,0), new Vector2i((nodeOptionWidth * (nodeCategories.size())) + (containerMargin+(nodeCategories.size())),nodeOptionHeight * (maxDisplayNodes+1)),  new ARGB(ColorBank.BLACK, 0.0f), screenContainer);
            mainContainer.setParentBinding(DrawBinding.MIDDLE_MIDDLE);
            mainContainer.setSelfBinding(DrawBinding.MIDDLE_MIDDLE);

            int index = 0;
            for (NodeCategory category : List.of(NodeCategory.DEV, NodeCategory.MAIN, NodeCategory.PRIVATE, NodeCategory.MISC_EVENT))  {
                ArrayList<String> nodes = nodeCategories.get(category);

                DrawRect header = new DrawRect(
                        new Vector2i((nodeOptionWidth * index) + (index * containerMargin), 0),
                        new Vector2i(nodeOptionWidth, nodeOptionHeight),
                        new ARGB(category.getColor(), 0.8f),
                        mainContainer
                );
                DrawText headerText = new DrawText(
                        new Vector2i(nodeButtonMargin,0),
                        Component.literal(category.getName()).withColor(ColorBank.WHITE),
                        1f,
                        true,
                        header
                );
                headerText.setParentBinding(AxisBinding.NONE, AxisBinding.MIDDLE);
                headerText.setSelfBinding(AxisBinding.NONE, AxisBinding.MIDDLE);

                DrawVerticalScrollContainer nodeTypeContainer = new DrawVerticalScrollContainer(
                        new Vector2i(0,0),
                        new Vector2i(nodeOptionWidth, nodeOptionHeight * maxDisplayNodes),
                        new ARGB(ColorBank.BLACK, 0.35f),
                        header
                );
                nodeTypeContainer.setParentBinding(AxisBinding.NONE, AxisBinding.FULL);

                int yoffset = 0;
                for (String nodeID : nodes) {
                    DrawButton nodeButton = new DrawButton(
                            new Vector2i(0, yoffset),
                            new Vector2i(nodeOptionWidth, nodeOptionHeight),
                            new ARGB(ColorBank.BLACK, 0.6f),
                            new ARGB(ColorBank.MC_GRAY, 0.8f)
                    );

                    nodeButton.setCallback(() -> {
                        close();
                        Mod.sendCommand("/server " + nodeID);
                    });
                    buttons.add(nodeButton);

                    DrawText nodeText = new DrawText(
                            new Vector2i(nodeButtonMargin,0),
                            Component.literal(nodeID).withColor(ColorBank.WHITE_GRAY),
                            1f,
                            true,
                            nodeButton
                    );
                    yoffset += nodeOptionHeight;
                    nodeText.setParentBinding(AxisBinding.NONE, AxisBinding.MIDDLE);
                    nodeText.setSelfBinding(AxisBinding.NONE, AxisBinding.MIDDLE);
                    nodeTypeContainer.addContent(nodeButton);
                }

                scrollContainers.add(nodeTypeContainer);
                index++;
            }
        } else {
            DrawText errorText = new DrawText(
                    new Vector2i(0,0),
                    Component.literal("Error: Haven't received node list from server yet").withColor(ColorBank.MC_RED),
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
        if (i == 1) for (DrawButton button : buttons) button.mouseClick(mouseButtonEvent, false);
    }


    @Override
    public void onScroll(long l, double scrollX, double scrollY, CallbackInfo ci) {
        super.onScroll(l,scrollX,scrollY,ci);
        if (mainContainer != null) {
            if (mainContainer.containsPoint(getMouseX(), getMouseY())) {
                for (DrawVerticalScrollContainer scrollContainer : scrollContainers) {
                    scrollContainer.scroll(new Vector2i((int) getMouseX(), (int) getMouseY()), new Vector2d(scrollX, scrollY));
                }
            }
        }

    };

    @Override
    public void onRender(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci) {
        screenContainer.render(context, (int) getMouseX(), (int) getMouseY());
    }

    @Override
    public void close() {
        super.close();
    }
}
