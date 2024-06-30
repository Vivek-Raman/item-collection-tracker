package dev.vivekraman.tracker.screen;

import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.tracker.model.LocalState;
import dev.vivekraman.tracker.service.LocalStateService;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import dev.vivekraman.util.state.Registerable;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;

public class UIProvider implements Registerable {
  private static final Logger log = MyLogger.get();

  private ChecklistScreenProvider checklistScreenProvider;

  @Override
  public void init() throws Exception {
    Registerable.super.init();
    checklistScreenProvider = ClassRegistry.supplyClient(ChecklistScreenProvider.class);

    // add button to pause menu
    ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
      if (screen instanceof GameMenuScreen) {
        Screens.getButtons(screen).add(ButtonWidget.builder(Text.translatable("checklist.open-button"),
            this::handleOpenChecklistPressed).build());
      }
    });
  }

  private void handleOpenChecklistPressed(ButtonWidget buttonWidget) {
    LocalState state = ClassRegistry.supplyClient(LocalStateService.class).getLocalState();
    log.info("Dump state {}", state);
//      MinecraftClient.getInstance().setScreen(buildScreen(checklist));
  }
}
