package dev.vivekraman.util.state;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;

public class ClassRegistry {
  private static ClassRegistry instance;
  private static boolean initialized = false;
  private final Map<String, Registerable> classes;

  public ClassRegistry() {
    classes = new ConcurrentHashMap<>();
  }

  public static void init(Logger log) {
    if (initialized || Objects.nonNull(instance)) {
      log.error("ClassRegistry is already initialized!");
    }

    instance = new ClassRegistry();
    initialized = true;
  }

  public static void register(Registerable toRegister) throws Exception {
    register(toRegister, toRegister.getClass());
  }

  public static <T> void register(Registerable toRegister, Class<T> keyClazz) throws Exception {
    ClassRegistry.instance.classes.put(keyClazz.getName(), toRegister);
    toRegister.init();
  }

  @SuppressWarnings("unchecked")
  public static <T extends Registerable> T supply(Class<T> clazz) {
    return (T) ClassRegistry.instance.classes.get(clazz.getName());
  }
}
