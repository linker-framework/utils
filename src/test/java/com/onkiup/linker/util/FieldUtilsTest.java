package com.onkiup.linker.util;

import java.lang.reflect.Field;

import junit.framework.Assert;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : FieldUtilsTest
 * @created : Wednesday Mar 18, 2020 20:57:45 EDT
 */

public class FieldUtilsTest {
  private int value;

  public void testDeduct() throws Exception {
    Field expected = FieldUtilsTest.class.getDeclaredField("value");
    Assert.assertSame(expected, FieldUtils.deduct(FieldUtilsTest.class, Object.class, "value"));
    Assert.assertSame(expected, FieldUtils.deduct(FieldUtilsTest.class, Object.class, "getValue"));
    Assert.assertSame(expected, FieldUtils.deduct(FieldUtilsTest.class, Object.class, "setValue"));
  }
}
