package dev.vivekraman.tracker.service;

import dev.vivekraman.tracker.network.ItemCollectedPayload;
import dev.vivekraman.tracker.network.SyncStatePayload;
import dev.vivekraman.tracker.network.UpdateActiveIdentifierPayload;
import dev.vivekraman.util.state.Registerable;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class NetworkService implements Registerable {
  @Override
  public void init() throws Exception {
    Registerable.super.init();

    PayloadTypeRegistry.playC2S().register(ItemCollectedPayload.ID, ItemCollectedPayload.CODEC);
    PayloadTypeRegistry.playC2S().register(UpdateActiveIdentifierPayload.ID, UpdateActiveIdentifierPayload.CODEC);
    PayloadTypeRegistry.playS2C().register(SyncStatePayload.ID, SyncStatePayload.CODEC);
  }
}
