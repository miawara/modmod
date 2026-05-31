package mia.modmod.render2d.util.elements;

import mia.modmod.render2d.util.DrawContextHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class DrawCustomToolTip extends DrawObject {
    private final List<Component> lore;
    private final float yAnchor;

    public DrawCustomToolTip(Vector2i position, List<Component> lore, float yAnchor) {
        this(position, lore, yAnchor,null);
    }

    public DrawCustomToolTip(Vector2i position, List<Component> lore, float yAnchor, DrawObject parent) {
        this.position = position;
        this.lore = lore;
        this.yAnchor = yAnchor;
        if (parent != null) parent.addDrawable(this);
        this.drawables = new ArrayList<>();
    }

    @Override
    protected void draw(GuiGraphics context, int mouseX, int mouseY) {
        DrawContextHelper.drawTooltip(context, lore, mouseX, mouseY, yAnchor);
    }
}
