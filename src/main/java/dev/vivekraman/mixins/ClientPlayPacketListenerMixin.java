package dev.vivekraman.mixins;

import dev.vivekraman.util.logging.MyLogger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.registry.Registries;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// https://github.com/FabricMC/fabric/issues/1130#issuecomment-1598880692
@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayPacketListenerMixin {
    private static final Logger log = MyLogger.get();

    @Inject(method = "onItemPickupAnimation", at = @At("TAIL"))
    public void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet, CallbackInfo ci) {
        log.info("does this work, {} || {}", packet.getEntityId(),
                Registries.ITEM.get(packet.getEntityId()).getName().getString());
    }
}
