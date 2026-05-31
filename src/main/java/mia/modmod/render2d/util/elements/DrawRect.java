package mia.modmod.render2d.util.elements;

import mia.modmod.render2d.util.ARGB;
import mia.modmod.render2d.util.DrawContextHelper;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2i;

import java.util.ArrayList;

public class DrawRect extends DrawObject {
    protected final ARGB color;

    public DrawRect(Vector2i position, Vector2i size, ARGB color) { this(position, size, color, null); }
    public DrawRect(Vector2i position, Vector2i size, ARGB color, DrawObject parent) {
        this.position = position;
        this.size = size;
        this.color = color;
        if (parent != null) parent.addDrawable(this);
        this.drawables = new ArrayList<>();
    }

    @Override
    protected void draw(GuiGraphics context, int mouseX, int mouseY) {
        DrawContextHelper.drawRect(context, x1(), y1(), getWidth(), getHeight(), color);
    }
}
