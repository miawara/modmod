package mia.modmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mia.modmod.config.ConfigStore;
import mia.modmod.features.FeatureManager;
import mia.modmod.features.listeners.impl.*;
import mia.modmod.render2d.util.HudMatrixRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Mod implements ClientModInitializer {
	public static final String MOD_ID = "modmod";

	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Minecraft MC = Minecraft.getInstance();
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static int tick = 0;

	@Override
	public void onInitializeClient() {
		ConfigStore.load();
		FeatureManager.init();
		Mod.registerCallbacks();
		FeatureManager.getFeaturesByIdentifier(RegisterKeyBindEvent.class).forEach(RegisterKeyBindEvent::registerKeyBind);
		log("modmod initialized");
	}


	public static void shutdownClient() {
		ConfigStore.save();
		log("shutting down client");
	}

	private static void registerCallbacks() {
		log("registering callbacks...");

		WorldRenderEvents.END_MAIN.register(context -> {
			HudMatrixRegistry.register(context);
			FeatureManager.implementFeatureListener(WorldRenderEventListener.class, feature -> { feature.WorldRenderEvents_END_MAIN(context); });

		});
		WorldRenderEvents.BEFORE_TRANSLUCENT.register(context -> {
			FeatureManager.implementFeatureListener(WorldRenderEventListener.class, feature -> { feature.WorldRenderEvents_BEFORE_TRANSLUCENT(context); });
		});

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			FeatureManager.implementFeatureListener(WorldRenderEventListener.class, feature -> { feature.WorldRenderEvents_AFTER_ENTITIES(context); });
		});

		ClientTickEvents.START_CLIENT_TICK.register(client -> FeatureManager.implementFeatureListener(TickEvent.class, feature -> { feature.tickR(tick); }));
		ClientTickEvents.END_CLIENT_TICK.register(client -> FeatureManager.implementFeatureListener(TickEvent.class, feature -> {
			tick++;
			feature.tickF(tick);
		}));
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> FeatureManager.implementFeatureListener(ClientEventListener.class, ClientEventListener::clientInitialize));
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {FeatureManager.implementFeatureListener(ClientEventListener.class, ClientEventListener::clientShutdown); shutdownClient();});
		ItemTooltipCallback.EVENT.register(((itemStack, tooltipContext, tooltipType, list) -> FeatureManager.implementFeatureListener(RenderTooltip.class, feature -> feature.tooltip(itemStack, tooltipContext, tooltipType, list))));
		HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(MOD_ID,"before_chat"), (context, tickDelta) -> {
			//HudMatrixRegistry.setRenderHUDTickCounter(tickDelta);
			//FeatureManager.implementFeatureListener(RenderHUD.class, feature -> feature.renderHUD(context, tickDelta));
		});
		ClientPlayConnectionEvents.INIT.register((handler, client) -> FeatureManager.implementFeatureListener(ServerConnectionEventListener.class, feature -> feature.serverConnectInit(handler, client)));
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> FeatureManager.implementFeatureListener(ServerConnectionEventListener.class, feature -> feature.serverConnectJoin(handler, sender, client)));
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> FeatureManager.implementFeatureListener(ServerConnectionEventListener.class, feature -> feature.serverConnectDisconnect(handler, client)));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> FeatureManager.implementFeatureListener(RegisterCommandListener.class, feature -> feature.register(dispatcher, registryAccess)));

		log("callback registering complete!");
	}

	public static void sendCommand(String command) {
		assert command.charAt(0) == '/';
		if (Mod.MC.getConnection() != null) {
			Mod.MC.getConnection().sendCommand(command.substring(1));
		}
	}

	private static void message(Component message, int id_color) {
		if (Mod.MC.player == null) return;
		if (message.equals(Component.empty())) {
			rawMessage(message);
			return;
		}
		Mod.MC.execute(() -> {
			Mod.MC.player.displayClientMessage(Component.empty()
				.append(Component.literal(MOD_ID + " ").withColor(id_color))
				.append(Component.literal("᛬ ").withColor(0x9c9c9c))
				.append(message.copy()), false);
		});

	}

	public static void rawMessage(Component message) {
		if (Mod.MC.player == null) return;
		Mod.MC.execute(() -> {
			Mod.MC.player.displayClientMessage(message, false);
		});

	}

	public static void sendPacket(Packet<?> packet) {
		assert Mod.MC.getConnection() != null;
		Mod.MC.getConnection().send(packet);
	}

	public static void message(Component message) {
		message(message, ColorBank.MIA_PURPLE);
	}

	public static void message(String message) {
		message(Component.literal(message), ColorBank.MIA_PURPLE);
	}

	public static void messageError(Component message) {
		message(message.copy().withColor(ColorBank.MC_RED), 0xff695c);
	}
	public static void messageError(String message) {
		messageError(Component.literal(message).withColor(ColorBank.MC_RED));
	}


	public static Screen getCurrentScreen() { return Mod.MC.screen; }
	public static void setCurrentScreen(Screen screen) { Mod.MC.setScreen(screen); }

	public static int getScaledWindowWidth() {
		return Mod.MC.getWindow().getGuiScaledWidth();
	}
	public static int getScaledWindowHeight() {
		return Mod.MC.getWindow().getGuiScaledHeight();
	}

	public static String getPlayerName() { return Mod.MC.getUser().getName(); }
	public static UUID getPlayerUUID() { return Mod.MC.getUser().getProfileId(); }

	public static void log(String msg) { LOGGER.info(msg); }
	public static void warn(String msg) { LOGGER.warn(msg); }
	public static void error(String msg) { LOGGER.error(msg); }
}