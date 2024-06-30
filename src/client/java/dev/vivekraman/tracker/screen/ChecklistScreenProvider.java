package dev.vivekraman.tracker.screen;

import dev.vivekraman.tracker.model.LocalChecklist;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.Registerable;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;

public class ChecklistScreenProvider implements Registerable {
  private static final Logger log = MyLogger.get();

  public Screen buildScreen(LocalChecklist checklist) {
    // TODO: if checklist is null, then active identifier is likely null
    // TODO: add a text field at the top to set the active identifier (add a lock mechanism to prevent accidental change)
    // TODO: when active identifier is changed, send a packet to persist active identifier to world state
    // TODO: consume this packet on the server and update state, publish full sync packet to clients

    ConfigBuilder builder = ConfigBuilder.create();
    builder.setTitle(Text.translatable("checklist.title"));
    ConfigCategory category = builder.getOrCreateCategory(Text.translatable("checklist.category.item"));

    checklist.getCollectionInfo().forEach((itemCode, collectionInfo) -> {
      Item item = Registries.ITEM.get(Identifier.tryParse(itemCode));
      category.addEntry(builder.entryBuilder()
          .startTextDescription(Text.literal(itemCode))
          .build());
    });

    return builder.build();
  }
}
