package dev.vivekraman.tracker.service;

import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.tracker.model.LocalState;
import dev.vivekraman.tracker.model.ServerChecklist;
import dev.vivekraman.tracker.model.ServerState;
import dev.vivekraman.tracker.network.UpdateActiveIdentifierPayload;
import dev.vivekraman.util.state.Registerable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class LocalStateService implements Registerable {
  private LocalState localState;

  public void syncWithServerState(ClientPlayerEntity player, ServerState serverState) {
    String activeIdentifier = serverState.getActiveIdentifiers().get(player.getUuid());

    ServerChecklist serverChecklist = serverState.getChecklists().get(activeIdentifier);
    Map<String, LocalChecklist.CollectionInfo> collectionInfo = new LinkedHashMap<>();
    if (Objects.nonNull(serverChecklist)) {
      serverChecklist.getCollectionInfo().forEach((itemCode, serverCollectionInfo) -> {
      collectionInfo.put(itemCode, LocalChecklist.CollectionInfo.builder()
          .collectedOn(serverCollectionInfo.getCollectedOn())
          .collectedBy(serverCollectionInfo.getCollectedBy())
          .build());
      });
    } else {
      serverChecklist = new ServerChecklist();
    }

    if (Objects.isNull(localState)) {
      localState = new LocalState();
    }
    localState.setIdentifier(activeIdentifier);
    localState.setChecklist(LocalChecklist.builder()
        .identifier(serverChecklist.getIdentifier())
        .updatedOn(serverChecklist.getUpdatedOn())
        .collectionInfo(collectionInfo).build());
  }

  public void persistNewIdentifier(String newIdentifier) {
    ClientPlayNetworking.send(new UpdateActiveIdentifierPayload(
        MinecraftClient.getInstance().player.getUuidAsString(), newIdentifier));
  }
}
