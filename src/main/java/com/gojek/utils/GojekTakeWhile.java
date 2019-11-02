package com.gojek.utils;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GojekTakeWhile {

  public static <T> Stream<T> takeWhile(final Stream<T> stream, final Predicate<T> predicate) {
    CustomSpliterator<T> spliterator = new CustomSpliterator<>(stream.spliterator(), predicate);
    return StreamSupport.stream(spliterator, false);
  }

  private static class CustomSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
    private final Spliterator<T> spliterator;
    private final Predicate<T> predicate;
    private boolean isMatched = true;

    CustomSpliterator(final Spliterator<T> spliterator, final Predicate<T> predicate) {
      super(spliterator.estimateSize(), 0);
      this.spliterator = spliterator;
      this.predicate = predicate;
    }

    @Override
    public synchronized boolean tryAdvance(final Consumer<? super T> consumer) {
      final boolean hasNext = spliterator.tryAdvance(element -> {
        if (this.predicate.test(element) && isMatched) {
          consumer.accept(element);
        } else {
          this.isMatched = false;
        }
      });
      return hasNext && isMatched;
    }
  }
}


