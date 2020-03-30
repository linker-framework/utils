package com.onkiup.linker.util;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : AnnotationUtils
 * @created : Wednesday Mar 18, 2020 16:40:26 EDT
 */

public class AnnotationUtils {
  private static ThreadLocal<Reflections> REFLECTIONS = new ThreadLocal<>();

  protected static Reflections reflections() {
    if (REFLECTIONS.get() == null) {
      REFLECTIONS.set(new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forClassLoader(Thread.currentThread().getContextClassLoader()))
      ));
    }
    return REFLECTIONS.get();
  }

  public Stream<Method> methodsWith(Class annotation) {
    return reflections().getMethodsAnnotatedWith(annotation).stream();
  }
}
