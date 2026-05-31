package mia.modmod.render2d.util.elements;

import mia.modmod.render2d.util.ARGB;
import mia.modmod.render2d.util.DrawContextHelper;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2i;

public class DrawOutlineRect extends DrawRect {
    public DrawOutlineRect(Vector2i position, Vector2i size, ARGB color) {
        this(position, size, color, null);
    }

    public DrawOutlineRect(Vector2i position, Vector2i size, ARGB color, DrawObject parent) {
        super(position, size, color, parent);
    }

    @Override
    protected void draw(GuiGraphics context, int mouseX, int mouseY) {
        DrawContextHelper.drawRectBorder(context, x1(), y1(), getWidth(), getHeight(), color);
    }
}
