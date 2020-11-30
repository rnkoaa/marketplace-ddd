package com.marketplace.eventstore.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.immutables.criteria.backend.WriteResult;

public class WriteResultSubscriber implements Subscriber<WriteResult> {

  private final CountDownLatch latch;
  private volatile Subscription subscription;
  private volatile boolean completed;
  private final List<Throwable> errors = new ArrayList<>();
  private List<WriteResult> received = new ArrayList<>();

  public WriteResultSubscriber() {
    this.latch = new CountDownLatch(1);
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    this.subscription = subscription;
  }

  @Override
  public void onNext(WriteResult item) {
    received.add(item);
  }

  @Override
  public void onError(Throwable throwable) {
    errors.add(throwable);
    onComplete();
  }

  @Override
  public void onComplete() {
    completed = true;
    latch.countDown();
  }

  public boolean isCompleted() {
    return completed;
  }

  public List<Throwable> getErrors() {
    return errors;
  }

  public List<WriteResult> getReceived() {
    return received;
  }
}
