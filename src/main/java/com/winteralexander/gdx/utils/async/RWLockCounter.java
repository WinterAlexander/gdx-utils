package com.winteralexander.gdx.utils.async;

import com.winteralexander.gdx.utils.Validation;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * {@link ReadWriteLock} that counts the number of threads trying to get in and
 * out. Very useful for debugging but shouldn't be used for production code.
 * <p>
 * Created on 2019-11-07.
 *
 * @author Alexander Winter
 */
public class RWLockCounter implements ReadWriteLock {
	private final LockCounter readLock, writeLock;

	public RWLockCounter(ReadWriteLock lock) {
		Validation.ensureNotNull(lock, "lock");

		readLock = new LockCounter(lock.readLock());
		writeLock = new LockCounter(lock.writeLock());
	}

	@Override
	public Lock readLock() {
		return readLock;
	}

	@Override
	public Lock writeLock() {
		return writeLock;
	}

	public int getReadCount() {
		return readLock.getCount();
	}

	public int getWriteCount() {
		return writeLock.getCount();
	}
}
