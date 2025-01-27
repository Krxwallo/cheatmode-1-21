package io.github.krxwallo.cheatmode.events;

import io.github.krxwallo.cheatmode.config.Config;
import io.github.krxwallo.cheatmode.gui.button.ItemButton;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

import javax.annotation.Nullable;
import javax.sound.midi.SysexMessage;
import java.util.List;

@EventBusSubscriber
public final class Hooks {
    private static ItemButton button;

    private static GameType previousGameMode = GameType.SURVIVAL;
    public static boolean open = false; // "cheat mode screen" open?
    public static Minecraft mc() { return Minecraft.getInstance(); }
    public static boolean singleplayer() { return mc().isLocalServer(); }

    @Nullable
    private static ServerPlayer serverPlayer() {
        if (mc().player == null) return null;
        if (mc().getSingleplayerServer() == null) return null;

        return mc().getSingleplayerServer().getPlayerList().getPlayer(mc().player.getUUID());
    }

    private static void creative() {
        if (mc().player == null) return;
        if (mc().gameMode == null) return;
        if (mc().gameMode.getPlayerMode().isCreative()) return;

        previousGameMode = mc().gameMode.getPlayerMode();

        if (singleplayer()) {
            var player = serverPlayer();
            if (player == null) return;

            player.setGameMode(GameType.CREATIVE);
        }
        else {
            // Server -> require op
            if (!mc().player.hasPermissions(2)) mc().player.sendSystemMessage(Component.translatable("message.cheatmode.no_permissions").withStyle(ChatFormatting.RED));
            else mc().player.connection.sendCommand("gamemode creative");
        }
    }

    public static void onScreenInit(Screen screen) {
        if (screen instanceof InventoryScreen invScreen) {
            if (Config.CLIENT.instantCreativeInventory.get()) creative();
            else {
                button = new ItemButton(invScreen.getGuiLeft() + 77, invScreen.getGuiTop() + 30, b -> creative());
                addButton(invScreen, button);
            }
        }
    }

    private static void addButton(Screen screen, Button button) {
        screen.renderables.add(button);
        //noinspection unchecked
        ((List<GuiEventListener>) screen.children()).add(button);
    }

    public static void onScreenClose(Screen screen) {
        if (mc().player == null) return;

        if (screen instanceof CreativeModeInventoryScreen) {
            if (!open) return;
            open = false;

            if (singleplayer()) {
                var player = serverPlayer();
                if (player == null) return;
                if (mc().gameMode == null || mc().gameMode.getPreviousPlayerMode() == null) return;

                player.setGameMode(previousGameMode);
            }
            else {
                // Server -> require op
                if (!mc().player.hasPermissions(2)) mc().player.sendSystemMessage(Component.translatable("message.cheatmode.no_permissions").withStyle(ChatFormatting.RED));
                else mc().player.connection.sendCommand("gamemode " + previousGameMode.name().toLowerCase());
            }
        }
    }

    @SubscribeEvent
    public static void onScreenDrawPost(ScreenEvent.Render.Post event) {

        if (event.getScreen() instanceof InventoryScreen screen && button != null) {
            if(button.isMouseOver(event.getMouseX(), event.getMouseY())) {
                var tooltip = Component.translatable("gui.cheatmode.open_creative_inventory").withStyle(ChatFormatting.GOLD);
                screen.setTooltipForNextRenderPass(List.of(tooltip.getVisualOrderText()));
            }
        }
    }
}
