package dev.vivekraman.tracker;

import dev.vivekraman.tracker.api.PersistenceAPI;
import dev.vivekraman.tracker.model.Operation;
import dev.vivekraman.tracker.model.OperationType;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import dev.vivekraman.util.state.Registerable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ItemEntity;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

public class ItemCollectedHandler implements Registerable {
  private static final Logger log = MyLogger.get();

  private PersistenceAPI persistenceAPI;

  @Override
  public void init() throws Exception {
    Registerable.super.init();
    persistenceAPI = ClassRegistry.supply(PersistenceAPI.class);
  }

  public void handleItemCollected(ClientPlayerEntity player, ItemEntity collectedItem) {
    log.info("{} collected item {}", player.getName(), collectedItem.getName());

    // TODO: push to operation queue

    List<Operation> list = List.of(Operation.builder()
        .itemCode(collectedItem.getDisplayName().getString())
        .type(OperationType.REGISTER)
        .identifier("from-the-mod-dev")
        .collectedOn(new Date()).build());
    persistenceAPI.persistOperations(list, () -> log.info("API success, {}", list));
  }
}
