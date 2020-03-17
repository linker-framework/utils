package com.onkiup.linker.util;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : ClassFieldIterator
 * @created : Tuesday Mar 17, 2020 17:40:18 EDT
 */

public class ClassFieldIterator<TO, FROM extends TO> implements Iterator<Field> {

  private Field[] fields;
  private Class<FROM> from;
  private Class<? extends TO> current;
  private Class<TO> upto;
  private int next = 0;

  public ClassFieldIterator(Class<FROM> from, Class<TO> upto) {
    this.from = from;
    this.current = from;
    this.upto = upto;
    this.fields = from.getDeclaredFields();
  }

  @Override
  public boolean hasNext() {
    if (next < fields.length) {
      return true;
    }

    Class<?> parent = current.getSuperclass();
    if (upto.isAssignableFrom(parent) && !Objects.equals(parent, upto)) {
      current = (Class<? extends TO>)parent;
      fields = current.getDeclaredFields();
      next = 0;
      return fields.length > 0;
    }

    return false;
  }

  @Override
  public Field next() {
    return fields[next++];
  }
}
