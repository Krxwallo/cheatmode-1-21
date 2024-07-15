package io.github.krxwallo.cheatmode.mixins;

import io.github.krxwallo.cheatmode.config.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {
    // reach distance = block placing, breaking, interactions
    // attack range = attacking entities

    // todo!
//
//    @Inject(at = @At("HEAD"), method = "getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D", locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
//    public void getAttributeValue(Attribute attribute, CallbackInfoReturnable<Double> cir) {
//        if (!Config.CLIENT_SPEC.isLoaded()) return;
//
//        if (attribute == NeoForgeMod.ENTITY_REACH.orElse(null) || attribute == ForgeMod.BLOCK_REACH.orElse(null))
//            cir.setReturnValue(Config.CLIENT.reach.get());
//    }
}
