package dev.vivekraman.tracker.network;

import dev.vivekraman.util.Constants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record UpdateActiveIdentifierPayload(String playerUUID, String activeIdentifier) implements CustomPayload {
  public static final CustomPayload.Id<UpdateActiveIdentifierPayload> ID =
      new CustomPayload.Id<>(new Identifier(Constants.MOD_ID, "update-active-identifier"));
  public static final PacketCodec<RegistryByteBuf, UpdateActiveIdentifierPayload> CODEC = PacketCodec.tuple(
      PacketCodecs.STRING, UpdateActiveIdentifierPayload::playerUUID,
      PacketCodecs.STRING, UpdateActiveIdentifierPayload::activeIdentifier,
      UpdateActiveIdentifierPayload::new);

  @Override
  public Id<UpdateActiveIdentifierPayload> getId() {
    return ID;
  }
}
