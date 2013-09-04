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

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;

public abstract class GenericMessage<T extends Message> extends MessageCodec<T> implements Message {
	protected ByteBuf buffer;

	public GenericMessage(Class<T> clazz, int opcode) {
		super(clazz, opcode);
	}

	/**
	 * Gets the field root for this message.  This should be a static final unchanging array.
	 */
	public abstract CompoundMessageField getFieldRoot();

	public CompoundMessageField getToClientFieldRoot() {
		return getFieldRoot();
	}

	public CompoundMessageField getToServerFieldRoot() {
		return getFieldRoot();
	}

	/**
	 * Gets the field loop up table for the message
	 */
	public abstract int[] getFieldLoopup();

	@SuppressWarnings ({"unchecked", "hiding"})
	public <T> T get(FieldRef<T> ref) {
		setupBuffer(ref);

		CompoundMessageField f = getFieldRoot();

		return (T) f.read(this.buffer);
	}

	public long getLong(FieldRef<Long> ref) {
		setupBuffer(ref);

		CompoundMessageField f = getFieldRoot();

		return f.readLong(this.buffer);
	}

	public int getInt(FieldRef<Integer> ref) {
		setupBuffer(ref);

		CompoundMessageField f = getFieldRoot();

		return f.readInt(this.buffer);
	}

	public short getShort(FieldRef<Integer> ref) {
		setupBuffer(ref);

		CompoundMessageField f = getFieldRoot();

		return f.readShort(this.buffer);
	}

	public byte getByte(FieldRef<Byte> ref) {
		setupBuffer(ref);

		CompoundMessageField f = getFieldRoot();

		return f.readByte(this.buffer);
	}

	public short getUnsignedByte(FieldRef<Short> ref) {
		setupBuffer(ref);

		CompoundMessageField f = getFieldRoot();

		return f.readUnsignedByte(this.buffer);
	}

	private void setupBuffer(FieldRef<?> ref) {
		int index = ref.getIndex();
		this.buffer.readerIndex(getFieldLoopup()[index]);
	}

	@Override
	public ByteBuf encode(T message) throws IOException {
		return this.buffer;
	}

	@Override
	@SuppressWarnings ("unchecked")
	public T decode(ByteBuf b) throws IOException {
		CompoundMessageField root = Spout.getPlatform() == Platform.CLIENT ? getToClientFieldRoot() : getToServerFieldRoot();
		int start = b.readerIndex();
		int fieldCount = root.getSubFieldCount();
		int[] indexArray = new int[fieldCount];
		int length = root.skip(b, indexArray);
		this.buffer = Unpooled.buffer(length);
		b.getBytes(start, this.buffer, 0, length);
		return (T) this;
	}
}
