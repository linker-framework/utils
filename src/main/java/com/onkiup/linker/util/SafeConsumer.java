package com.onkiup.linker.util;

public interface SafeConsumer<T> {
  void accept(T value) throws Exception;
}
