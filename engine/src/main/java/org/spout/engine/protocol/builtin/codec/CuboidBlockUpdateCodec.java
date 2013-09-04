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
package org.spout.engine.protocol.builtin.codec;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufs;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.util.ByteBufUtils;
import org.spout.engine.protocol.builtin.message.CuboidBlockUpdateMessage;

/**
 *
 */
public class CuboidBlockUpdateCodec extends MessageCodec<CuboidBlockUpdateMessage> {
	public CuboidBlockUpdateCodec(int opcode) {
		super(CuboidBlockUpdateMessage.class, opcode);
	}

	@Override
	public ByteBuf encode(CuboidBlockUpdateMessage message) {
		ByteBuf buffer = ByteBufs.buffer(4 * 6 + message.getBlockTypes().length * 2 + message.getBlockData().length * 2 + message.getBlockLight().length + message.getSkyLight().length + ByteBufUtils.UUID_SIZE);
		buffer.writeInt(message.getMinX());
		buffer.writeInt(message.getMinY());
		buffer.writeInt(message.getMinZ());
		buffer.writeInt(message.getSizeX());
		buffer.writeInt(message.getSizeY());
		buffer.writeInt(message.getSizeZ());

		for (short s : message.getBlockTypes()) {
			buffer.writeShort(s);
		}
		for (short s : message.getBlockData()) {
			buffer.writeShort(s);
		}
		buffer.writeBytes(message.getBlockLight());
		buffer.writeBytes(message.getSkyLight());
		ByteBufUtils.writeUUID(buffer, message.getWorldUUID());
		return buffer;
	}

	@Override
	public CuboidBlockUpdateMessage decode(ByteBuf buffer) {
		final int minX = buffer.readInt();
		final int minY = buffer.readInt();
		final int minZ = buffer.readInt();
		final int sizeX = buffer.readInt();
		final int sizeY = buffer.readInt();
		final int sizeZ = buffer.readInt();
		final int volume = sizeX * sizeY * sizeZ;
		short[] blockTypes = new short[volume];
		short[] blockData = new short[volume];
		int lightArraySize = volume / 2;
		if ((volume & 1) != 0) {
			++lightArraySize;
		}
		byte[] blockLight = new byte[lightArraySize];
		byte[] skyLight = new byte[lightArraySize];
		for (int i = 0; i < blockTypes.length; ++i) {
			blockTypes[i] = buffer.readShort();
		}

		for (int i = 0; i < blockData.length; ++i) {
			blockData[i] = buffer.readShort();
		}

		buffer.readBytes(blockLight);
		buffer.readBytes(skyLight);
		final UUID world = ByteBufUtils.readUUID(buffer);

		return new CuboidBlockUpdateMessage(world, minX, minY, minZ, sizeX, sizeY, sizeZ, blockTypes, blockData, blockLight, skyLight);
	}
}
