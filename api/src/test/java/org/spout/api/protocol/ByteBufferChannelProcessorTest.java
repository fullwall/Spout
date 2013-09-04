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
package org.spout.api.protocol;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import org.spout.api.protocol.fake.ChannelHandlerContextFaker;

public class ByteBufferChannelProcessorTest {
	private final int LENGTH = 65536;
	Thread mainThread;

	@Test
	public void randomPassthrough() {

		mainThread = Thread.currentThread();

		ByteBuf buffer = Unpooled.buffer(2048);

		ChannelHandlerContext ctx = ChannelHandlerContextFaker.setup();

		ByteBufferChannelProcessor processor = new ByteBufferChannelProcessor(256);

		byte[] input = new byte[LENGTH];
		byte[] output = new byte[LENGTH];

		Random r = new Random();

		for (int i = 0; i < input.length; i++) {
			input[i] = (byte) (r.nextInt());
		}

		int writePointer = 0;
		int readPointer = 0;

		int pass = 0;

		while (writePointer < LENGTH && (pass++) < 512) {

			int toWrite = r.nextInt(512);

			if (r.nextInt(10) == 0) {
				// simulate "large" packets
				toWrite *= 10;
			}

			if (toWrite > buffer.writableBytes()) {
				toWrite = buffer.writableBytes();
			}
			if (toWrite > LENGTH - writePointer) {
				toWrite = LENGTH - writePointer;
			}

			System.out.println("Writing block of size " + toWrite);

			buffer.writeBytes(input, writePointer, toWrite);
			writePointer += toWrite;

			ByteBuf outputBuffer = processor.write(ctx, buffer);

			buffer.discardReadBytes();

			while (outputBuffer.isReadable()) {
				int toRead = r.nextInt(768);
				if (toRead > outputBuffer.readableBytes()) {
					toRead = outputBuffer.readableBytes();
				}
				System.out.println("ToRead: " + toRead + " of " + outputBuffer.readableBytes());
				outputBuffer.readBytes(output, readPointer, toRead);
				readPointer += toRead;
				outputBuffer.discardReadBytes();
			}
		}

		for (int i = 0; i < input.length; i++) {
			assertTrue("Mismatch at position " + i, input[i] == output[i]);
		}
	}
}
