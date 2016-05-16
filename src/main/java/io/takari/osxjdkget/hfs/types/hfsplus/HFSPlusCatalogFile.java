/*-
 * Copyright (C) 2008 Erik Larsson
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
import java.util.Date;

import io.takari.osxjdkget.csjc.StaticStruct;
import io.takari.osxjdkget.csjc.structelements.Dictionary;
import io.takari.osxjdkget.hfs.types.finder.ExtendedFileInfo;
import io.takari.osxjdkget.hfs.types.finder.FileInfo;
import io.takari.osxjdkget.util.Util;

/** This class was generated by CStructToJavaClass. */
public class HFSPlusCatalogFile extends HFSPlusCatalogLeafRecordData implements
  HFSPlusCatalogAttributes, StaticStruct {
  /*
   * struct HFSPlusCatalogFile
   * size: 248 bytes
   * description:
   *
   * BP   Size  Type              Identifier        Description
   * ----------------------------------------------------------
   * 0    2     SInt16            recordType
   * 2    2     UInt16            flags
   * 4    4     UInt32            reserved1
   * 8    4     HFSCatalogNodeID  fileID
   * 12   4     UInt32            createDate
   * 16   4     UInt32            contentModDate
   * 20   4     UInt32            attributeModDate
   * 24   4     UInt32            accessDate
   * 28   4     UInt32            backupDate
   * 32   16    HFSPlusBSDInfo    permissions
   * 48   16    FileInfo          userInfo
   * 64   16    ExtendedFileInfo  finderInfo
   * 80   4     UInt32            textEncoding
   * 84   4     UInt32            reserved2
   * 88   80    HFSPlusForkData   dataFork
   * 168  80    HFSPlusForkData   resourceFork
   */

  private final byte[] recordType = new byte[2];
  private final byte[] flags = new byte[2];
  private final byte[] reserved1 = new byte[4];
  private final HFSCatalogNodeID fileID;
  private final byte[] createDate = new byte[4];
  private final byte[] contentModDate = new byte[4];
  private final byte[] attributeModDate = new byte[4];
  private final byte[] accessDate = new byte[4];
  private final byte[] backupDate = new byte[4];
  private final HFSPlusBSDInfo permissions;
  private final FileInfo userInfo;
  private final ExtendedFileInfo finderInfo;
  private final byte[] textEncoding = new byte[4];
  private final byte[] reserved2 = new byte[4];
  private final HFSPlusForkData dataFork;
  private final HFSPlusForkData resourceFork;

  public HFSPlusCatalogFile(byte[] data, int offset) {
    System.arraycopy(data, offset + 0, recordType, 0, 2);
    System.arraycopy(data, offset + 2, flags, 0, 2);
    System.arraycopy(data, offset + 4, reserved1, 0, 4);
    fileID = new HFSCatalogNodeID(data, offset + 8);
    System.arraycopy(data, offset + 12, createDate, 0, 4);
    System.arraycopy(data, offset + 16, contentModDate, 0, 4);
    System.arraycopy(data, offset + 20, attributeModDate, 0, 4);
    System.arraycopy(data, offset + 24, accessDate, 0, 4);
    System.arraycopy(data, offset + 28, backupDate, 0, 4);
    permissions = new HFSPlusBSDInfo(data, offset + 32);
    userInfo = new FileInfo(data, offset + 48);
    finderInfo = new ExtendedFileInfo(data, offset + 64);
    System.arraycopy(data, offset + 80, textEncoding, 0, 4);
    System.arraycopy(data, offset + 84, reserved2, 0, 4);
    dataFork = new HFSPlusForkData(data, offset + 88);
    resourceFork = new HFSPlusForkData(data, offset + 168);
  }

  public static int length() {
    return 248;
  }

  @Override
  public short getRecordType() {
    return Util.readShortBE(recordType);
  }

  @Override
  public short getFlags() {
    return Util.readShortBE(flags);
  }

  public int getReserved1() {
    return Util.readIntBE(reserved1);
  }

  public HFSCatalogNodeID getFileID() {
    return fileID;
  }

  @Override
  public int getCreateDate() {
    return Util.readIntBE(createDate);
  }

  @Override
  public int getContentModDate() {
    return Util.readIntBE(contentModDate);
  }

  @Override
  public int getAttributeModDate() {
    return Util.readIntBE(attributeModDate);
  }

  @Override
  public int getAccessDate() {
    return Util.readIntBE(accessDate);
  }

  @Override
  public int getBackupDate() {
    return Util.readIntBE(backupDate);
  }

  @Override
  public HFSPlusBSDInfo getPermissions() {
    return permissions;
  }

  public FileInfo getUserInfo() {
    return userInfo;
  }

  public ExtendedFileInfo getFinderInfo() {
    return finderInfo;
  }

  @Override
  public int getTextEncoding() {
    return Util.readIntBE(textEncoding);
  }

  public int getReserved2() {
    return Util.readIntBE(reserved2);
  }

  public HFSPlusForkData getDataFork() {
    return dataFork;
  }

  public HFSPlusForkData getResourceFork() {
    return resourceFork;
  }

  /** File is locked and cannot be written to. */
  public boolean getFileLockedFlag() {
    return (getFlags() & kHFSFileLockedMask) != 0;
  }

  /** A file thread record exists for this file. */
  public boolean getThreadExistsFlag() {
    return (getFlags() & kHFSThreadExistsMask) != 0;
  }

  /** Object has extended attributes. */
  public boolean getHasAttributesFlag() {
    return (getFlags() & kHFSHasAttributesMask) != 0;
  }

  /** Object has security data (ACLs). */
  public boolean getHasSecurityMaskFlag() {
    return (getFlags() & kHFSHasSecurityMask) != 0;
  }

  /**
   * Only for HFSX, folder maintains a separate sub-folder count (sum of
   * folder records and directory hard links).
   */
  public boolean getHasFolderCountFlag() {
    return (getFlags() & kHFSHasFolderCountMask) != 0;
  }

  /** Has hardlink chain (inode or link). */
  public boolean getHasLinkChainFlag() {
    return (getFlags() & kHFSHasLinkChainMask) != 0;
  }

  /** Folder has a child that's a dir link. */
  public boolean getHasChildLinkFlag() {
    return (getFlags() & kHFSHasChildLinkMask) != 0;
  }

  /** File/Folder has the date-added stored in the finder info. */
  public boolean getHasDateAddedFlag() {
    return (getFlags() & kHFSHasDateAddedMask) != 0;
  }

  @Override
  public Date getCreateDateAsDate() {
    return HFSPlusDate.gmtTimestampToDate(getCreateDate());
  }

  @Override
  public Date getContentModDateAsDate() {
    return HFSPlusDate.gmtTimestampToDate(getContentModDate());
  }

  @Override
  public Date getAttributeModDateAsDate() {
    return HFSPlusDate.gmtTimestampToDate(getAttributeModDate());
  }

  @Override
  public Date getAccessDateAsDate() {
    return HFSPlusDate.gmtTimestampToDate(getAccessDate());
  }

  @Override
  public Date getBackupDateAsDate() {
    return HFSPlusDate.gmtTimestampToDate(getBackupDate());
  }


  /* @Override */
  @Override
  public Dictionary getStructElements() {
    DictionaryBuilder db = new DictionaryBuilder(HFSPlusCatalogFile.class.getSimpleName());

    db.addFlag("fileLocked", flags, kHFSFileLockedBit);
    db.addFlag("threadExists", flags, kHFSThreadExistsBit);
    db.addFlag("hasAttributes", flags, kHFSHasAttributesBit);
    db.addFlag("hasSecurity", flags, kHFSHasSecurityBit);
    db.addFlag("hasFolderCount", flags, kHFSHasFolderCountBit);
    db.addFlag("hasLinkChain", flags, kHFSHasLinkChainBit);
    db.addFlag("hasChildLink", flags, kHFSHasChildLinkBit);
    db.addFlag("hasDateAdded", flags, kHFSHasDateAddedBit);
    db.addUIntBE("reserved1", reserved1);
    db.add("fileID", fileID.getOpaqueStructElement());
    db.add("createDate", new HFSPlusDateField(createDate, false));
    db.add("contentModDate", new HFSPlusDateField(contentModDate, false));
    db.add("attributeModDate", new HFSPlusDateField(attributeModDate, false));
    db.add("accessDate", new HFSPlusDateField(accessDate, false));
    db.add("backupDate", new HFSPlusDateField(backupDate, false));
    db.add("permissions", permissions.getStructElements());
    db.add("userInfo", userInfo.getStructElements());
    db.add("finderInfo", finderInfo.getStructElements());
    db.addUIntBE("textEncoding", textEncoding);
    db.addUIntBE("reserved2", reserved2);
    db.add("dataFork", dataFork.getStructElements());
    db.add("resourceFork", resourceFork.getStructElements());

    return db.getResult();
  }

  @Override
  public void printFields(PrintStream ps, String prefix) {
    ps.println(prefix + " recordType: " + getRecordType());
    ps.println(prefix + " flags: " + getFlags());
    ps.println(prefix + " reserved1: " + getReserved1());
    ps.println(prefix + " fileID: ");
    getFileID().print(ps, prefix + "  ");
    ps.println(prefix + " createDate: " + getCreateDateAsDate());
    ps.println(prefix + " contentModDate: " + getContentModDateAsDate());
    ps.println(prefix + " attributeModDate: " + getAttributeModDateAsDate());
    ps.println(prefix + " accessDate: " + getAccessDateAsDate());
    ps.println(prefix + " backupDate: " + getBackupDateAsDate());
    ps.println(prefix + " permissions: ");
    getPermissions().print(ps, prefix + "  ");
    ps.println(prefix + " userInfo: ");
    getUserInfo().print(ps, prefix + "  ");
    ps.println(prefix + " finderInfo: ");
    getFinderInfo().print(ps, prefix + "  ");
    ps.println(prefix + " textEncoding: " + getTextEncoding());
    ps.println(prefix + " reserved2: " + getReserved2());
    ps.println(prefix + " dataFork: ");
    getDataFork().print(ps, prefix + "  ");
    ps.println(prefix + " resourceFork: ");
    getResourceFork().print(ps, prefix + "  ");
  }

  @Override
  public void print(PrintStream ps, String prefix) {
    ps.println(prefix + "HFSPlusCatalogFile:");
    printFields(ps, prefix);
  }

  @Override
  public byte[] getBytes() {
    byte[] result = new byte[length()];
    byte[] tempData;
    int offset = 0;

    System.arraycopy(recordType, 0, result, offset, recordType.length);
    offset += recordType.length;
    System.arraycopy(flags, 0, result, offset, flags.length);
    offset += flags.length;
    System.arraycopy(reserved1, 0, result, offset, reserved1.length);
    offset += reserved1.length;
    tempData = fileID.getBytes();
    System.arraycopy(tempData, 0, result, offset, tempData.length);
    offset += tempData.length;
    System.arraycopy(createDate, 0, result, offset, createDate.length);
    offset += createDate.length;
    System.arraycopy(contentModDate, 0, result, offset, contentModDate.length);
    offset += contentModDate.length;
    System.arraycopy(attributeModDate, 0, result, offset, attributeModDate.length);
    offset += attributeModDate.length;
    System.arraycopy(accessDate, 0, result, offset, accessDate.length);
    offset += accessDate.length;
    System.arraycopy(backupDate, 0, result, offset, backupDate.length);
    offset += backupDate.length;
    tempData = permissions.getBytes();
    System.arraycopy(tempData, 0, result, offset, tempData.length);
    offset += tempData.length;
    tempData = userInfo.getBytes();
    System.arraycopy(tempData, 0, result, offset, tempData.length);
    offset += tempData.length;
    tempData = finderInfo.getBytes();
    System.arraycopy(tempData, 0, result, offset, tempData.length);
    offset += tempData.length;
    System.arraycopy(textEncoding, 0, result, offset, textEncoding.length);
    offset += textEncoding.length;
    System.arraycopy(reserved2, 0, result, offset, reserved2.length);
    offset += reserved2.length;
    tempData = dataFork.getBytes();
    System.arraycopy(tempData, 0, result, offset, tempData.length);
    offset += tempData.length;
    tempData = resourceFork.getBytes();
    System.arraycopy(tempData, 0, result, offset, tempData.length);
    offset += tempData.length;

    return result;
  }

  /* @Override */
  @Override
  public int size() {
    return length();
  }
}
