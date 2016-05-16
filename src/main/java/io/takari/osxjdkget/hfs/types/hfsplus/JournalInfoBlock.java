/*-
 * Copyright (C) 2007 Erik Larsson
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

package io.takari.osxjdkget.hfs.types.hfsplus;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigInteger;

import io.takari.osxjdkget.csjc.PrintableStruct;
import io.takari.osxjdkget.csjc.StaticStruct;
import io.takari.osxjdkget.csjc.StructElements;
import io.takari.osxjdkget.csjc.structelements.Dictionary;
import io.takari.osxjdkget.util.Util;

/** This class was generated by CStructToJavaClass. */
public class JournalInfoBlock implements StaticStruct, PrintableStruct,
  StructElements {
  public static final int kJIJournalInFSMask = 0x00000001;
  public static final int kJIJournalOnOtherDeviceMask = 0x00000002;
  public static final int kJIJournalNeedInitMask = 0x00000004;

  /*
   * struct JournalInfoBlock
   * size: 180 bytes
   * description:
   *
   * BP  Size  Type        Identifier       Description
   * --------------------------------------------------
   * 0   4     UInt32      flags
   * 4   4*8   UInt32[8]   deviceSignature
   * 36  8     UInt64      offset
   * 44  8     UInt64      size
   * 52  4*32  UInt32[32]  reserved
   */

  public static final int STRUCTSIZE = 180;

  private int flags;
  private final byte[] deviceSignature = new byte[4 * 8];
  private long offset;
  private long size;
  private final byte[] reserved = new byte[4 * 32];

  public JournalInfoBlock(byte[] data, int offset) {
    this.flags = Util.readIntBE(data, offset + 0);
    System.arraycopy(data, offset + 4, this.deviceSignature, 0, 4 * 8);
    this.offset = Util.readLongBE(data, offset + 36);
    this.size = Util.readLongBE(data, offset + 44);
    System.arraycopy(data, offset + 52, this.reserved, 0, 4 * 32);
  }

  public static int length() {
    return STRUCTSIZE;
  }

  @Override
  public int size() {
    return length();
  }

  /**  */
  public final int getFlags() {
    return flags;
  }

  /**  */
  public final int[] getDeviceSignature() {
    return Util.readIntArrayBE(deviceSignature);
  }

  /**  */
  public final BigInteger getOffset() {
    return Util.unsign(getRawOffset());
  }

  /**  */
  public final BigInteger getSize() {
    return Util.unsign(getRawSize());
  }

  /**  */
  public final int[] getReserved() {
    return Util.readIntArrayBE(reserved);
  }

  /** <b>Note that the return value from this function should be interpreted as an unsigned integer, for instance using Util.unsign(...).</b> */
  public final long getRawOffset() {
    return offset;
  }

  /** <b>Note that the return value from this function should be interpreted as an unsigned integer, for instance using Util.unsign(...).</b> */
  public final long getRawSize() {
    return size;
  }

  public boolean getFlagJournalInFS() {
    return (getFlags() & kJIJournalInFSMask) != 0;
  }

  public boolean getFlagJournalOnOtherDevice() {
    return (getFlags() & kJIJournalOnOtherDeviceMask) != 0;
  }

  public boolean getFlagJournalNeedInit() {
    return (getFlags() & kJIJournalNeedInitMask) != 0;
  }

  public static int getStructSize() {
    return length();
  }

  @Override
  public void printFields(PrintStream ps, String prefix) {
    ps.println(prefix + " flags: " + getFlags());
    ps.println(prefix + " deviceSignature: " + getDeviceSignature());
    ps.println(prefix + " offset: " + getOffset());
    ps.println(prefix + " size: " + getSize());
    ps.println(prefix + " reserved: " + getReserved());
  }

  @Override
  public void print(PrintStream ps, String prefix) {
    ps.println(prefix + "JournalInfoBlock:");
    printFields(ps, prefix);
  }

  @Override
  public byte[] getBytes() {
    byte[] result = new byte[length()];
    int _offset = 0;
    Util.arrayPutBE(result, _offset, this.flags);
    _offset += 4;
    System.arraycopy(this.deviceSignature, 0, result, _offset, this.deviceSignature.length);
    _offset += this.deviceSignature.length;
    Util.arrayPutBE(result, _offset, this.offset);
    _offset += 8;
    Util.arrayPutBE(result, _offset, this.size);
    _offset += 8;
    System.arraycopy(this.reserved, 0, result, _offset, this.reserved.length);
    _offset += this.reserved.length;
    return result;
  }

  private Field getPrivateField(String name) throws NoSuchFieldException {
    Field f = getClass().getDeclaredField(name);
    f.setAccessible(true);
    return f;
  }

  @Override
  public Dictionary getStructElements() {
    DictionaryBuilder db = new DictionaryBuilder("JournalInfoBlock",
      "Journal info block, describing the location and size of the " +
        "journal.");

    try {
      db.addUIntBE("flags", getPrivateField("flags"), this, null, null,
        HEXADECIMAL);
      db.addIntArray("deviceSignature", deviceSignature, BITS_32,
        UNSIGNED, BIG_ENDIAN, null, HEXADECIMAL);
      db.addUIntBE("offset", getPrivateField("offset"), this, null,
        "bytes", DECIMAL);
      db.addUIntBE("size", getPrivateField("size"), this, null, "bytes",
        DECIMAL);
      db.addIntArray("reserved", reserved, BITS_32, UNSIGNED, BIG_ENDIAN,
        null, HEXADECIMAL);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    return db.getResult();
  }
}
