/*-
 * Copyright (C) 2008-2009 Erik Larsson
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

package io.takari.osxjdkget.storage.fs.hfscommon;

import io.takari.osxjdkget.hfs.types.hfscommon.CommonHFSCatalogFileRecord;
import io.takari.osxjdkget.hfs.types.hfscommon.CommonHFSCatalogFolder;
import io.takari.osxjdkget.hfs.types.hfscommon.CommonHFSCatalogFolderRecord;
import io.takari.osxjdkget.hfs.types.hfscommon.CommonHFSCatalogLeafRecord;
import io.takari.osxjdkget.hfs.types.hfscommon.CommonHFSCatalogNodeID;
import io.takari.osxjdkget.storage.fs.FSAttributes;
import io.takari.osxjdkget.storage.fs.FSEntry;
import io.takari.osxjdkget.storage.fs.FSFolder;
import io.takari.osxjdkget.storage.fs.FSFork;

/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
public class HFSCommonFSFolder extends HFSCommonFSEntry implements FSFolder {
  /**
   * The record from which this file was referenced. In the case of a
   * non-hardlinked directory, this variable is equal to
   * <code>folderRecord</code>.
   * The key record supplies the name/location of the folder, but all other
   * data is taken from <code>folderRecord</code>.
   */
  private final CommonHFSCatalogLeafRecord keyRecord;

  /**
   * The folder record, from which folder data and attributes are retrieved.
   * Could be called the 'inode' although it's not really proper in regard to
   * the structure of HFS.
   */
  private final CommonHFSCatalogFolderRecord folderRecord;

  private final HFSCommonFSAttributes attributes;

  public HFSCommonFSFolder(HFSCommonFileSystemHandler iParent, CommonHFSCatalogFolderRecord iFolderRecord) {
    this(iParent, null, iFolderRecord);
  }

  HFSCommonFSFolder(HFSCommonFileSystemHandler iParent, CommonHFSCatalogFileRecord iHardLinkFileRecord, CommonHFSCatalogFolderRecord iFolderRecord) {
    super(iParent, iFolderRecord.getData());

    // Input check
    if (iParent == null)
      throw new IllegalArgumentException("iParent must not be null!");
    if (iFolderRecord == null)
      throw new IllegalArgumentException("iFolderRecord must not be null!");

    if (iHardLinkFileRecord != null)
      this.keyRecord = iHardLinkFileRecord;
    else
      this.keyRecord = iFolderRecord;
    this.folderRecord = iFolderRecord;

    this.attributes =
      new HFSCommonFSAttributes(this, folderRecord.getData());
  }

  /* @Override */
  @Override
  public String[] list() {
    return fsHandler.listNames(folderRecord);
  }

  /* @Override */
  @Override
  public FSEntry[] listEntries() {
    return fsHandler.listFSEntries(folderRecord);
  }

  /* @Override */
  @Override
  public FSEntry getChild(String name) {
    return fsHandler.getEntry(folderRecord, name);
  }

  /* @Override */
  @Override
  public long getValence() {
    return folderRecord.getData().getValence();
  }

  /* @Override */
  @Override
  public FSAttributes getAttributes() {
    return attributes;
  }

  /* @Override */
  @Override
  public String getName() {
    return fsHandler.getProperNodeName(keyRecord);
  }

  /*
  @Override
  public FSFolder getParent() {
      return fsHandler.lookupParentFolder(keyRecord);
  }
   * */

  /* @Override */
  @Override
  public boolean isCompressed() {
    return false;
  }

  /* @Override */
  @Override
  protected CommonHFSCatalogNodeID getCatalogNodeID() {
    return folderRecord.getData().getFolderID();
  }

  /* @Override */
  @Override
  protected FSFork getResourceFork() {
    return null;
  }

  public CommonHFSCatalogFolder getInternalCatalogFolder() {
    return folderRecord.getData();
  }

  CommonHFSCatalogFolderRecord getInternalCatalogFolderRecord() {
    return folderRecord;
  }
}
