package dev.vivekraman.tracker.config;

import dev.vivekraman.util.Constants;
import lombok.Data;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Data
@Config(name = Constants.MOD_ID)
public class TrackerConfig implements ConfigData {
  private String identifier;
}
