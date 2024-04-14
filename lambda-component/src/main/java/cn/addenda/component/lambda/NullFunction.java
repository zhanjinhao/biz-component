package cn.addenda.component.lambda;

import java.util.function.Consumer;
import java.util.function.Function;

public class NullFunction<T, R> implements Function<T, R> {

    private final Consumer<T> consumer;

    public NullFunction(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public R apply(T o) {
        consumer.accept(o);
        return null;
    }
}
