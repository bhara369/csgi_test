package com.search.indexer.util;

/**
 * Utility class for cleaning text content.
 */
public class TextCleaner {

  /**
   * Removes HTML tags from text.
   *
   * @param text The text to clean
   * @return Text with HTML tags removed
   */
  public static String removeHtmlTags(String text) {
    if (text == null) {
      throw new NullPointerException("Input text cannot be null");
    }
    return text.replaceAll("<[^>]+>", "");
  }
}