/*-
 * Copyright (C) 2006 Erik Larsson
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

import io.takari.osxjdkget.csjc.DynamicStruct;
import io.takari.osxjdkget.csjc.structelements.Dictionary;
import io.takari.osxjdkget.csjc.structelements.IntegerFieldRepresentation;
import io.takari.osxjdkget.util.Util;

/** This class was generated by CStructToJavaClass. */
public class HFSPlusCatalogThread extends HFSPlusCatalogLeafRecordData implements DynamicStruct {
  /*
   * struct HFSPlusCatalogThread
   * size: <=520 bytes
   * description:
   *
   * BP  Size  Type              Identifier  Description
   * ---------------------------------------------------
   * 0   2     SInt16            recordType
   * 2   2     SInt16            reserved
   * 4   4     HFSCatalogNodeID  parentID
   * 8   <=512 HFSUniStr255      nodeName
   */

  private final byte[] recordType = new byte[2];
  private final byte[] reserved = new byte[2];
  private final HFSCatalogNodeID parentID;
  private final HFSUniStr255 nodeName;

  public HFSPlusCatalogThread(byte[] data, int offset) {
    System.arraycopy(data, offset + 0, recordType, 0, 2);
    System.arraycopy(data, offset + 2, reserved, 0, 2);
    parentID = new HFSCatalogNodeID(data, offset + 4);
    nodeName = new HFSUniStr255(data, offset + 8);
  }

  @Override
  public byte[] getBytes() {
    byte[] result = new byte[length()];
    byte[] tempData;
    int offset = 0;

    System.arraycopy(recordType, 0, result, offset, recordType.length);
    offset += recordType.length;
    System.arraycopy(reserved, 0, result, offset, reserved.length);
    offset += reserved.length;
    tempData = parentID.getBytes();
    System.arraycopy(tempData, 0, result, offset, tempData.length);
    offset += tempData.length;
    tempData = nodeName.getBytes();
    System.arraycopy(tempData, 0, result, offset, tempData.length);
    offset += tempData.length;

    return result;
  }

  public int length() {
    return 2 + 2 + HFSCatalogNodeID.length() + nodeName.length();
  }

  /* @Override */
  @Override
  public int occupiedSize() {
    return length();
  }

  /* @Override */
  @Override
  public int maxSize() {
    return 520;
  }

  @Override
  public short getRecordType() {
    return Util.readShortBE(recordType);
  }

  public short getReserved() {
    return Util.readShortBE(reserved);
  }

  public HFSCatalogNodeID getParentID() {
    return parentID;
  }

  public HFSUniStr255 getNodeName() {
    return nodeName;
  }

  /* @Override */
  @Override
  public void printFields(PrintStream ps, String prefix) {
    ps.println(prefix + " recordType: " + getRecordType());
    ps.println(prefix + " reserved: " + getReserved());
    ps.println(prefix + " parentID: ");
    getParentID().print(ps, prefix + "  ");
    ps.println(prefix + " nodeName: ");
    getNodeName().print(ps, prefix + "  ");
  }

  /* @Override */
  @Override
  public void print(PrintStream ps, String prefix) {
    ps.println(prefix + "HFSPlusCatalogThread:");
    printFields(ps, prefix);
  }

  /* @Override */
  @Override
  public Dictionary getStructElements() {
    DictionaryBuilder db = new DictionaryBuilder(HFSPlusCatalogThread.class.getSimpleName());

    db.addUIntBE("recordType", recordType, "Record type",
      IntegerFieldRepresentation.HEXADECIMAL);
    db.addUIntBE("reserved", reserved, "Reserved",
      IntegerFieldRepresentation.HEXADECIMAL);
    db.add("parentID", parentID.getOpaqueStructElement(), "Parent ID");
    db.add("nodeName", nodeName.getStructElements(), "Node name");

    return db.getResult();
  }
}
