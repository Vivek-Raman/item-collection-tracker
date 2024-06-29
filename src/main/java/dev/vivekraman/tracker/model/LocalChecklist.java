package dev.vivekraman.tracker.model;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocalChecklist {
  private String identifier;
  private Date updatedOn;
  private Map<String, CollectionInfo> collectionInfo;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static final class CollectionInfo {
    private String collectedBy;
    private Date collectedOn;
  }
}
