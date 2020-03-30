package com.onkiup.linker.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : TestTextUtils
 * @created : Wednesday Mar 18, 2020 20:06:45 EDT
 */

public class TextUtilsTest {
  @Test
  public void testStartsWithGetOrSet() {
    Assert.assertTrue(TextUtils.startsWithGetOrSet("getText"));
    Assert.assertTrue(TextUtils.startsWithGetOrSet("setText"));
    Assert.assertTrue(TextUtils.startsWithGetOrSet("GetText"));
    Assert.assertTrue(TextUtils.startsWithGetOrSet("SeTText"));
    Assert.assertFalse(TextUtils.startsWithGetOrSet("wetText"));
    Assert.assertFalse(TextUtils.startsWithGetOrSet("sRtText"));
  }
}
