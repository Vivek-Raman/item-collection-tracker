package dev.vivekraman.tracker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.vivekraman.tracker.model.Operation;
import dev.vivekraman.util.state.ClassRegistry;
import dev.vivekraman.util.state.Registerable;
import org.apache.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.function.Function;

public class PersistenceAPI implements Registerable {
  private static final String BASE_URL = "https://localhost:9000/";
  private static final String BASE_URL_PROD = "https://backend.vivekraman.dev/";

  private static final Gson gson = new GsonBuilder().create();

  @Override
  public void init() throws Exception {
    Registerable.super.init();
  }

  private void callAPI(HttpRequest request, Function<Void, Void> onSuccess) {
    try (HttpClient client = HttpClient.newBuilder().build()) {
      client.sendAsync(request, (responseInfo) -> {
        if (HttpStatus.SC_OK == responseInfo.statusCode()) {
          onSuccess.apply(null);
        }
        return null;
      });
    }
  }

  public void persistOperations(List<Operation> operations, Function<Void, Void> onSuccess) {
    HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL))
            .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(operations)))
            .build();
    callAPI(request, onSuccess);
  }
}
