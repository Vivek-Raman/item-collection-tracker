package dev.vivekraman.tracker.network;

import dev.vivekraman.tracker.api.model.Operation;
import dev.vivekraman.util.Constants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ItemCollectedPayload(Operation operation) implements CustomPayload {
  public static final CustomPayload.Id<ItemCollectedPayload> ID =
      new CustomPayload.Id<>(new Identifier(Constants.MOD_ID, "item-collected"));
  public static final PacketCodec<RegistryByteBuf, ItemCollectedPayload> CODEC = PacketCodec.tuple(
      Operation.CODEC,
      ItemCollectedPayload::operation,
      ItemCollectedPayload::new);

  @Override
  public Id<ItemCollectedPayload> getId() {
    return ID;
  }
}
