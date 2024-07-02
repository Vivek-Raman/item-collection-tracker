package dev.vivekraman.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServerState {
  private Map<UUID, String> activeIdentifiers;
  private Map<String, ServerChecklist> checklists;
}
