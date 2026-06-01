package mia.modmod.features.impl.moderation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import mia.modmod.Mod;
import mia.modmod.core.KeyBindCategories;
import mia.modmod.features.Categories;
import mia.modmod.features.Feature;
import mia.modmod.features.listeners.impl.RegisterCommandListener;
import mia.modmod.features.listeners.impl.RegisterKeyBindEvent;
import mia.modmod.features.listeners.impl.TickEvent;
import mia.modmod.features.listeners.impl.WorldRenderEventListener;
import mia.modmod.render2d.util.DrawContextHelper;
import mia.modmod.render2d.util.Line;
import mia.modmod.render2d.util.RenderContextHelper;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class ClickTP extends Feature implements RegisterKeyBindEvent, TickEvent, RegisterCommandListener {
    public KeyMapping clicktp;

    public ClickTP(Categories category) {
        super(category, "clicktp actual just hacks", "hacks", "title");
        clicktp = new KeyMapping("clicktp", GLFW.GLFW_KEY_U, KeyBindCategories.STAFF.getCategory());
    }

    @Override
    public void registerKeyBind() {
        KeyBindingHelper.registerKeyBinding(clicktp);
    }

    @Override
    public void tickR(int tick) {
        if (clicktp.isDown()) {
            HitResult hitResult = Mod.MC.player.raycastHitResult(1000,Mod.MC.player);
            forcetp(0,0,110);

        }
    }

    @Override
    public void tickF(int tick) {

    }

    private void forcetp(double x, double y, double z) {
        Mod.message("forcetp(" + x + ", " + y + ", " + z + ")");

        Vec3 playerPos = Mod.MC.player.position();
        float maxMoveDistance = 110f;

        for (int k = 0; k < 5; k++) Mod.MC.player.connection.send(new ServerboundMovePlayerPacket.StatusOnly(false, true));
        for (int i = 0; i < 5; i++) {
            Vec3 newPos = playerPos.add(0, 0, (i+1)*maxMoveDistance);
            Mod.message(newPos.toString());
            for (int k = 0; k < 10; k++) Mod.MC.player.connection.send(new ServerboundMovePlayerPacket.StatusOnly(true, true));
            Mod.MC.player.connection.send(new ServerboundMovePlayerPacket.Pos(newPos, true, true));

        }
        Vec3 newPos = playerPos.add(0, 0, (5)*maxMoveDistance);
        Mod.MC.player.teleportTo(newPos.x, newPos.y, newPos.z);
    }





    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(
                ClientCommandManager.literal("forcetp")
                        .then(
                                ClientCommandManager.argument("x", DoubleArgumentType.doubleArg())
                                        .then(
                                                ClientCommandManager.argument("y", DoubleArgumentType.doubleArg())
                                                        .then(
                                                                ClientCommandManager.argument("z", DoubleArgumentType.doubleArg())
                                                                        .executes(commandContext -> {
                                                                            double x = DoubleArgumentType.getDouble(commandContext, "x");
                                                                            double y = DoubleArgumentType.getDouble(commandContext, "y");
                                                                            double z = DoubleArgumentType.getDouble(commandContext, "z");

                                                                            forcetp(x,y,z);
                                                                            return 1;
                                                                        })

                                                        )

                                        )

                        )
        );
    }


}
