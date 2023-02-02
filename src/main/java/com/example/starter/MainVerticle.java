package com.example.starter;

import com.example.starter.handler.RouterHandler;
import com.example.starter.worker.PersonVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = this.setRoute(vertx);

    this.deployVerticle(vertx);

    vertx.createHttpServer().requestHandler(router)
      .listen(8888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          log.info("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
    });
  }

  private void deployVerticle(Vertx vertx) {
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setInstances(1);
    vertx.deployVerticle(PersonVerticle.class, deploymentOptions, result -> {
      if (result.succeeded()){
        log.info("deploy {} success", PersonVerticle.class.getName());
      } else {
        log.info("deploy {} failed", PersonVerticle.class.getName());
      }
    });
  }

  private Router setRoute(Vertx vertx) {
    Router router = Router.router(vertx);
    router.route("/api/*").handler(BodyHandler.create());

    router.get("/api/person").handler(RouterHandler::handlingPerson);
    router.post("/api/person").handler(RouterHandler::handlingPostPerson);

    return router;
  }
}
