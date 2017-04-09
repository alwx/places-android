package me.alwx.places.data.db;

import android.database.Cursor;

/**
 * Helper class for different DB methods
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public final class Db {
  public static String getString(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
  }

  public static long getLong(Cursor cursor, String columnName) {
    return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
  }

  public static float getFloat(Cursor cursor, String columnName) {
    return cursor.getFloat(cursor.getColumnIndexOrThrow(columnName));
  }

  private Db() {
    throw new AssertionError("No instances.");
  }
}