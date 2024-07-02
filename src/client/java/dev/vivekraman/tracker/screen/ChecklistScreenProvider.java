package dev.vivekraman.tracker.screen;

import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.tracker.model.LocalState;
import dev.vivekraman.tracker.service.LocalStateService;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import dev.vivekraman.util.state.Registerable;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class ChecklistScreenProvider implements Registerable {
  private static final Logger log = MyLogger.get();

  private LocalStateService localStateService;

  @Override
  public void init() throws Exception {
    Registerable.super.init();

    localStateService = ClassRegistry.supplyClient(LocalStateService.class);
  }

  public Screen buildScreen(Screen parentScreen) {
    LocalState state = localStateService.getLocalState();

    ConfigBuilder builder = ConfigBuilder.create();
    builder.setParentScreen(parentScreen);
    builder.setTitle(Text.translatable("checklist.title"));

    ConfigCategory identifierCategory = builder.getOrCreateCategory(Text.translatable("checklist.category.identifier"));
    identifierCategory.addEntry(builder.entryBuilder()
        .startStrField(Text.translatable("active identifier"), StringUtils.defaultString(state.getIdentifier()))
        .setSaveConsumer(localStateService::persistNewIdentifier)
        .build());
    if (Objects.isNull(state.getChecklist())) {
      return builder.build();
    }

    LocalChecklist checklist = state.getChecklist();
    ConfigCategory category = builder.getOrCreateCategory(Text.translatable("checklist.category.item"));

    Registries.ITEM.getEntrySet().forEach((entry) -> {
      Identifier id = entry.getKey().getValue();
      Item item = entry.getValue();
      category.addEntry(builder.entryBuilder()
          .startTextDescription(Text.translatable(item.getTranslationKey()))
          .setColor(checklist.getCollectionInfo().containsKey(id.toString()) ? Colors.LIGHT_YELLOW : Colors.RED)
          .build());
    });

    return builder.build();
  }
}
