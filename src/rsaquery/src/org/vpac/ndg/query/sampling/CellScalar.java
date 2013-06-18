package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.ScalarElement;

/**
 * Addresses a single cell in a dataset. This is a mutable type, effectively
 * allowing cells to be passed by reference.
 * @author Alex Fraser
 */
public class CellScalar implements Cell {

	private ScalarElement value;
	private Prototype prototype;
	private String name;

	public CellScalar(String name, Prototype prototype) {
		this.prototype = prototype;
		value = (ScalarElement) prototype.getElement().copy();
		value.setValid(false);
		this.name = name;
	}

	/**
	 * @return The value of the current cell in the dataset.
	 */
	public ScalarElement getScalar() {
		return value;
	}

    /**
     * Write into the dataset.
     * @param value The value to write.
     */
	public void setScalar(ScalarElement value) {
		this.value = value.copy();
	}

	@Override
	public ScalarElement get() {
		return getScalar();
	}

	@Override
	public void set(Element<?> value) throws ClassCastException {
		setScalar((ScalarElement)value);
	}

	@Override
	public void unset() {
		this.value.setValid(false);
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", name, value);
	}

	@Override
	public Prototype getPrototype() {
		return prototype;
	}

}
