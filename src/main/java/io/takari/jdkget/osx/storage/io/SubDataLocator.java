/*-
 * Copyright (C) 2009 Erik Larsson
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

package io.takari.jdkget.osx.storage.io;

import io.takari.jdkget.osx.io.ConcatenatedStream;
import io.takari.jdkget.osx.io.RandomAccessStream;
import io.takari.jdkget.osx.io.ReadableConcatenatedStream;
import io.takari.jdkget.osx.io.ReadableRandomAccessStream;

/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
public class SubDataLocator extends DataLocator {
  private DataLocator source;
  private long offset;
  private long length;

  public SubDataLocator(DataLocator source, long offset, long length) {
    this.source = source;
    this.offset = offset;
    this.length = length;

    this.source.addReference(this);
  }

  @Override
  public ReadableRandomAccessStream createReadOnlyFile() {
    return new ReadableConcatenatedStream(source.createReadOnlyFile(), offset, length);
  }

  @Override
  public boolean isWritable() {
    return source.isWritable();
  }

  @Override
  public RandomAccessStream createReadWriteFile() throws UnsupportedOperationException {
    return new ConcatenatedStream(source.createReadWriteFile(), offset, length);
  }

  @Override
  public void releaseResources() {
    source.removeReference(this);
  }
}
