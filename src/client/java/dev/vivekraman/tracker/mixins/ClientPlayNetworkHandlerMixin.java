package dev.vivekraman.tracker.mixins;

import dev.vivekraman.tracker.service.ItemCollectedHandler;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements TickablePacketListener, ClientPlayPacketListener {
  private static final Logger log = MyLogger.get();

  @Shadow
  private ClientWorld world;

  @Inject(method = "onItemPickupAnimation", at = @At("HEAD"))
  public void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet, CallbackInfo ci) {
    MinecraftClient client = MinecraftClient.getInstance();
    NetworkThreadUtils.forceMainThread(packet, this, client);

    ClientPlayerEntity player = client.player;
    if (Objects.isNull(player) || player.getId() != packet.getCollectorEntityId()) {
      return;
    }

    Entity entity = this.world.getEntityById(packet.getEntityId());
    if (entity instanceof ItemEntity collectedItem) {
      ItemCollectedHandler handler = ClassRegistry.supplyClient(ItemCollectedHandler.class);
      handler.handleItemCollected(client.player, collectedItem);
    }
  }
}
