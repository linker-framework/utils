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
}
