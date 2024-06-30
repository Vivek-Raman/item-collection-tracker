package dev.vivekraman.tracker.service;

import dev.vivekraman.tracker.network.SyncStatePayload;
import dev.vivekraman.tracker.persistence.ServerPersistence;
import dev.vivekraman.util.state.Registerable;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class StateService implements Registerable {
  @Override
  public void init() throws Exception {
    Registerable.super.init();

    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      pushStateToClient(handler.getPlayer());
    });
  }

  public void pushStateToClient(ServerPlayerEntity player) {
    ServerPersistence persistence = ServerPersistence.loadFromServer(player.getServer());
    ServerPlayNetworking.send(player, new SyncStatePayload(
        ServerPersistence.convertToNbt(persistence.getServerState())));
  }
}
