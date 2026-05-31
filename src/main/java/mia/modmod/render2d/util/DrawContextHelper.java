package mia.modmod.render2d.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mia.modmod.ColorBank;
import mia.modmod.Mod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class DrawContextHelper {
    public static void drawRect(GuiGraphics context, int x, int y, int width, int height, ARGB color) {
        context.fill(x,y,x+width,y+height,color.getARGB());
    }
    public static void drawRectBorder(GuiGraphics context, int x, int y, int width, int height, ARGB color) {
        drawRect(context, x, y, width, 1, color);
        drawRect(context, x, y, 1, height, color);
        drawRect(context, x+width-1, y, 1, height, color);
        drawRect(context, x, y+height-1, width, 1, color);
    }

    public static void drawText(GuiGraphics context, Component text, int x, int y, float alpha, boolean shadow) {
        context.drawString(Mod.MC.font, text, x, y, ARGB.getARGB(ColorBank.WHITE, alpha), shadow);
    }
    public static void drawTooltip(GuiGraphics context, Component text, int x, int y, float yAnchor) {
        drawTooltip(context, List.of(text), x, y, yAnchor);
    }


    // yAnchor: a value of 0.5 will center it around y and a value of 1 will make it render2d above y
    public static void drawTooltip(GuiGraphics context, List<Component> list, int x, int y, float yAnchor) {
        context.pose().pushMatrix();
        context.pose().transform(new Vector3f(0,0,-100));
        int baseWidth = 0;
        int baseHeight = (list.size() * Mod.MC.font.lineHeight) + ((list.size()-1) * 2);
        for (Component text : list) {
            int textWidth = Mod.MC.font.width(text);
            if (textWidth > baseWidth) baseWidth = textWidth;
        }

        int margin = 3;
        int baseX = x + 6;
        int baseY = (int) (y - (yAnchor * (baseHeight + (2 * margin))));
        int width = baseWidth + margin * 2;
        int height = baseHeight + margin * 2;

        drawRect(context, baseX+1, baseY+1, width-1, height-1, new ARGB(0x121212, 0.75));
        drawRectBorder(context, baseX, baseY, width, height, new ARGB(ColorBank.MIA_PURPLE, 1f));

        int i = 0;
        for (Component text : list) {
            drawText(context, text, baseX+margin, baseY + margin + i * (Mod.MC.font.lineHeight + 2), 1f, true);
            i++;
        }

    }
    public static void drawPlayerHead(GuiGraphics context, PlayerSkin playerSkin, int x, int y, int size) {
        context.blit(
                RenderPipelines.GUI_TEXTURED,
                playerSkin.body().texturePath(),
                x, y, // blit x y
                8, 8, // u, v
                size, size, // blit size
                8, 8, // blit area
                64, 64, // texture size
                -1
        );
        // draw layer1
        context.blit(
                RenderPipelines.GUI_TEXTURED,
                playerSkin.body().texturePath(),
                x-1, y-1,
                8 * 5, 8,
                size+2, size+2,
                8, 8,
                64, 64,
                -1
        );
    }





    private void drawHitbox(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vec3 cameraPos, AABB hitbox, int color, float width) {
        List<Line> lines = RenderContextHelper.getBoundBoxWireframe(hitbox);
        for (Line line : lines) drawLine(pose, vertexConsumer, cameraPos, line, color, width);
    }

    private void drawLine(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vec3 cameraPos, Line line, int color, float width) {
        drawLine(pose, vertexConsumer, cameraPos, line.start().toVector3f(), line.end().toVector3f(), color, width);
    }

    private void drawLine(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vec3 cameraPos, Vector3f v1, Vector3f v2, int color, float width) {
        Vector3f normal = cameraPos.toVector3f().sub(v1).normalize();

        vertexConsumer.addVertex(pose, v1)
                .setColor(color)
                .setNormal(pose, normal)
                .setLight(0xF000F0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLineWidth(width);

        vertexConsumer.addVertex(pose, v2)
                .setColor(color)
                .setNormal(pose, normal)
                .setLight(0xF000F0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLineWidth(width);
    }
}

