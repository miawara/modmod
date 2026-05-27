package mia.modmod.features.impl.support;

import mia.modmod.ColorBank;
import mia.modmod.features.Categories;
import mia.modmod.features.Feature;
import mia.modmod.features.listeners.ModifiableEventData;
import mia.modmod.features.listeners.ModifiableEventResult;
import mia.modmod.features.listeners.impl.ChatEventListener;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SessionSpyMessages extends Feature implements ChatEventListener {
    public SessionSpyMessages(Categories category) { super(category, "Session Spy", "session_spy", "Makes session spy messages readable"); }

    @Override
    public ModifiableEventResult<Component> chatEvent(ModifiableEventData<Component> message, CallbackInfo ci) {
        Matcher matcher = Pattern.compile("^\\* (\\[.*])*([a-zA-Z0-9_]{3,16}): (.*)").matcher(message.base().getString());
        if (matcher.find()) {
            String name = matcher.group(2);
            String text = matcher.group(3);
            return message.modified(
                    Component.empty()
                    .append(Component.literal("[SPY] ").withColor(0x80ffcc))
                    .append(Component.literal(name + ": " + text).withColor(ColorBank.MC_GRAY))
            );
        }
        return message.pass();
    }
}
