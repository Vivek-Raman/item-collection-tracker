package dev.vivekraman.tracker.service;

import dev.vivekraman.tracker.api.model.Operation;
import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.tracker.persistence.LocalPersistence;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.Registerable;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OperationService implements Registerable {
  private final Logger log = MyLogger.get();

  public void persistOperation(LocalPersistence persistence, Operation operation) {
    boolean dirty = false;
    Date now = new Date();
    LocalChecklist checklist;

    if (Objects.isNull(persistence.getLocalState().getChecklists())) {
      dirty = true;
      persistence.getLocalState().setChecklists(new LinkedHashMap<>());
    }

    if (!persistence.getLocalState().getChecklists().containsKey(operation.getIdentifier())) {
      dirty = true;
      checklist = LocalChecklist.builder()
          .identifier(operation.getIdentifier())
          .updatedOn(now)
          .collectionInfo(new LinkedHashMap<>())
          .build();
      persistence.getLocalState().getChecklists().put(operation.getIdentifier(), checklist);
    } else {
      checklist = persistence.getLocalState().getChecklists().get(operation.getIdentifier());
    }

    Map<String, LocalChecklist.CollectionInfo> collectionInfo = checklist.getCollectionInfo();
    if (!collectionInfo.containsKey(operation.getItemCode())) {
      dirty = true;
      collectionInfo.put(operation.getItemCode(), LocalChecklist.CollectionInfo.builder()
          .collectedBy(operation.getCollectedBy())
          .collectedOn(operation.getCollectedOn())
          .build());
      checklist.setUpdatedOn(now);
    }

    persistence.setDirty(dirty);
    log.info("Persisted operation {}, dirty {} to State", operation, dirty);
  }
}
