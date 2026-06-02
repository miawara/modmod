package mia.modmod.features.impl.internal.mode;

import mia.modmod.Mod;
import mia.modmod.core.NaturalOrderStringIntegerComparator;
import mia.modmod.features.Categories;
import mia.modmod.features.Feature;
import mia.modmod.features.FeatureManager;
import mia.modmod.features.listeners.DFMode;
import mia.modmod.features.listeners.ModifiableEventData;
import mia.modmod.features.listeners.ModifiableEventResult;
import mia.modmod.features.listeners.impl.ChatEventListener;
import mia.modmod.features.listeners.impl.ModeSwitchEventListener;
import mia.modmod.features.listeners.impl.PacketListener;
import mia.modmod.features.listeners.impl.ServerConnectionEventListener;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class LocationAPI extends Feature implements ChatEventListener, ServerConnectionEventListener, PacketListener {
    private static DFMode mode = DFMode.NONE;
    private static final Pattern SPAWN_ACTIONBAR_PATTERN = Pattern.compile("(⏵+ - )?⧈ -?\\d+ Tokens {2}ᛥ -?\\d+ Tickets {2}⚡ -?\\d+ Sparks"); // copied from WeCode : zbinfinn

    private static int serverListCommandCompletionRequestPacketID = 10000000;
    private static ArrayList<String> nodeStringList;
    private static LinkedHashMap<NodeCategory, ArrayList<String>> nodes;



    public LocationAPI(Categories category) {
        super(category, "LocationAPI", "locapi", "Tracks state and location across diamondfire");
    }


    public static DFMode getMode() { return mode; }

    private static void modeSwitch(DFMode newMode) {
        FeatureManager.implementFeatureListener(ModeSwitchEventListener.class, feature -> feature.onModeSwitch(newMode, mode));
        mode = newMode;
    }

    public static @NotNull Optional<ArrayList<String>> getRawNodeList() { return Optional.ofNullable(nodeStringList); }
    public static @NotNull Optional<LinkedHashMap<NodeCategory, ArrayList<String>>> getNodeCategories() { return Optional.ofNullable(nodes); }

    @Override
    public ModifiableEventResult<Component> chatEvent(ModifiableEventData<Component> message, CallbackInfo ci) {
        String content = message.base().getString();

        Matcher matcher;

        matcher = Pattern.compile("^» Joined game: (.*) by (.{3,16})\\.").matcher(content);
        if (matcher.find()) {
            modeSwitch(DFMode.PLAY);
        }

        matcher = Pattern.compile("» You are now in dev mode\\.").matcher(content);
        if (matcher.find()) {
            modeSwitch(DFMode.DEV);
        }

        matcher = Pattern.compile("^» You are now in build mode\\.").matcher(content);
        if (matcher.find()) {
            modeSwitch(DFMode.BUILD);
        }

        matcher = Pattern.compile("^» Sending you to ").matcher(content);
        if (matcher.find()) {
            modeSwitch(DFMode.NONE);
        }

        matcher = Pattern.compile("^» You are now spectating this plots code! Other people cannot see you and this action has been logged\\. Do /spawn to exit\\.").matcher(content);
        if (matcher.find()) {
            modeSwitch(DFMode.CODE_SPECTATE);
            ci.cancel();
        }

        return message.pass();
    }


    @Override
    public void DFConnectJoin(ClientPacketListener networkHandler) {
        mode = DFMode.SPAWN;

        // request node list
        serverListCommandCompletionRequestPacketID++;
        Mod.sendPacket(new ServerboundCommandSuggestionPacket(serverListCommandCompletionRequestPacketID, "/server "));
    }

    @Override
    public void receivePacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ClientboundCommandSuggestionsPacket commandSuggestionsPacket) {
            if (commandSuggestionsPacket.id() != serverListCommandCompletionRequestPacketID) return;
            nodeStringList = commandSuggestionsPacket.suggestions().stream().map(ClientboundCommandSuggestionsPacket.Entry::text).collect(Collectors.toCollection(ArrayList::new));
            nodes = new LinkedHashMap<>();
            ArrayList<String> remainingNodes = new ArrayList<>(nodeStringList);
            for (NodeCategory nodeCategory : NodeCategory.values()) {
                ArrayList<String> totalMatchedNodes = new ArrayList<>();

                for (String matcher : nodeCategory.getMatchers()) {
                    ArrayList<String> matchedNodes = remainingNodes.stream().filter(nodeID -> Pattern.matches(matcher, nodeID)).collect(Collectors.toCollection(ArrayList::new));
                    matchedNodes.sort(new NaturalOrderStringIntegerComparator<>());
                    totalMatchedNodes.addAll(matchedNodes);
                    remainingNodes.removeAll(matchedNodes);
                }

                nodes.put(nodeCategory, totalMatchedNodes);
            }
        }

        if (Mod.MC.player != null) {
            Matcher matcher;
            if (packet instanceof ClientboundSetActionBarTextPacket(Component text)) {
                matcher = SPAWN_ACTIONBAR_PATTERN.matcher(text.getString());
                if (matcher.find()) {
                    mode = DFMode.SPAWN;
                }
            }
        }
    }

    @Override
    public void DFConnectDisconnect(ClientPacketListener networkHandler) {
        mode = DFMode.NONE;
    }

    @Override
    public void sendPacket(Packet<?> packet, CallbackInfo ci) {

    }

    @Override
    public void serverConnectInit(ClientPacketListener networkHandler, Minecraft minecraftServer) {

    }

    @Override
    public void serverConnectJoin(ClientPacketListener networkHandler, PacketSender sender, Minecraft minecraftServer) {

    }

    @Override
    public void serverConnectDisconnect(ClientPacketListener networkHandler, Minecraft minecraftServer) {

    }

}
