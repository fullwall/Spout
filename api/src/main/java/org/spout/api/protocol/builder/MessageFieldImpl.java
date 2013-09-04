/*
 * This file is part of Spout.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spout is licensed under the Spout License Version 1.
 *
 * Spout is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Spout is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.api.protocol.builder;

import io.netty.buffer.ByteBuf;

public abstract class MessageFieldImpl implements MessageField {
	@Override
	public int getFixedLength() {
		return -1;
	}

	@Override
	public long readLong(ByteBuf buffer) {
		throw new UnsupportedOperationException("This field does not support long read");
	}

	@Override
	public int readInt(ByteBuf buffer) {
		throw new UnsupportedOperationException("This field does not support int read");
	}

	@Override
	public short readShort(ByteBuf buffer) {
		throw new UnsupportedOperationException("This field does not support short read");
	}

	@Override
	public byte readByte(ByteBuf buffer) {
		throw new UnsupportedOperationException("This field does not support byte read");
	}

	@Override
	public short readUnsignedByte(ByteBuf buffer) {
		throw new UnsupportedOperationException("This field does not support unsigned byte read");
	}

	@Override
	public void writeLong(ByteBuf buffer, long value) {
		throw new UnsupportedOperationException("This field does not support long write");
	}

	@Override
	public void writeInt(ByteBuf buffer, int value) {
		throw new UnsupportedOperationException("This field does not support int write");
	}

	@Override
	public void writeShort(ByteBuf buffer, short value) {
		throw new UnsupportedOperationException("This field does not support short write");
	}

	@Override
	public void writeByte(ByteBuf buffer, byte value) {
		throw new UnsupportedOperationException("This field does not support byte write");
	}

	@Override
	public void writeUnsignedByte(ByteBuf buffer, short value) {
		throw new UnsupportedOperationException("This field does not support unsigned byte write");
	}
}
