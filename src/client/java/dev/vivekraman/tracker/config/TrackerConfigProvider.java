package dev.vivekraman.tracker.config;

import dev.vivekraman.util.state.Registerable;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class TrackerConfigProvider implements Registerable {
  @Override
  public void init() throws Exception {
    Registerable.super.init();
    AutoConfig.register(TrackerConfig.class, GsonConfigSerializer::new);
  }

  public TrackerConfig get() {
    return AutoConfig.getConfigHolder(TrackerConfig.class).getConfig();
  }
}
