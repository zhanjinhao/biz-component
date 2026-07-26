package cn.addenda.component.common.lambda.att;

import cn.addenda.component.common.jackson.util.JacksonUtils;
import lombok.Getter;

public class AttRunnable<A> implements Runnable {

  @Getter
  private final Runnable runnable;

  @Getter
  private final A att;

  private final boolean jacksonToString;

  private AttRunnable(A att, Runnable runnable, boolean jacksonToString) {
    this.att = att;
    this.runnable = runnable;
    this.jacksonToString = jacksonToString;
  }

  @Override
  public void run() {
    runnable.run();
  }

  @Override
  public String toString() {
    return "AttRunnable{" +
            "runnable=" + runnable +
            ", att=" + (jacksonToString ? JacksonUtils.toStr(att) : att) +
            '}';
  }

  public static <A> AttRunnable<A> of(A att, Runnable runnable) {
    return new AttRunnable<>(att, runnable, false);
  }

  public static <A> AttRunnable<A> of(A att, Runnable runnable, boolean jacksonToString) {
    return new AttRunnable<>(att, runnable, jacksonToString);
  }

}
