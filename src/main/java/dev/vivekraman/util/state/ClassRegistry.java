package dev.vivekraman.util.state;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Logger;

public class ClassRegistry {
  private static ClassRegistry instance;
  private static ClassRegistry clientInstance;

  private static boolean initialized = false;
  private static boolean clientInitialized = false;

  private final Map<String, Registerable> classes;
  private final Map<String, Registerable> clientClasses;

  public ClassRegistry() {
    classes = new ConcurrentHashMap<>();
    clientClasses = new ConcurrentHashMap<>();
  }

  public static void init(Logger log) {
    if (initialized || Objects.nonNull(instance)) {
      log.error("ClassRegistry is already initialized!");
      return;
    }

    instance = new ClassRegistry();
    initialized = true;
  }

  @Environment(EnvType.CLIENT)
  public static void initClient(Logger log) {
    if (clientInitialized || Objects.nonNull(clientInstance)) {
      log.error("Client ClassRegistry is already initialized!");
      return;
    }

    clientInstance = new ClassRegistry();
    clientInitialized = true;
  }

  public static void register(Registerable toRegister) throws Exception {
    register(toRegister, toRegister.getClass());
  }

  @Environment(EnvType.CLIENT)
  public static void registerClient(Registerable toRegister) throws Exception {
    registerClient(toRegister, toRegister.getClass());
  }

  public static <T> void register(Registerable toRegister, Class<T> keyClazz) throws Exception {
    ClassRegistry.instance.classes.put(keyClazz.getName(), toRegister);
    toRegister.init();
  }

  @Environment(EnvType.CLIENT)
  public static <T> void registerClient(Registerable toRegister, Class<T> keyClazz) throws Exception {
    ClassRegistry.clientInstance.classes.put(keyClazz.getName(), toRegister);
    toRegister.init();
  }

  @SuppressWarnings("unchecked")
  public static <T extends Registerable> T supply(Class<T> clazz) {
    return (T) ClassRegistry.instance.classes.get(clazz.getName());
  }

  @SuppressWarnings("unchecked")
  @Environment(EnvType.CLIENT)
  public static <T extends Registerable> T supplyClient(Class<T> clazz) {
    return (T) ClassRegistry.clientInstance.classes.get(clazz.getName());
  }
}
