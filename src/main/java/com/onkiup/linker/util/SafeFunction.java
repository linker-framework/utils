package com.onkiup.linker.util;

public interface SafeFunction<T, X> {
  X apply(T value) throws Exception;
}
