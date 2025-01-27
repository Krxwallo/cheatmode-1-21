package io.github.krxwallo.cheatmode.mixins;

import io.github.krxwallo.cheatmode.events.Hooks;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.krxwallo.cheatmode.events.Hooks.mc;

@Mixin(MultiPlayerGameMode.class)
public class GameModeMixin {
    @Inject(at = @At("HEAD"), method = "setLocalMode(Lnet/minecraft/world/level/GameType;)V")
    public void setLocalMode(GameType gameMode, CallbackInfo ci) {
        if (mc().player == null || !gameMode.isCreative()) return;

        if (mc().screen instanceof InventoryScreen) {
            Hooks.open = true;
            mc().forceSetScreen(new CreativeModeInventoryScreen(mc().player, mc().player.connection.enabledFeatures(), mc().options.operatorItemsTab().get()));
        }
    }

    // Render survival hearts when "cheat mode screen" is open
    @Inject(at = @At("HEAD"), method = "canHurtPlayer", cancellable = true)
    public void canHurtPlayer(CallbackInfoReturnable<Boolean> cir) {
        if (((MultiPlayerGameMode)(Object) this).getPlayerMode().isSurvival()) cir.setReturnValue(true);
        else cir.setReturnValue(Hooks.open && mc().screen instanceof CreativeModeInventoryScreen);
    }

    // Render experience bar when "cheat mode screen" is open
    @Inject(at = @At("HEAD"), method = "hasExperience", cancellable = true)
    public void hasExperience(CallbackInfoReturnable<Boolean> cir) {
        if (((MultiPlayerGameMode)(Object) this).getPlayerMode().isSurvival()) cir.setReturnValue(true);
        else cir.setReturnValue(Hooks.open && mc().screen instanceof CreativeModeInventoryScreen);
    }
}
