package mia.modmod.features;

import mia.modmod.features.listeners.AbstractEventListener;
import mia.modmod.features.listeners.impl.*;

import java.util.List;

public enum FeatureListener {
    CLIENT_EVENT_LISTENER(ClientEventListener.class),
    COMMAND_REGISTRATION(RegisterCommandListener.class),
    MODE_SWITCH_EVENT_LISTENER(ModeSwitchEventListener.class),
    PACKET_LISTENER(PacketListener.class),
    CHAT_EVENT_LISTENER(ChatEventListener.class),
    RENDER_HUD(RenderHUD.class),
    RENDER_TOOLTIP(RenderTooltip.class),
    SERVER_CONNECTION_EVENT_LISTENER(ServerConnectionEventListener.class),
    TICK_EVENT(TickEvent.class),
    REGISTER_KEY_BIND_EVENT(RegisterKeyBindEvent.class);

    private final Class<? extends AbstractEventListener> identifier;

    <T extends AbstractEventListener> FeatureListener(Class<T> identifier) { this.identifier = identifier; }

    public Class<? extends AbstractEventListener> getIdentifier() { return identifier; }

    public static List<FeatureListener> getFeatureIdentifiers() { return List.of(FeatureListener.values()); }
}

