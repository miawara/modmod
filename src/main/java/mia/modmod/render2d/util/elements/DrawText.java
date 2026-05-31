package mia.modmod.render2d.util.elements;

import mia.modmod.Mod;
import mia.modmod.render2d.util.DrawContextHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

import java.util.ArrayList;

public class DrawText extends DrawObject {
    public Component text;
    private boolean shadow;
    private float alpha;

    public DrawText(Vector2i position, Component text, float alpha, boolean shadow) {
        this(position, text, alpha, shadow, null);
    }

    public DrawText(Vector2i position, Component text, float alpha, boolean shadow, DrawObject parent) {
        this.position = position;
        this.text = text;
        this.alpha = alpha;
        this.shadow = shadow;
        if (parent != null) parent.addDrawable(this);
        this.drawables = new ArrayList<>();
    }

    public void setText(Component text) { this.text = text; }

    @Override
    public Vector2i getSize() { return new Vector2i(Mod.MC.font.width(text), Mod.MC.font.lineHeight); }

    @Override
    protected void draw(GuiGraphics context, int mouseX, int mouseY) {
        DrawContextHelper.drawText(context, text, x1(), y1(), alpha, shadow);
    }
}
