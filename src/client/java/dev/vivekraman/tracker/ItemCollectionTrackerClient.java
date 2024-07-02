package dev.vivekraman.tracker;

import dev.vivekraman.tracker.model.LocalState;
import dev.vivekraman.tracker.network.SyncStatePayload;
import dev.vivekraman.tracker.persistence.ServerPersistence;
import dev.vivekraman.tracker.screen.ChecklistScreenProvider;
import dev.vivekraman.tracker.screen.UIProvider;
import dev.vivekraman.tracker.service.ItemCollectedHandler;
import dev.vivekraman.tracker.service.LocalStateService;
import dev.vivekraman.util.Constants;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class ItemCollectionTrackerClient implements ClientModInitializer {
  public static final Logger log = MyLogger.get();

  @Override
  public void onInitializeClient() {
    ClassRegistry.initClient(log);
    try {
//      ClassRegistry.register(new PersistenceAPI());
      ClassRegistry.registerClient(new LocalStateService(new LocalState()));
      ClassRegistry.registerClient(new ItemCollectedHandler());

      ClassRegistry.registerClient(new ChecklistScreenProvider());
      ClassRegistry.registerClient(new UIProvider());
    } catch (Exception e) {
      log.error("Failed to register classes! ", e);
    }

    ClientPlayNetworking.registerGlobalReceiver(SyncStatePayload.ID, ((payload, context) -> {
      ServerPersistence persistence = ServerPersistence.readNbt(payload.persistenceTag());
      ClassRegistry.supplyClient(LocalStateService.class).syncWithServerState(
          context.player(), persistence.getServerState());
    }));

    log.info("{} initialized on the client!", Constants.MOD_ID);
  }
}
