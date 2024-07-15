package io.github.krxwallo.cheatmode.mixins;

import io.github.krxwallo.cheatmode.config.Config;
import io.github.krxwallo.cheatmode.events.Hooks;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;flying:Z", opcode = Opcodes.GETFIELD), method = "aiStep")
    public boolean mayfly(Abilities instance) {
        if (Hooks.mc().gameMode == null) return false;
        return Hooks.mc().gameMode.getPlayerMode().isCreative() || Config.CLIENT.flight.get();
    }
}
