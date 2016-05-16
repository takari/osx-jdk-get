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

package io.takari.osxjdkget.storage.ps.ebr.types;

import java.io.PrintStream;
import java.util.LinkedList;

import io.takari.osxjdkget.io.ReadableRandomAccessStream;
import io.takari.osxjdkget.storage.ps.Partition;
import io.takari.osxjdkget.storage.ps.ebr.ExtendedBootRecord;
import io.takari.osxjdkget.storage.ps.legacy.PartitionSystem;

/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
public class DOSExtendedPartitionSystem implements PartitionSystem {
  private final ExtendedBootRecord[] extendedBootRecords;

  public DOSExtendedPartitionSystem(ReadableRandomAccessStream llf, long extendedPartitionOffset,
    long extendedPartitionLength, int sectorSize) {
    // System.err.println("creating a new DOSExtendedPartitionSystem with:");
    // System.err.println("  extendedPartitionOffset=" + extendedPartitionOffset);
    // System.err.println("  extendedPartitionLength=" + extendedPartitionLength);
    // System.err.println("  sectorSize=" + sectorSize);
    LinkedList<ExtendedBootRecord> recordList = new LinkedList<ExtendedBootRecord>();
    byte[] block = new byte[sectorSize];
    long seekLocation = extendedPartitionOffset;
    if (seekLocation > extendedPartitionOffset + extendedPartitionLength)
      throw new RuntimeException("Invalid DOS Extended partition system (seekLocation=" + seekLocation + ").");
    llf.seek(seekLocation);
    llf.readFully(block);
    ExtendedBootRecord ebr = new ExtendedBootRecord(block, 0, extendedPartitionOffset, seekLocation, sectorSize);
    recordList.addLast(ebr);
    int i = 0;
    // System.err.println("ebr[" + i++ + "]:");
    // ebr.print(System.err, " ");
    while (ebr.getSecondEntry().getLBAPartitionLength() != 0 ||
      ebr.getSecondEntry().getLBAFirstSector() != 0) {
      seekLocation = ebr.getSecondEntry().getStartOffset();
      if (seekLocation > extendedPartitionOffset + extendedPartitionLength)
        throw new RuntimeException("Invalid DOS Extended partition system (seekLocation=" + seekLocation + ").");
      llf.seek(seekLocation);
      llf.readFully(block);
      ebr = new ExtendedBootRecord(block, 0, extendedPartitionOffset, seekLocation, sectorSize);
      recordList.addLast(ebr);
      // System.err.println("ebr[" + i++ + "]:");
      // ebr.print(System.err, " ");
    }

    this.extendedBootRecords = recordList.toArray(new ExtendedBootRecord[recordList.size()]);
  }

  @Override
  public boolean isValid() {
    for (ExtendedBootRecord ebr : extendedBootRecords) {
      if (!ebr.isValid())
        return false;
    }
    return true;
  }

  @Override
  public int getPartitionCount() {
    return extendedBootRecords.length;
  }

  @Override
  public int getUsedPartitionCount() {
    // There shouldn't be any superfluous entries as it would be meaningless
    return getPartitionCount();
  }

  @Override
  public Partition getPartitionEntry(int index) {
    return extendedBootRecords[index].getFirstEntry();
  }

  @Override
  public Partition[] getPartitionEntries() {
    Partition[] result = new Partition[extendedBootRecords.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = extendedBootRecords[i].getFirstEntry();
    }
    return result;
  }

  @Override
  public Partition[] getUsedPartitionEntries() {
    return getPartitionEntries();
  }

  @Override
  public String getLongName() {
    return "DOS Extended";
  }

  @Override
  public String getShortName() {
    return "EBR";
  }

  @Override
  public void printFields(PrintStream ps, String prefix) {
    ps.println(prefix + " extendedBootRecords:");
    for (int i = 0; i < extendedBootRecords.length; ++i) {
      ps.println(prefix + "  [" + i + "]:");
      extendedBootRecords[i].print(ps, prefix + "   ");
    }
  }

  @Override
  public void print(PrintStream ps, String prefix) {
    ps.println(prefix + this.getClass().getSimpleName() + ":");
    printFields(ps, prefix);
  }
}
