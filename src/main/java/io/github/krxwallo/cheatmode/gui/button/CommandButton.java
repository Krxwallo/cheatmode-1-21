package io.github.krxwallo.cheatmode.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;

public class CommandButton extends Button {
    private final String command;
    private final Minecraft minecraft = Minecraft.getInstance();

    public CommandButton(int x, int y, int width, int height, String message, String command) {
        super(x, y, width, height, MutableComponent.create(new PlainTextContents.LiteralContents(message)), b -> {}, DEFAULT_NARRATION);
        this.command = command;
    }

    @Override
    public void onPress() {
        if (minecraft.player == null) return;
        enableCheats();
        minecraft.player.connection.sendCommand(command);
    }

    private void enableCheats() {
        if (!minecraft.hasSingleplayerServer()) return;
        IntegratedServer server = minecraft.getSingleplayerServer();
        assert server != null;
        server.getPlayerList().setAllowCommandsForAllPlayers(true);
        if (minecraft.player == null) return;
        minecraft.player.setPermissionLevel(4);
    }
}
