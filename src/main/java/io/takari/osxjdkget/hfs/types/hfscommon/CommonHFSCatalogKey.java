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

package io.takari.osxjdkget.hfs.types.hfscommon;

import java.io.PrintStream;

import io.takari.osxjdkget.csjc.StructElements;
import io.takari.osxjdkget.csjc.structelements.Dictionary;
import io.takari.osxjdkget.hfs.types.hfs.CatKeyRec;
import io.takari.osxjdkget.hfs.types.hfsplus.HFSPlusCatalogKey;

/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
public abstract class CommonHFSCatalogKey extends CommonBTKey<CommonHFSCatalogKey> implements StructElements {
  public abstract CommonHFSCatalogNodeID getParentID();

  public abstract CommonHFSCatalogString getNodeName();

  /* @Override */
  @Override
  public void print(PrintStream ps, String prefix) {
    ps.println(prefix + CommonHFSCatalogKey.class.getSimpleName() + ":");
    printFields(ps, prefix + " ");
  }

  public static CommonHFSCatalogKey create(CommonHFSCatalogNodeID parentID,
    CommonHFSCatalogString name) {
    final long parentIDLong = parentID.toLong();
    if (parentIDLong > 0xFFFFFFFFL) {
      throw new RuntimeException("Unexpected: UInt32 overflow in " +
        "value returned from CommonHFSCatalogNodeID.toLong " +
        "(" + parentIDLong + ").");
    }

    if (parentID instanceof CommonHFSCatalogNodeID.HFSImplementation &&
      name instanceof CommonHFSCatalogString.HFSImplementation) {
      byte[] nameBytes = name.getStringBytes();
      if (nameBytes.length > 31) {
        throw new RuntimeException("Name length too large for HFS " +
          "catalog record (name length: " + nameBytes.length +
          ", max: 31).");
      }

      return CommonHFSCatalogKey.create(new CatKeyRec((int) parentIDLong,
        nameBytes));
    } else if (parentID instanceof CommonHFSCatalogNodeID.HFSPlusImplementation &&
      name instanceof CommonHFSCatalogString.HFSPlusImplementation) {
      byte[] nameBytes = name.getStringBytes();
      if (nameBytes.length > 255) {
        throw new RuntimeException("Name length too large for HFS+ " +
          "catalog record (name length: " + nameBytes.length +
          ", max: 255).");
      }

      return new HFSPlusImplementation(new HFSPlusCatalogKey(
        ((CommonHFSCatalogNodeID.HFSPlusImplementation) parentID).getHFSCatalogNodeID(),
        ((CommonHFSCatalogString.HFSPlusImplementation) name).getInternal()));
    } else {
      throw new RuntimeException("Mismatching/unknown types for " +
        "parentID (" + parentID.getClass() + ") and name " +
        "(" + name.getClass() + ").");
    }
  }

  public static CommonHFSCatalogKey create(HFSPlusCatalogKey key) {
    return new HFSPlusImplementation(key);
  }

  /*
  public static CommonHFSCatalogKey create(HFSPlusCatalogKey key, HFSXKeyCompareType compType) {
      return new HFSXImplementation(key, compType);
  }
   * */

  public static CommonHFSCatalogKey create(CatKeyRec key) {
    return new HFSImplementation(key);
  }

  public static class HFSPlusImplementation extends CommonHFSCatalogKey {
    private final HFSPlusCatalogKey key;
    //private HFSXKeyCompareType compType;

    public HFSPlusImplementation(HFSPlusCatalogKey key) {
      //this(key, HFSXKeyCompareType.CASE_FOLDING);
      this.key = key;
    }
    /*
    protected HFSPlusImplementation(HFSPlusCatalogKey key,
            HFSXKeyCompareType compType) {
        this.key = key;
        this.compType = compType;
    }
    */

    @Override
    public CommonHFSCatalogNodeID getParentID() {
      return CommonHFSCatalogNodeID.create(key.getParentID());
    }

    @Override
    public CommonHFSCatalogString getNodeName() {
      return CommonHFSCatalogString.createHFSPlus(key.getNodeName());
    }

    @Override
    public byte[] getBytes() {
      return key.getBytes();
    }

    /* @Override */
    @Override
    public int compareTo(CommonHFSCatalogKey o) {
      if (o instanceof HFSPlusImplementation) {
        HFSPlusImplementation k = (HFSPlusImplementation) o;
        return key.compareTo(k.key);
        /*
        long res = getParentID().toLong() - k.getParentID().toLong();
        if(res == 0) {
            switch(compType) {
                case CASE_FOLDING:
                    return FastUnicodeCompare.compare(key.getNodeName().getUnicode(),
                            k.key.getNodeName().getUnicode());
                case BINARY_COMPARE:
                    return Util.unsignedArrayCompareLex(key.getNodeName().getUnicode(),
                            k.key.getNodeName().getUnicode());
                default:
                    throw new RuntimeException("Invalid value in file system structure! " +
                            "Compare type = " + compType);
            }
        }
        else if(res > 0)
            return 1;
        else
            return -1;
        */
      } else {
        throw new RuntimeException("Can't compare a " + o.getClass() +
          " with a " + this.getClass());
      }
    }

    /* @Override */
    @Override
    public int maxSize() {
      return key.maxSize();
    }

    /* @Override */
    @Override
    public int occupiedSize() {
      return key.occupiedSize();
    }

    /* @Override */
    @Override
    public void printFields(PrintStream ps, String prefix) {
      ps.println(prefix + "key:");
      key.print(ps, prefix + " ");
    }

    /* @Override */
    @Override
    public Dictionary getStructElements() {
      return key.getStructElements();
    }
  }

  /*
  public static class HFSXImplementation extends HFSPlusImplementation {
      public HFSXImplementation(HFSPlusCatalogKey key,
              HFSXKeyCompareType compType) {
          super(key, compType);
      }
  }
  */

  public static class HFSImplementation extends CommonHFSCatalogKey {
    private final CatKeyRec key;

    public HFSImplementation(CatKeyRec key) {
      this.key = key;
    }

    @Override
    public CommonHFSCatalogNodeID getParentID() {
      return CommonHFSCatalogNodeID.create(key.getCkrParID());
    }

    @Override
    public CommonHFSCatalogString getNodeName() {
      return CommonHFSCatalogString.createHFS(key.getCkrCName());
    }

    /* @Override */
    @Override
    public int maxSize() {
      return key.maxSize();
    }

    /* @Override */
    @Override
    public int occupiedSize() {
      return key.occupiedSize();
    }

    @Override
    public byte[] getBytes() {
      return key.getBytes();
    }

    /* @Override */
    @Override
    public int compareTo(CommonHFSCatalogKey o) {
      if (o instanceof HFSImplementation) {
        return key.compareTo(((HFSImplementation) o).key);
      } else {
        throw new RuntimeException("Can't compare a " + o.getClass() +
          " with a " + this.getClass());
      }
    }

    /* @Override */
    @Override
    public void printFields(PrintStream ps, String prefix) {
      ps.println(prefix + "key:");
      key.print(ps, prefix + " ");
    }

    /* @Override */
    @Override
    public Dictionary getStructElements() {
      return key.getStructElements();
    }
  }
}
