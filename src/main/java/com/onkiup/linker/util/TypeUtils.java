package com.onkiup.linker.util;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public final class TypeUtils {
  private TypeUtils() {

  }

  private static ThreadLocal<Reflections> REFLECTIONS = new ThreadLocal<>();

  public static Class<?> typeParameter(Class<?> subClass, Class<?> classOfInterest, int parameterIndex) {

    while (subClass != subClass.getSuperclass()) {
      // instance.getClass() is no subclass of classOfInterest or instance is a direct instance of classOfInterest
      subClass = subClass.getSuperclass();
      if (subClass == null) throw new IllegalArgumentException();
    }
    ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
    return (Class<?>) parameterizedType.getActualTypeArguments()[parameterIndex];
  }

  public static boolean isConcrete(Class<?> test) {
    return test.isInterface() || Modifier.isAbstract(test.getModifiers());
  }

  public static <X> Stream<Class<? extends X>> subClasses(Class<X> parent) {
    return reflections().getSubTypesOf(parent).stream();
  }

  public static Reflections reflections() {
    if (REFLECTIONS.get() == null) {
      REFLECTIONS.set(new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forClassLoader(Thread.currentThread().getContextClassLoader()))
        .setScanners(new SubTypesScanner(true))
      ));
    }
    return REFLECTIONS.get();
  }
}
