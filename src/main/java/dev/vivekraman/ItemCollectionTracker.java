package dev.vivekraman;

import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.ClassRegistry;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.Logger;

public class ItemCollectionTracker implements ClientModInitializer {
    public static final Logger log = MyLogger.get();

	@Override
	public void onInitializeClient() {
		ClassRegistry.init(log);
	}
}
