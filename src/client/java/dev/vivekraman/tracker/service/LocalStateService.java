package dev.vivekraman.tracker.service;

import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.tracker.model.LocalState;
import dev.vivekraman.tracker.model.ServerChecklist;
import dev.vivekraman.tracker.model.ServerState;
import dev.vivekraman.util.state.Registerable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class LocalStateService implements Registerable {
  @Getter private LocalState localState;

  public void syncWithServerState(ServerState serverState) {
    if (Objects.isNull(localState.getIdentifier()) || localState.getIdentifier().isEmpty()) {
      return;
    }

    ServerChecklist serverChecklist = serverState.getChecklists().get(localState.getIdentifier());
    Map<String, LocalChecklist.CollectionInfo> collectionInfo = new LinkedHashMap<>();
    serverChecklist.getCollectionInfo().forEach((itemCode, serverCollectionInfo) -> {
      collectionInfo.put(itemCode, LocalChecklist.CollectionInfo.builder()
          .collectedOn(serverCollectionInfo.getCollectedOn())
          .collectedBy(serverCollectionInfo.getCollectedBy())
          .build());
    });

    localState.setChecklist(LocalChecklist.builder()
        .identifier(serverChecklist.getIdentifier())
        .updatedOn(serverChecklist.getUpdatedOn())
        .collectionInfo(collectionInfo).build());
  }
}
