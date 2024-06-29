package dev.vivekraman.tracker.persistence;

import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.tracker.model.LocalState;
import dev.vivekraman.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.*;

@Getter
@AllArgsConstructor
public class LocalPersistence extends PersistentState {
  private LocalState localState;

  public LocalPersistence() {
    this.localState = new LocalState(new HashMap<>());
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    NbtCompound stateTag = convertToNbt(localState);
    nbt.put(Constants.MOD_ID, stateTag);
    return nbt;
  }

  public static NbtCompound convertToNbt(LocalState localState) {
    NbtCompound stateTag = new NbtCompound();

    Optional.ofNullable(localState.getChecklists()).orElseGet(Collections::emptyMap).forEach((identifier, checklist) -> {
      NbtCompound checklistTag = new NbtCompound();
      checklistTag.putString("identifier", checklist.getIdentifier());
      checklist.getCollectionInfo().forEach((itemCode, collectionInfo) -> {
        NbtCompound collectionInfoTag = new NbtCompound();
        collectionInfoTag.putString("collectedBy", collectionInfo.getCollectedBy());
        collectionInfoTag.putLong("collectedOn", collectionInfo.getCollectedOn().getTime());
        checklistTag.put(itemCode, collectionInfoTag);
      });

      stateTag.put(checklist.getIdentifier(), checklistTag);
    });

    return stateTag;
  }

  public static LocalPersistence readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
    return readNbt(tag);
  }

  public static LocalPersistence readNbt(NbtCompound tag) {
    LocalState incomingState = new LocalState();

    NbtCompound stateTag = tag.getCompound(Constants.MOD_ID);
    Set<String> identifiers = stateTag.getKeys();

    identifiers.forEach(identifier -> {
      NbtCompound checklistTag = stateTag.getCompound(identifier);
      LocalChecklist checklist = new LocalChecklist();
      checklist.setIdentifier(identifier);
      checklist.setUpdatedOn(new Date(checklistTag.getLong("updatedOn")));
      // TODO: validate updatedOn before overwriting local state

      NbtCompound collectionsTag = checklistTag.getCompound("collectionInfo");
      Map<String, LocalChecklist.CollectionInfo> collectionInfoMap = new LinkedHashMap<>();
      collectionsTag.getKeys().forEach(itemCode -> {
        NbtCompound collectionInfoTag = stateTag.getCompound(itemCode);
        LocalChecklist.CollectionInfo collectionInfo = new LocalChecklist.CollectionInfo();
        collectionInfo.setCollectedBy(collectionInfoTag.getString("collectedBy"));
        collectionInfo.setCollectedOn(new Date(collectionInfoTag.getLong("collectedOn")));
        checklist.getCollectionInfo().putIfAbsent(itemCode, collectionInfo);
      });
      checklist.setCollectionInfo(collectionInfoMap);
    });

    return new LocalPersistence(incomingState);
  }

  public static LocalPersistence loadFromServer(MinecraftServer server) {
    PersistentStateManager stateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
    LocalPersistence localPersistence = stateManager.getOrCreate(
        new Type<LocalPersistence>(LocalPersistence::new, LocalPersistence::readNbt, null),
        Constants.MOD_ID);
    return localPersistence;
  }
}
