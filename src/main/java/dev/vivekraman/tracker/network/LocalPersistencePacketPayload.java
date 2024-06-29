package dev.vivekraman.tracker.network;

import dev.vivekraman.util.Constants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
@Deprecated
public record LocalPersistencePacketPayload(NbtCompound persistenceTag) implements CustomPayload {
  public static final CustomPayload.Id<LocalPersistencePacketPayload> ID =
      new CustomPayload.Id<>(new Identifier(Constants.MOD_ID, "initial_sync"));

  public static final PacketCodec<RegistryByteBuf, LocalPersistencePacketPayload> CODEC = PacketCodec.tuple(
      PacketCodecs.NBT_COMPOUND,
      LocalPersistencePacketPayload::persistenceTag,
      LocalPersistencePacketPayload::new);


  @Override
  public Id<LocalPersistencePacketPayload> getId() {
    return ID;
  }
}
