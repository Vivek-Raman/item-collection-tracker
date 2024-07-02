package dev.vivekraman.tracker.service;

import dev.vivekraman.tracker.network.UpdateActiveIdentifierPayload;
import dev.vivekraman.tracker.persistence.ServerPersistence;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import dev.vivekraman.util.state.Registerable;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class PlayerService implements Registerable {
  private final Logger log = MyLogger.get();

  private StateService stateService;

  @Override
  public void init() throws Exception {
    Registerable.super.init();
    stateService = ClassRegistry.supply(StateService.class);

    registerUpdateActiveIdentifierListener();
  }

  private void registerUpdateActiveIdentifierListener() {
    ServerPlayNetworking.registerGlobalReceiver(UpdateActiveIdentifierPayload.ID, ((payload, context) -> {
      ClassRegistry.supply(PlayerService.class).updateActiveIdentifier(
          context.player(), payload.playerUUID(), payload.activeIdentifier());
    }));
  }

  public void notifyNewItemCollected(String itemCode, ServerPlayerEntity player) {
    player.sendMessage(Text.literal("You collected a " + itemCode + "!"));
  }

  public void updateActiveIdentifier(ServerPlayerEntity player, String playerUUID, String activeIdentifier) {
    ServerPersistence persistence = ServerPersistence.loadFromServer(player.getServer());
    persistence.getServerState().getActiveIdentifiers().put(UUID.fromString(playerUUID), activeIdentifier);
    persistence.markDirty();
    stateService.pushStateToClient(persistence, player);
  }
}
