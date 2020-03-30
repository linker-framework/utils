package com.onkiup.linker.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : FieldUtils
 * @created : Tuesday Mar 17, 2020 17:36:33 EDT
 */

public final class FieldUtils {
  private FieldUtils() {

  }

  public static <U, F extends U> Stream<Field> stream(Class<F> from, Class<U> upto) {
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

  public static Object get(Object from, Field field) {
    field.setAccessible(true);
    try {
      return field.get(from);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object getViaGetter(Object from, Field field) {
    return getter(field).map(getter -> {
      try {
        return getter.invoke(from);
      } catch (Exception e) {
        throw new RuntimeException("Failed to invoke getter '" + getter + "'", e);
      }
    }).orElseThrow(() -> new RuntimeException("No getter found for field '" + field + "'"));
  }

  public static void setViaSetter(Object to, Field field, Object value) {
    Method setter = setter(field).orElseThrow(() -> new RuntimeException("No setter found for field '" + field + "'"));
    try {
      setter.invoke(to, value);
    } catch (Exception e) {
      throw new RuntimeException("Failed to invoke setter '" + setter + "'");
    }
  }

  public static Optional<Method> getter(Field field) {
    Class<?> source = field.getDeclaringClass();
    String fieldName = field.getName();

    try {
      return Optional.of(source.getDeclaredMethod(fieldName));
    } catch (NoSuchMethodException nsme) {
      try {
        String getterName = "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
        return Optional.of(source.getDeclaredMethod(getterName));
      } catch (NoSuchMethodException nsme2) {
        return Optional.empty();
      }
    }
  }

  public static Optional<Method> setter(Field field) {
    Class<?> source = field.getDeclaringClass();
    String fieldName = field.getName();

    try {
      return Optional.of(source.getDeclaredMethod(fieldName, field.getType()));
    } catch (NoSuchMethodException nsme) {
      try {
        String getterName = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
        return Optional.of(source.getDeclaredMethod(getterName, field.getType()));
      } catch (NoSuchMethodException nsme2) {
        return Optional.empty();
      }
    }
  }

  public static Optional<Field> deduct(Class from, Class upto, @Nonnull String name) {
    if (name.length() > 3 && TextUtils.startsWithGetOrSet(name)) {
      // normalizing getter name
      name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name.substring(3));
    }

    return Optional.ofNullable(FieldUtils.field(from, upto, name));
  }

  public static Optional<Field> field(Class onlyFrom, @Nonnull String name) {
    return Optional.ofNullable(FieldUtils.field(onlyFrom, onlyFrom.getSuperclass(), name));
  }
}
