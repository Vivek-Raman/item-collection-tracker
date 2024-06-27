package dev.vivekraman.tracker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.vivekraman.tracker.model.Operation;
import dev.vivekraman.util.logging.MyLogger;
import dev.vivekraman.util.state.Registerable;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.List;

public class PersistenceAPI implements Registerable {
  private static final String BASE_URL_LOCAL = "http://localhost:9000/";
  private static final String BASE_URL_PROD = "https://backend.vivekraman.dev/";

  private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
  private static final Logger log = MyLogger.get();

  @Override
  public void init() throws Exception {
    Registerable.super.init();
  }

  private void callAPI(HttpRequest.Builder requestBuilder, APISuccessCallback successCallback) {
    requestBuilder.setHeader("Content-Type", "application/json");
    requestBuilder.version(HttpClient.Version.HTTP_2);
    HttpRequest request = requestBuilder.build();
    try (HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build()) {
      HttpResponse<String> response = client.send(request,
          (responseInfo) -> HttpResponse.BodySubscribers.ofString(Charset.defaultCharset()));
      if (HttpStatus.SC_OK == response.statusCode()) {
        successCallback.onSuccess();
      } else {
        log.error("Failed to call API {}, error {}, response {}",
            request.uri().getPath(), response.statusCode(), response);
      }
    } catch (Exception e) {
      log.error(e);
    }
  }

  public void persistOperations(List<Operation> operations, APISuccessCallback onSuccess) {
    HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(BASE_URL_LOCAL)
            .resolve("/item-collection-tracker/operations"))
        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(operations), Charset.defaultCharset()));
    callAPI(request, onSuccess);
  }
}
