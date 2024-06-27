package dev.vivekraman;

import dev.vivekraman.tracker.ItemCollectedHandler;
import dev.vivekraman.tracker.api.PersistenceAPI;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class ItemCollectionTracker implements ClientModInitializer {
  public static final Logger log = MyLogger.get();

  @Override
  public void onInitializeClient() {
    ClassRegistry.init(log);
    try {
      ClassRegistry.register(new PersistenceAPI());
      ClassRegistry.register(new ItemCollectedHandler());
    } catch (Exception e) {
      log.error("Failed to register classes! ", e);
    }
  }
}
