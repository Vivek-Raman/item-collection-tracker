package dev.vivekraman.tracker.service;

import dev.vivekraman.tracker.api.model.Operation;
import dev.vivekraman.tracker.api.model.OperationType;
import dev.vivekraman.tracker.network.ItemCollectedPayload;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.Registerable;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ItemEntity;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

public class ItemCollectedHandler implements Registerable {
  private static final Logger log = MyLogger.get();


  @Override
  public void init() throws Exception {
    Registerable.super.init();
  }

  public void handleItemCollected(ClientPlayerEntity player, ItemEntity collectedItem) {
    List<Operation> list = List.of(Operation.builder()
        .itemCode(collectedItem.getStack().getName().getString())
        .type(OperationType.REGISTER)
        .identifier("from-the-mod-dev")
        .collectedBy(player.getNameForScoreboard())
        .collectedOn(new Date()).build());
    ClientPlayNetworking.send(new ItemCollectedPayload(list.getFirst()));
  }
}
