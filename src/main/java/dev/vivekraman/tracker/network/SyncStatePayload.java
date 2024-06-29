package dev.vivekraman.tracker.network;

import dev.vivekraman.util.Constants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncStatePayload(NbtCompound persistenceTag) implements CustomPayload {
  public static final CustomPayload.Id<SyncStatePayload> ID =
      new CustomPayload.Id<>(new Identifier(Constants.MOD_ID, "full_sync"));

  public static final PacketCodec<RegistryByteBuf, SyncStatePayload> CODEC = PacketCodec.tuple(
      PacketCodecs.NBT_COMPOUND,
      SyncStatePayload::persistenceTag,
      SyncStatePayload::new);


  @Override
  public Id<SyncStatePayload> getId() {
    return ID;
  }
}
