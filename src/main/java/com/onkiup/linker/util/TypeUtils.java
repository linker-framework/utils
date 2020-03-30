package com.onkiup.linker.util;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

public final class TypeUtils {
  private TypeUtils() {

  }

  private static Map<ClassLoader, Reflections> REFLECTIONS = new WeakHashMap<>();

  public static Class<?> typeParameter(Class<?> source, Class<?> classOfInterest, int parameterIndex) {
    Type subClass = source;

    while(isChild(subClass, classOfInterest)) {
      // instance.getClass() is no subclass of classOfInterest or instance is a direct
      // instance of classOfInterest
      Type parent = superType(subClass);
      Type result = null;
      if (parent != null && isAssignable(classOfInterest, parent)) {
        result = parent;
      } else {
        Type[] ifaces = asClass(subClass).getGenericInterfaces();
        for (Type iface : ifaces) {
          Class ifaceClass;
          if (iface instanceof ParameterizedType) {
            ifaceClass = (Class) ((ParameterizedType) iface).getRawType();
          } else {
            ifaceClass = (Class) iface;
          }

          if (classOfInterest.isAssignableFrom(ifaceClass)) {
            result = iface;
            break;
          }
        }
      } 

      if (result != null)
        subClass = result;
    }


    if (isAssignable(ParameterizedType.class, subClass)) {
      ParameterizedType parameterizedType = (ParameterizedType) (Type) subClass;
      return (Class<?>) parameterizedType.getActualTypeArguments()[parameterIndex];
    }

    throw new IllegalArgumentException("Requested class " + subClass + " (of type " + subClass.getClass() + ") is not parametrized");
  }

  public static boolean isChild(Type test, Type parent) {
    return isAssignable(parent, test) && !Objects.equals(test.getTypeName(), parent.getTypeName());
  }
  public static Type superType(Type of) {
    return asClass(of).getGenericSuperclass();
  }

  public static Class asClass(Type type) {
    if (type instanceof Class) {
      return (Class) type;
    } else if (type instanceof ParameterizedType) {
      return (Class) ((ParameterizedType) type).getRawType();
    }
    throw new RuntimeException("Unsupported Type implementation: " + type);
  }

  public static boolean isAssignable(Type what, Type from) {
    return asClass(what).isAssignableFrom(asClass(from));
  }

  public static boolean isConcrete(Class<?> test) {
    return test.isInterface() || Modifier.isAbstract(test.getModifiers());
  }

  public static <X> Stream<Class<? extends X>> subClasses(Class<X> parent) {
    return reflections(parent.getClassLoader()).getSubTypesOf(parent).stream();
  }

  protected static Reflections reflections(ClassLoader loader) {
    if (!REFLECTIONS.containsKey(loader)) {
      REFLECTIONS.put(loader, new Reflections(new ConfigurationBuilder()
          // .setUrls(ClasspathHelper.forClassLoader(loader))
          .setScanners(new SubTypesScanner(true))));
    }
    return REFLECTIONS.get(loader);
  }
}
