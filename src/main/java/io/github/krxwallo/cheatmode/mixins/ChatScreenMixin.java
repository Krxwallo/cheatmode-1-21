package io.github.krxwallo.cheatmode.mixins;

import io.github.krxwallo.cheatmode.CheatMode;
import io.github.krxwallo.cheatmode.options.CheatModeOptions;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.krxwallo.cheatmode.events.Hooks.mc;


@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(at = @At("TAIL"), method = "init()V")
    public void init(CallbackInfo ci) {
        var screen = (ChatScreen) (Object) this;
        if (screen instanceof InBedChatScreen) return;

        var width = screen.width;
        var height = screen.height;

        int i = 0;
        for (OptionInstance<?> option : new OptionInstance[]{CheatModeOptions.instantCreativeInventory, CheatModeOptions.flight, CheatModeOptions.blockReach, CheatModeOptions.interactionReach}) {
            int j = width / 5 * 3;
            int k = height / 10 - 12 + 24 * i;
            var button = option.createButton(mc().options, j, k, 150);
            screen.renderables.add(button);
            addWidget(screen, button);
            ++i;
        }
    }

    private static <T extends GuiEventListener & NarratableEntry> void addWidget(Screen screen, T button) {
        try {
            var field = Screen.class.getDeclaredField("narratables");
            field.setAccessible(true);
            //noinspection unchecked
            ((List<NarratableEntry>) field.get(screen)).add(button);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //noinspection unchecked
        ((List<GuiEventListener>) screen.children()).add(button);
    }
}