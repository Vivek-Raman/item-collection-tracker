package dev.vivekraman.tracker.screen;

import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.tracker.model.LocalState;
import dev.vivekraman.tracker.service.LocalStateService;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import dev.vivekraman.util.state.Registerable;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;

public class ChecklistScreenProvider implements Registerable {
  private static final Logger log = MyLogger.get();

  @Override
  public void init() throws Exception {
    Registerable.super.init();

    // add button to pause menu
    ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
      if (screen instanceof GameMenuScreen) {
        Screens.getButtons(screen).add(ButtonWidget.builder(Text.translatable("checklist.open-button"),
            (button -> {
              LocalChecklist checklist =
                  ClassRegistry.supplyClient(LocalStateService.class).getLocalState().getChecklist();
              MinecraftClient.getInstance().setScreen(buildScreen(checklist));
            })).build());
      }
    });
  }

  public Screen buildScreen(LocalChecklist checklist) {
    // TODO: if checklist is null, then active identifier is likely null
    // TODO: add a text field at the top to set the active identifier (add a lock mechanism to prevent accidental change)
    // TODO: when active identifier is changed, send a packet to persist active identifier to world state
    // TODO: consume this packet on the server and update state, publish full sync packet to clients

    ConfigBuilder builder = ConfigBuilder.create();
    builder.setTitle(Text.translatable("checklist.title"));
    ConfigCategory category = builder.getOrCreateCategory(Text.translatable("checklist.category.item"));

    category.addEntry(builder.entryBuilder()
        .startTextDescription(Text.literal(checklist.getIdentifier()))
        .build());

    return builder.build();
  }
}
