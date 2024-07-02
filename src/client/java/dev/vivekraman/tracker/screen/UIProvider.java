package dev.vivekraman.tracker.screen;

import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.tracker.model.LocalState;
import dev.vivekraman.tracker.service.LocalStateService;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import dev.vivekraman.util.state.Registerable;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
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
        client.execute(() -> {
          Screens.getButtons(screen).add(ButtonWidget.builder(Text.translatable("checklist.open-button"),
              buttonWidget -> handleOpenChecklistPressed(screen, buttonWidget)).build());
        });
      }
    });
  }

  private void handleOpenChecklistPressed(Screen parentScreen, ButtonWidget buttonWidget) {
    MinecraftClient.getInstance().setScreen(checklistScreenProvider.buildScreen(parentScreen));
  }
}
