package com.example.starter.worker;

import com.example.starter.model.PersonModel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.info("init person verticle");

    vertx.eventBus().consumer("get.person", this::processPerson);
    vertx.eventBus().consumer("post.person", this::processPostPerson);


    super.start(startPromise);
  }

  private void processPostPerson(Message<String> tMessage) {
    String body = tMessage.body();
    log.info("post.person got {}", body);

    PersonModel personModel = Json.decodeValue(body, PersonModel.class);
    personModel.setStatus("update");
    String result = Json.encode(personModel);
    tMessage.reply(result);

  }

  private  void processPerson(Message<String> tMessage) {
    String name = tMessage.body();
    log.info("get.person got {}", name);
    PersonModel personModel = new PersonModel();
    personModel.setName(name);
    personModel.setStatus("active");

    String result = Json.encode(personModel);
    tMessage.reply(result);
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }
}
