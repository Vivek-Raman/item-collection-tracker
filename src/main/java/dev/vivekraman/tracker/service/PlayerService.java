package dev.vivekraman.tracker.service;

import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.Registerable;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;

public class PlayerService implements Registerable {
  private final Logger log = MyLogger.get();

  public void notifyNewItemCollected(ServerPlayerEntity player, String itemCode) {
    player.sendMessage(Text.literal("You collected a " + itemCode + "!"));
  }
}
