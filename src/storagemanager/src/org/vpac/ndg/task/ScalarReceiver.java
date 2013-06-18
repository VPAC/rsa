package org.vpac.ndg.task;

/**
 * A simple mutable wrapper object, to be used as a container for transferring
 * data between tasks. You could consider this class to be a collection of size
 * 1.
 *
 * @author adfries
 *
 * @param <T>
 *            The type that this container holds.
 */
public class ScalarReceiver <T> {

	private T value;

	public ScalarReceiver() {
		value = null;
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}

}
