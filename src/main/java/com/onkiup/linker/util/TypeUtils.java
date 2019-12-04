package com.onkiup.linker.util;

import java.lang.reflect.ParameterizedType;

public final class TypeUtils {
  private TypeUtils() {

  }

  public static Class<?> typeParameter(Class<?> subClass, Class<?> classOfInterest, int parameterIndex) {

    while (subClass != subClass.getSuperclass()) {
      // instance.getClass() is no subclass of classOfInterest or instance is a direct instance of classOfInterest
      subClass = subClass.getSuperclass();
      if (subClass == null) throw new IllegalArgumentException();
    }
    ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
    return (Class<?>) parameterizedType.getActualTypeArguments()[parameterIndex];
  }
}
