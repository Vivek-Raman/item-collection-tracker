package dev.vivekraman.tracker;

import dev.vivekraman.tracker.config.TrackerConfig;
import dev.vivekraman.tracker.config.TrackerConfigProvider;
import dev.vivekraman.tracker.service.ItemCollectedHandler;
import dev.vivekraman.util.Constants;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class ItemCollectionTrackerClient implements ClientModInitializer {
  public static final Logger log = MyLogger.get();

  @Override
  public void onInitializeClient() {
    ClassRegistry.initClient(log);
    try {
//      ClassRegistry.register(new PersistenceAPI());
      ClassRegistry.registerClient(new TrackerConfigProvider());
      ClassRegistry.registerClient(new ItemCollectedHandler());

    } catch (Exception e) {
      log.error("Failed to register classes! ", e);
    }

    log.info("{} initialized on the client!", Constants.MOD_ID);
  }
}
