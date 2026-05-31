package mia.modmod.render2d.util;

import mia.modmod.Mod;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class RenderContextHelper {
    public static List<Vec3> getBoundingBoxCorners(AABB box) {
        List<Vec3> corners = new ArrayList<>();

        corners.add(new Vec3(box.minX, box.minY, box.minZ));
        corners.add(new Vec3(box.minX, box.minY, box.maxZ));
        corners.add(new Vec3(box.maxX, box.minY, box.minZ));
        corners.add(new Vec3(box.maxX, box.minY, box.maxZ));

        corners.add(new Vec3(box.minX, box.maxY, box.minZ));
        corners.add(new Vec3(box.minX, box.maxY, box.maxZ));
        corners.add(new Vec3(box.maxX, box.maxY, box.minZ));
        corners.add(new Vec3(box.maxX, box.maxY, box.maxZ));

        return corners;
    }

    public static List<Line> getBoundBoxWireframe(AABB box) {
        List<Line> lines = new ArrayList<>();

        List<Vec3> corners = getBoundingBoxCorners(box);

        for (int i = 0; i < 2; i++) {
            lines.add(new Line(corners.get((i * 4)), corners.get(1+(i*4))));
            lines.add(new Line(corners.get(2+(i*4)), corners.get(3+(i*4))));
            lines.add(new Line(corners.get((i * 4)), corners.get(2+(i*4))));
            lines.add(new Line(corners.get(1+(i*4)), corners.get(3+(i*4))));
        }

        lines.add(new Line(corners.get(0), corners.get(4)));
        lines.add(new Line(corners.get(1), corners.get(5)));
        lines.add(new Line(corners.get(2), corners.get(6)));
        lines.add(new Line(corners.get(3), corners.get(7)));


        return lines;
    }

    /*
        @return returns a Vec3, x & y are the screen coordinate, z is depth
     */
    public static Vec3 worldToScreen(Vec3 pos) {
        Camera camera = Mod.MC.getEntityRenderDispatcher().camera;
        int displayHeight = Mod.MC.getWindow().getScreenHeight();
        Vector3f target = new Vector3f();

        assert camera != null;
        double deltaX = pos.x - camera.position().x;
        double deltaY = pos.y - camera.position().y;
        double deltaZ = pos.z - camera.position().z;

        Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1.f).mul(HudMatrixRegistry.positionMatrix);

        Matrix4f matrixProj = new Matrix4f(HudMatrixRegistry.projectionMatrix);
        Matrix4f matrixModel = new Matrix4f(HudMatrixRegistry.modelViewMatrix);

        matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), HudMatrixRegistry.lastViewport, target);

        return new Vec3(target.x / Mod.MC.getWindow().getGuiScale(), (displayHeight - target.y) / Mod.MC.getWindow().getGuiScale(), target.z);
    }

    public static float getFov(float tickProgress) {
        try {
            return Mod.MC.gameRenderer.getFov(Mod.MC.gameRenderer.getMainCamera(), tickProgress, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}