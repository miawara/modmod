package mia.modmod.render2d.util.elements;

import mia.modmod.render2d.util.ARGB;
import mia.modmod.render2d.util.DrawContextHelper;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2d;
import org.joml.Vector2i;

import java.util.ArrayList;

public class DrawVerticalScrollContainer extends DrawObject {
    protected final ARGB color;
    private double scrollOffset;

    private ArrayList<DrawObject> contents;

    public DrawVerticalScrollContainer(Vector2i position, Vector2i size, ARGB color) { this(position, size, color, null); }

    public DrawVerticalScrollContainer(Vector2i position, Vector2i size, ARGB color, DrawObject parent) {
        this.position = position;
        this.size = size;
        this.color = color;
        if (parent != null) parent.addDrawable(this);
        this.drawables = new ArrayList<>();
        this.contents = new ArrayList<>();
        this.setRenderWithScissors(true, true);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY) {
        for (DrawObject content : contents) {
            content.setRenderOffset(new Vector2i(0, (int) -scrollOffset));
        }
        super.render(context,mouseX,mouseY);
    }

    @Override
    protected void draw(GuiGraphics context, int mouseX, int mouseY) {
        DrawContextHelper.drawRect(context, x1(), y1(), getWidth(), getHeight(), color);
    }

    public void addContent(DrawObject content) {
        this.contents.add(content);
        addDrawable(content);
    }

    public Vector2i getContentSize() {
        if (contents.isEmpty()) return new Vector2i(0,0);
        int maxWidth = 0;

        for (DrawObject content : contents) if (content.getWidth() > maxWidth) maxWidth = content.getWidth();

        return new Vector2i(maxWidth,
                contents.getLast().y2()-contents.getFirst().y1()
                );
    }


    public void scroll(Vector2i mouse, Vector2d scroll) {
        if (!containsPoint(mouse.x, mouse.y)) return;
        scrollOffset -= scroll.y * 20;

        scrollOffset = Math.clamp(
                scrollOffset,
                0,
                sizeDelta()
        );

    }


    private double sizeDelta() {
        return Math.max(0, getContentSize().y - getHeight());
    }
}
