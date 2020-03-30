package com.onkiup.linker.util;

public interface TextUtils {
  static CharSequence removeIgnoredCharacters(String ignoredCharacters, CharSequence from) {
    if (ignoredCharacters.length() == 0) {
      return from;
    }
    for (int i = 0; i < from.length(); i++) {
      if (ignoredCharacters.indexOf(from.charAt(i)) < 0) {
        return from.subSequence(i, from.length());
      }
    }
    return "";
  }

  static int firstNonIgnoredCharacter(String ignoredCharacters, CharSequence buffer, int from) {
    char character;
    do {
      character = buffer.charAt(from++);
    } while (ignoredCharacters.indexOf(character) > -1);
    return from - 1;
  }

  static boolean startsWithGetOrSet(String test) {
    if (test.length() < 3) {
      return false;
    }
    char gs = Character.toLowerCase(test.charAt(0));
    char e = Character.toLowerCase(test.charAt(1));
    char t = Character.toLowerCase(test.charAt(2));

    return (gs == 'g' || gs == 's')
      && e == 'e'
      && t == 't';
  }
}
