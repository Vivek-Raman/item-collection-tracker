package dev.vivekraman.tracker.service;

import dev.vivekraman.tracker.api.model.Operation;
import dev.vivekraman.tracker.api.model.OperationType;
import dev.vivekraman.tracker.network.ItemCollectedPayload;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import dev.vivekraman.util.state.Registerable;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.registry.Registries;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class ItemCollectedHandler implements Registerable {
  private static final Logger log = MyLogger.get();

  private LocalStateService localStateService;

  @Override
  public void init() throws Exception {
    Registerable.super.init();
    localStateService = ClassRegistry.supplyClient(LocalStateService.class);
  }

  public void handleItemCollected(ClientPlayerEntity player, ItemEntity collectedItem) {
    String activeIdentifier = localStateService.getLocalState().getIdentifier();
    if (StringUtils.isBlank(activeIdentifier)) {
      return;
    }

    Operation operation = Operation.builder()
        .itemCode(Registries.ITEM.getId(collectedItem.getStack().getItem()).toString())
        .type(OperationType.REGISTER)
        .identifier(activeIdentifier)
        .collectedBy(player.getNameForScoreboard())
        .collectedOn(new Date()).build();
    ClientPlayNetworking.send(new ItemCollectedPayload(operation));
  }
}
