package dev.vivekraman.tracker;

import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.Registerable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ItemEntity;
import org.apache.logging.log4j.Logger;

public class ItemCollectedHandler implements Registerable {
  private static final Logger log = MyLogger.get();

  public void handleItemCollected(ClientPlayerEntity player, ItemEntity collectedItem) {
    log.info("{} collected item {}", player.getName(), collectedItem.getName());
  }
}
