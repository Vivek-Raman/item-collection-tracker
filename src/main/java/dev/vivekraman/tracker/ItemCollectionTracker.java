package dev.vivekraman.tracker;

import dev.vivekraman.tracker.network.ItemCollectedPayload;
import dev.vivekraman.tracker.service.OperationService;
import dev.vivekraman.tracker.service.PlayerService;
import dev.vivekraman.util.Constants;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.apache.logging.log4j.Logger;

public class ItemCollectionTracker implements ModInitializer {
  public static final Logger log = MyLogger.get();

  @Override
  public void onInitialize() {
    PayloadTypeRegistry.playC2S().register(ItemCollectedPayload.ID, ItemCollectedPayload.CODEC);

    try {
      ClassRegistry.init(log);
      ClassRegistry.register(new PlayerService());
      ClassRegistry.register(new OperationService());
    } catch (Exception e) {
      log.error("Failed to register classes! ", e);
    }
    log.info("{} initialized on the server!", Constants.MOD_ID);
  }
}
