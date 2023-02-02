package com.example.starter.handler;

import com.example.starter.model.PersonModel;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouterHandler {

  public static void handlingPerson(RoutingContext routingContext){

    String name = routingContext.request().getParam("name");

    routingContext.vertx().eventBus().request("get.person", name, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()){
        String result = (String) messageAsyncResult.result().body();
        routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(result);
      } else {
        routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
          .end(messageAsyncResult.cause().getMessage());
      }
    });

  }

  public static void handlingPostPerson(RoutingContext routingContext) {

    PersonModel body = routingContext.body().asPojo(PersonModel.class);
    log.info(body.toString());

    String bodyString = routingContext.body().asString();

    routingContext.vertx().eventBus().request("post.person", bodyString, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()){
        String result = (String) messageAsyncResult.result().body();
        routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(result);
      } else {
        routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
          .end(messageAsyncResult.cause().getMessage());
      }
    });
  }
}
