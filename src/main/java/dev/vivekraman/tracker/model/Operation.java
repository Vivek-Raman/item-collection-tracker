package dev.vivekraman.tracker.model;

import lombok.*;

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
  private Date collectedOn;
}
