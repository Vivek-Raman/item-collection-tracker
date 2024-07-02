package dev.vivekraman.tracker.persistence;

import dev.vivekraman.tracker.model.ServerChecklist;
import dev.vivekraman.tracker.model.ServerState;
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
public class ServerPersistence extends PersistentState {
  private ServerState serverState;

  public ServerPersistence() {
    this.serverState = new ServerState(new HashMap<>(), new HashMap<>());
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    return convertToNbt(serverState);
  }

  public static NbtCompound convertToNbt(ServerState serverState) {
    NbtCompound checklistsTag = new NbtCompound();
    Optional.ofNullable(serverState.getChecklists()).orElseGet(Collections::emptyMap).forEach((identifier, checklist) -> {
      NbtCompound checklistTag = new NbtCompound();
      checklistTag.putString("identifier", checklist.getIdentifier());
      checklistTag.putLong("updatedOn", checklist.getUpdatedOn().getTime());
      NbtCompound collectionTag = new NbtCompound();
      checklist.getCollectionInfo().forEach((itemCode, collectionInfo) -> {
        NbtCompound collectionInfoTag = new NbtCompound();
        collectionInfoTag.putString("collectedBy", collectionInfo.getCollectedBy());
        collectionInfoTag.putLong("collectedOn", collectionInfo.getCollectedOn().getTime());
        collectionTag.put(itemCode, collectionInfoTag);
      });

      checklistTag.put("collectionInfo", collectionTag);
      checklistsTag.put(checklist.getIdentifier(), checklistTag);
    });

    NbtCompound activeIdentifiersTag = new NbtCompound();
    Optional.ofNullable(serverState.getActiveIdentifiers()).orElseGet(Collections::emptyMap).forEach((uuid, identifier) -> {
      activeIdentifiersTag.putString(uuid.toString(), identifier);
    });
    NbtCompound stateTag = new NbtCompound();
    stateTag.put("checklists", checklistsTag);
    stateTag.put("activeIdentifiers", activeIdentifiersTag);
    NbtCompound tag = new NbtCompound();
    tag.put(Constants.MOD_ID, stateTag);
    return tag;
  }

  public static ServerPersistence readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
    return readNbt(tag);
  }

  public static ServerPersistence readNbt(NbtCompound tag) {
    NbtCompound stateTag = tag.getCompound(Constants.MOD_ID);
    Map<UUID, String> activeIdentifiers = new LinkedHashMap<>();
    NbtCompound activeIdentifiersTag = stateTag.getCompound("activeIdentifiers");
    activeIdentifiersTag.getKeys().forEach((uuid) ->
        activeIdentifiers.put(UUID.fromString(uuid), activeIdentifiersTag.getString(uuid)));

    NbtCompound checklistsTag = stateTag.getCompound("checklists");
    Map<String, ServerChecklist> checklists = new LinkedHashMap<>();
    checklistsTag.getKeys().forEach(identifier -> {
      NbtCompound checklistTag = checklistsTag.getCompound(identifier);
      ServerChecklist checklist = ServerChecklist.builder()
          .identifier(identifier)
          .updatedOn(new Date(checklistTag.getLong("updatedOn")))
          .collectionInfo(new LinkedHashMap<>())
          .build();

      NbtCompound collectionsTag = checklistTag.getCompound("collectionInfo");
      Map<String, ServerChecklist.CollectionInfo> collectionInfoMap = new LinkedHashMap<>();
      collectionsTag.getKeys().forEach(itemCode -> {
        NbtCompound collectionInfoTag = stateTag.getCompound(itemCode);
        ServerChecklist.CollectionInfo collectionInfo = new ServerChecklist.CollectionInfo();
        collectionInfo.setCollectedBy(collectionInfoTag.getString("collectedBy"));
        collectionInfo.setCollectedOn(new Date(collectionInfoTag.getLong("collectedOn")));
        collectionInfoMap.putIfAbsent(itemCode, collectionInfo);
      });

      checklist.setCollectionInfo(collectionInfoMap);
      checklists.put(identifier, checklist);
    });

    return new ServerPersistence(new ServerState(activeIdentifiers, checklists));
  }

  public static ServerPersistence loadFromServer(MinecraftServer server) {
    PersistentStateManager stateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
    ServerPersistence serverPersistence = stateManager.getOrCreate(
        new Type<ServerPersistence>(ServerPersistence::new, ServerPersistence::readNbt, null),
        Constants.MOD_ID);
    return serverPersistence;
  }
}
