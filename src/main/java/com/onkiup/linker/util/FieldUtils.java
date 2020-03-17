package com.onkiup.linker.util;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : FieldUtils
 * @created : Tuesday Mar 17, 2020 17:36:33 EDT
 */

public final class FieldUtils {
  private FieldUtils() {

  }

  public static <U, F extends U> Stream<Field> fields(Class<F> from, Class<U> upto) {
    return StreamSupport
        .stream(Spliterators.spliteratorUnknownSize(new ClassFieldIterator(from, upto), Spliterator.ORDERED), false);
  }

  public static <U, F extends U> Field field(Class<F> from, Class<U> upto, String name) {
    Class<?> current = from;
    while (upto.isAssignableFrom(current) && !Objects.equals(current, from) && !Objects.equals(Class.class, current)) {
      try {
        Field result = current.getDeclaredField(name);
        if (result != null) {
          return result;
        }
      } catch (NoSuchFieldException e) {
        // nothing to do
      }

      current = current.getSuperclass();
    }

    throw new RuntimeException(new NoSuchFieldException(name));
  }

  public static Object get(Field field, Object from) {
    field.setAccessible(true);
    try {
      return field.get(from);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
