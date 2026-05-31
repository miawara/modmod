package mia.modmod.render2d.util.elements;

import mia.modmod.render2d.util.ARGB;
import mia.modmod.render2d.util.DrawContextHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Vector2i;

import java.util.ArrayList;

public class DrawButton extends DrawObject {
    private ARGB disabledColor, enabledColor;
    private boolean enabled;
    private Runnable callback = () -> {};

    public DrawButton(Vector2i position, Vector2i size, ARGB disabledColor, ARGB enabledColor) {
        this(position, size, disabledColor, enabledColor, null);
    }

    public DrawButton(Vector2i position, Vector2i size, ARGB disabledColor, ARGB enabledColor, DrawObject parent) {
        this.position = position;
        this.size = size;
        this.disabledColor = disabledColor;
        this.enabledColor = enabledColor;
        if (parent != null) parent.addDrawable(this);
        this.drawables = new ArrayList<>();
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isEnabled() { return enabled; }

    public void leftMouseClick(MouseButtonEvent click, boolean doubled) {
        callback.run();
    };

    @Override
    public boolean mouseClick(MouseButtonEvent click, boolean doubled) {
        if (containsPoint(click.x(), click.y())) {
            if (click.button() == 0) {
                leftMouseClick(click, doubled);
            }
        }
        super.mouseClick(click, doubled);
        return false;
    };

    @Override
    protected void draw(GuiGraphics context, int mouseX, int mouseY) {
        DrawContextHelper.drawRect(context, x1(), y1(), getWidth(), getHeight(), enabled || containsPoint(mouseX, mouseY) ? enabledColor : disabledColor);
    }
}
