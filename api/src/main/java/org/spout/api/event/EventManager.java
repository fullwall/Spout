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
package org.spout.api.event;

/**
 * Manages event registration through {@link Listener}s and {@link EventExecutor}s,  It also handles calling of events, and delayed events.
 */
public interface EventManager {
	/**
	 * Calls an event with the given details
	 *
	 * @param event Event details
	 * @return Called event
	 */
	public <T extends Event> T callEvent(T event);

	/**
	 * Calls an event with the given details, on the next tick
	 *
	 * @param event Event details
	 */
	public <T extends Event> void callDelayedEvent(T event);

	/**
	 * Unregisters all the events in the given listener class
	 *
	 * @param listener Listener to unregister
	 */
	public void unRegisterEvents(Listener listener);

	/**
	 * Registers all the events in the given listener class
	 *
	 * @param listener Listener to register
	 * @param owner Plugin to register
	 */
	public void registerEvents(Listener listener, Object owner);

	/**
	 * Registers the specified executor to the given event class
	 *
	 * @param event Event type to register
	 * @param priority Priority to register this event at
	 * @param executor EventExecutor to register
	 * @param owner Plugin to register
	 */
	public void registerEvent(Class<? extends Event> event, Order priority, EventExecutor executor, Object owner);
}
