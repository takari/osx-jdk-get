/*-
 * Copyright (C) 2006-2008 Erik Larsson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.takari.osxjdkget.hfs.types.carbon;

import java.io.PrintStream;

import io.takari.osxjdkget.csjc.PrintableStruct;
import io.takari.osxjdkget.csjc.StructElements;
import io.takari.osxjdkget.csjc.structelements.Dictionary;
import io.takari.osxjdkget.util.Util;

/** This class was generated by CStructToJavaClass. */
public class Rect implements PrintableStruct, StructElements {
  /*
   * struct Rect
   * size: 8 bytes
   * description:
   *
   * BP  Size  Type    Identifier  Description
   * -----------------------------------------
   * 0   2     SInt16  top
   * 2   2     SInt16  left
   * 4   2     SInt16  bottom
   * 6   2     SInt16  right
   */

  public static final int STRUCTSIZE = 8;

  private final byte[] top = new byte[2];
  private final byte[] left = new byte[2];
  private final byte[] bottom = new byte[2];
  private final byte[] right = new byte[2];

  public Rect(byte[] data, int offset) {
    System.arraycopy(data, offset + 0, top, 0, 2);
    System.arraycopy(data, offset + 2, left, 0, 2);
    System.arraycopy(data, offset + 4, bottom, 0, 2);
    System.arraycopy(data, offset + 6, right, 0, 2);
  }

  public static int length() {
    return STRUCTSIZE;
  }

  /**  */
  public short getTop() {
    return Util.readShortBE(top);
  }

  /**  */
  public short getLeft() {
    return Util.readShortBE(left);
  }

  /**  */
  public short getBottom() {
    return Util.readShortBE(bottom);
  }

  /**  */
  public short getRight() {
    return Util.readShortBE(right);
  }

  @Override
  public String toString() {
    return "(" + getTop() + "," + getLeft() + "," + getBottom() + "," + getRight() + ")";
  }

  @Override
  public void printFields(PrintStream ps, String prefix) {
    ps.println(prefix + " top: " + getTop());
    ps.println(prefix + " left: " + getLeft());
    ps.println(prefix + " bottom: " + getBottom());
    ps.println(prefix + " right: " + getRight());
  }

  @Override
  public void print(PrintStream ps, String prefix) {
    ps.println(prefix + "Rect:");
    printFields(ps, prefix);
  }

  public byte[] getBytes() {
    byte[] result = new byte[STRUCTSIZE];
    byte[] tempData;
    int offset = 0;
    System.arraycopy(top, 0, result, offset, top.length);
    offset += top.length;
    System.arraycopy(left, 0, result, offset, left.length);
    offset += left.length;
    System.arraycopy(bottom, 0, result, offset, bottom.length);
    offset += bottom.length;
    System.arraycopy(right, 0, result, offset, right.length);
    offset += right.length;
    return result;
  }

  /* @Override */
  @Override
  public Dictionary getStructElements() {
    DictionaryBuilder db = new DictionaryBuilder(Rect.class.getSimpleName());

    db.addUIntBE("top", top, "Top");
    db.addUIntBE("left", left, "Left");
    db.addUIntBE("bottom", bottom, "Bottom");
    db.addUIntBE("right", right, "Right");

    return db.getResult();
  }
}
