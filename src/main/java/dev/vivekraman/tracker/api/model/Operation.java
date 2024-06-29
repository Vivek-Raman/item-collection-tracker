package dev.vivekraman.tracker.api.model;

import lombok.*;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Operation {
  private OperationType type;
  private String itemCode;
  private String identifier;
  private String collectedBy;
  private Date collectedOn;

  public static PacketCodec<RegistryByteBuf, Operation> CODEC = PacketCodec.tuple(
      PacketCodecs.STRING, (Operation op) -> op.getType().name(),
      PacketCodecs.STRING, Operation::getItemCode,
      PacketCodecs.STRING, Operation::getIdentifier,
      PacketCodecs.STRING, Operation::getCollectedBy,
      PacketCodecs.VAR_LONG, (Operation op) -> op.getCollectedOn().getTime(),
      ((operationType, itemCode, identifier, collectedBy, collectedOn) -> Operation.builder()
          .type(OperationType.valueOf(operationType))
          .itemCode(itemCode)
          .identifier(identifier)
          .collectedBy(collectedBy)
          .collectedOn(new Date(collectedOn)).build()));
}
