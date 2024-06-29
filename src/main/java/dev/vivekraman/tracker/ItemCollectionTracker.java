package dev.vivekraman.tracker;

import dev.vivekraman.tracker.persistence.LocalPersistence;
import dev.vivekraman.tracker.network.ItemCollectedPayload;
import dev.vivekraman.tracker.service.OperationService;
import dev.vivekraman.util.Constants;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.apache.logging.log4j.Logger;

public class ItemCollectionTracker implements ModInitializer {
  public static final Logger log = MyLogger.get();

  @Override
  public void onInitialize() {
    PayloadTypeRegistry.playC2S().register(ItemCollectedPayload.ID, ItemCollectedPayload.CODEC);

    ServerPlayNetworking.registerGlobalReceiver(ItemCollectedPayload.ID, ((payload, context) -> {
      log.info("{} received from client!", payload.operation());
      LocalPersistence persistence = LocalPersistence.loadFromServer(context.player().getServer());
      ClassRegistry.supply(OperationService.class).persistOperation(persistence, payload.operation());
    }));

    try {
      ClassRegistry.init(log);
      ClassRegistry.register(new OperationService());
    } catch (Exception e) {
      log.error("Failed to register classes! ", e);
    }
    log.info("{} initialized on the server!", Constants.MOD_ID);
  }
}
