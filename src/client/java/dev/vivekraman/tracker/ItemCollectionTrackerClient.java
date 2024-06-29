package dev.vivekraman.tracker;

import dev.vivekraman.tracker.service.ItemCollectedHandler;
import dev.vivekraman.util.Constants;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
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
      ClassRegistry.registerClient(new ItemCollectedHandler());
    } catch (Exception e) {
      log.error("Failed to register classes! ", e);
    }

//    ClientPlayNetworking.registerGlobalReceiver(LocalPersistencePacketPayload.ID, ((payload, context) -> {
//      LocalPersistence persistence = LocalPersistence.readNbt(payload.persistenceTag());
//      log.info("Client received persistence {}", persistence.getLocalState());
//    }));

    log.info("{} initialized on the client!", Constants.MOD_ID);
  }
}
