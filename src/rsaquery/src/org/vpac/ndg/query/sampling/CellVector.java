package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorElement;

/**
 * Addresses a single cell in a dataset. This is a mutable type, effectively
 * allowing cells to be passed by reference.
 * @author Alex Fraser
 */
public class CellVector implements Cell {

	private VectorElement value;
	private Prototype prototype;
	private String name;

	public CellVector(String name, Prototype prototype) {
		this.prototype = prototype;
		value = (VectorElement) prototype.getElement().copy();
		value.setValid(false);
		this.name = name;
	}

	/**
	 * @return The value of the current cell in the dataset.
	 */
	public VectorElement getVector() {
		return value;
	}

    /**
     * Write into the dataset.
     * @param value The value to write.
     */
	public void setVector(VectorElement value) {
		this.value = value.copy();
	}

	@Override
	public VectorElement get() {
		return getVector();
	}

	@Override
	public void set(Element<?> value) throws ClassCastException {
		setVector((VectorElement)value);
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
