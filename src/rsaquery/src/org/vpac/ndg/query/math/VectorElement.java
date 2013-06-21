
package org.vpac.ndg.query.math;

import java.util.Arrays;

/**
 * A pixel that stores multiple values.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See VectorElement_gen.py.
public class VectorElement implements Element<VectorElement> {

	private ScalarElement[] components;

	/**
	 * Create a new vector as the concatenation of a collection of scalar
	 * elements.
	 * @param other The elements to concatenate. These will be copied.
	 */
	public VectorElement(ScalarElement... other) {
		this.components = new ScalarElement[other.length];
		for (int i = 0; i < other.length; i++)
			this.components[i] = (ScalarElement)other[i].copy();
	}

	private VectorElement(int capacity) {
		this.components = new ScalarElement[capacity];
	}

	/**
	 * Create a new vector of {@link ElementLong ElementLongs}.
	 * @param capacity The number of components.
	 * @param fillValue The initial value to assign to the components.
	 * @return The new vector.
	 */
	public static VectorElement createInt(int capacity, long fillValue) {
		ScalarElement[] components = new ScalarElement[capacity];
		for (int i = 0; i < capacity; i++) {
			ScalarElement elem = new ElementLong(fillValue);
			components[i] = elem;
		}
		return new VectorElement(components);
	}

	/**
	 * Create a new vector of {@link ElementDouble ElementDoubles}.
	 * @param capacity The number of components.
	 * @param fillValue The initial value to assign to the components.
	 * @return The new vector.
	 */
	public static VectorElement createReal(int capacity, double fillValue) {
		ScalarElement[] components = new ScalarElement[capacity];
		for (int i = 0; i < capacity; i++) {
			ScalarElement elem = new ElementDouble(fillValue);
			components[i] = elem;
		}
		return new VectorElement(components);
	}

	public static VectorElement create(int capacity, Type type) {
		VectorElement elem = new VectorElement(capacity);
		for (int i = 0; i < capacity; i++) {
			elem.components[i] = type.getElement().copy();
			elem.components[i].set(0);
		}
		return elem;
	}

	public static VectorElement create(Type[] types) {
		VectorElement elem = new VectorElement(types.length);
		for (int i = 0; i < elem.components.length; i++) {
			elem.components[i] = types[i].getElement().copy();
			elem.components[i].set(0);
		}
		return elem;
	}

	@Override
	public VectorElement copy() {
		return new VectorElement(components);
	}

	@Override
	public boolean isValid() {
		for (ScalarElement c : components) {
			if (!c.isValid())
				return false;
		}
		return true;
	}
	@Override
	public void setValid(boolean valid) {
		for (ScalarElement c : components)
			c.setValid(valid);
	}
	@Override
	public void setValid(Element<?> mask) {
		if (mask.getClass() == VectorElement.class)
			setValid((VectorElement) mask);
		else
			setValid((ScalarElement) mask);
	}
	public void setValid(ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].setValid(mask.isValid());
	}
	public void setValid(VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].setValid(mask.components[i].isValid());
	}
	@Override
	public void setValidIfValid(Element<?> mask) {
		if (mask.getClass() == VectorElement.class)
			setValidIfValid((VectorElement) mask);
		else
			setValidIfValid((ScalarElement) mask);
	}
	public void setValidIfValid(ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].setValidIfValid(mask);
	}
	public void setValidIfValid(VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].setValidIfValid(mask.components[i]);
	}

	@Override
	public ScalarElement[] getComponents() {
		return components;
	}

	// CASTING

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		boolean first = true;
		for (ScalarElement e : components){
			if (!first)
				sb.append(", ");
			else
				first = false;
			sb.append(e);
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @param i The index of the component to fetch.
	 * @return The component at the specified index.
	 */
	public ScalarElement get(int i) {
		return components[i];
	}

	/**
	 * @param i The index of the component to set.
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 * @return The component at the specified index.
	 */
	public ScalarElement set(int i, ScalarElement value) {
		return components[i].set(value);
	}

	/**
	 * @return The number of components in this vector.
	 */
	public int size() {
		return components.length;
	}

	@Override
	public VectorElement set(Element<?> value) {
		if (value.getClass() == VectorElement.class) {
			set((VectorElement) value);
		} else {
			set((ScalarElement) value);
		}
		return this;
	}

	public VectorElement set(ScalarElement value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}

	public VectorElement set(VectorElement value) {
		for (int i = 0; i < components.length; i++)
			components[i].set(value.components[i]);
		return this;
	}

	@Override
	public VectorElement set(Number value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}

	/**
	 * @return The component that represents time. This is always the first
	 * element.
	 */
	public ScalarElement getT() {
		return components[0];
	}
	/**
	 * Set the time component (first).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setT(ScalarElement value) {
		components[0].set(value);
	}
	/**
	 * Set the time component (first).
	 * @param value The value to assign to the component.
	 */
	public void setT(Number value) {
		components[0].set(value);
	}

	@Override
	public VectorElement set(byte value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}
	@Override
	public VectorElement asByte() {
		VectorElement res = new VectorElement();
		res.components = new ScalarElement[components.length];
		for (int i = 0; i < components.length; i++)
			res.components[i] = (ScalarElement) components[i].asByte();
		return res;
	}

	@Override
	public VectorElement set(short value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}
	@Override
	public VectorElement asShort() {
		VectorElement res = new VectorElement();
		res.components = new ScalarElement[components.length];
		for (int i = 0; i < components.length; i++)
			res.components[i] = (ScalarElement) components[i].asShort();
		return res;
	}

	@Override
	public VectorElement set(int value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}
	@Override
	public VectorElement asInt() {
		VectorElement res = new VectorElement();
		res.components = new ScalarElement[components.length];
		for (int i = 0; i < components.length; i++)
			res.components[i] = (ScalarElement) components[i].asInt();
		return res;
	}

	@Override
	public VectorElement set(long value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}
	@Override
	public VectorElement asLong() {
		VectorElement res = new VectorElement();
		res.components = new ScalarElement[components.length];
		for (int i = 0; i < components.length; i++)
			res.components[i] = (ScalarElement) components[i].asLong();
		return res;
	}

	@Override
	public VectorElement set(float value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}
	@Override
	public VectorElement asFloat() {
		VectorElement res = new VectorElement();
		res.components = new ScalarElement[components.length];
		for (int i = 0; i < components.length; i++)
			res.components[i] = (ScalarElement) components[i].asFloat();
		return res;
	}

	@Override
	public VectorElement set(double value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}
	@Override
	public VectorElement asDouble() {
		VectorElement res = new VectorElement();
		res.components = new ScalarElement[components.length];
		for (int i = 0; i < components.length; i++)
			res.components[i] = (ScalarElement) components[i].asDouble();
		return res;
	}

	private final static int OFFSET_X = 0 + 1;
	/**
	 * @return The X component (last; synonym for a).
	 */
	public ScalarElement getX() {
		return components[components.length - OFFSET_X];
	}
	/**
	 * Set the X component (last; synonym for a).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setX(ScalarElement value) {
		components[components.length - OFFSET_X].set(value);
	}
	/**
	 * Set the X component (last; synonym for a).
	 * @param value The value to assign to the component.
	 */
	public void setX(Number value) {
		components[components.length - OFFSET_X].set(value);
	}

	private final static int OFFSET_Y = 1 + 1;
	/**
	 * @return The Y component (second last; synonym for b).
	 */
	public ScalarElement getY() {
		return components[components.length - OFFSET_Y];
	}
	/**
	 * Set the Y component (second last; synonym for b).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setY(ScalarElement value) {
		components[components.length - OFFSET_Y].set(value);
	}
	/**
	 * Set the Y component (second last; synonym for b).
	 * @param value The value to assign to the component.
	 */
	public void setY(Number value) {
		components[components.length - OFFSET_Y].set(value);
	}

	private final static int OFFSET_Z = 2 + 1;
	/**
	 * @return The Z component (third last; synonym for c).
	 */
	public ScalarElement getZ() {
		return components[components.length - OFFSET_Z];
	}
	/**
	 * Set the Z component (third last; synonym for c).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setZ(ScalarElement value) {
		components[components.length - OFFSET_Z].set(value);
	}
	/**
	 * Set the Z component (third last; synonym for c).
	 * @param value The value to assign to the component.
	 */
	public void setZ(Number value) {
		components[components.length - OFFSET_Z].set(value);
	}

	private final static int OFFSET_W = 3 + 1;
	/**
	 * @return The W component (fourth last; synonym for d).
	 */
	public ScalarElement getW() {
		return components[components.length - OFFSET_W];
	}
	/**
	 * Set the W component (fourth last; synonym for d).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setW(ScalarElement value) {
		components[components.length - OFFSET_W].set(value);
	}
	/**
	 * Set the W component (fourth last; synonym for d).
	 * @param value The value to assign to the component.
	 */
	public void setW(Number value) {
		components[components.length - OFFSET_W].set(value);
	}

	private final static int OFFSET_A = 0 + 1;
	/**
	 * @return The A component (last; synonym for x).
	 */
	public ScalarElement getA() {
		return components[components.length - OFFSET_A];
	}
	/**
	 * Set the A component (last; synonym for x).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setA(ScalarElement value) {
		components[components.length - OFFSET_A].set(value);
	}
	/**
	 * Set the A component (last; synonym for x).
	 * @param value The value to assign to the component.
	 */
	public void setA(Number value) {
		components[components.length - OFFSET_A].set(value);
	}

	private final static int OFFSET_B = 1 + 1;
	/**
	 * @return The B component (second last; synonym for y).
	 */
	public ScalarElement getB() {
		return components[components.length - OFFSET_B];
	}
	/**
	 * Set the B component (second last; synonym for y).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setB(ScalarElement value) {
		components[components.length - OFFSET_B].set(value);
	}
	/**
	 * Set the B component (second last; synonym for y).
	 * @param value The value to assign to the component.
	 */
	public void setB(Number value) {
		components[components.length - OFFSET_B].set(value);
	}

	private final static int OFFSET_C = 2 + 1;
	/**
	 * @return The C component (third last; synonym for z).
	 */
	public ScalarElement getC() {
		return components[components.length - OFFSET_C];
	}
	/**
	 * Set the C component (third last; synonym for z).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setC(ScalarElement value) {
		components[components.length - OFFSET_C].set(value);
	}
	/**
	 * Set the C component (third last; synonym for z).
	 * @param value The value to assign to the component.
	 */
	public void setC(Number value) {
		components[components.length - OFFSET_C].set(value);
	}

	private final static int OFFSET_D = 3 + 1;
	/**
	 * @return The D component (fourth last; synonym for w).
	 */
	public ScalarElement getD() {
		return components[components.length - OFFSET_D];
	}
	/**
	 * Set the D component (fourth last; synonym for w).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setD(ScalarElement value) {
		components[components.length - OFFSET_D].set(value);
	}
	/**
	 * Set the D component (fourth last; synonym for w).
	 * @param value The value to assign to the component.
	 */
	public void setD(Number value) {
		components[components.length - OFFSET_D].set(value);
	}

	private final static int OFFSET_E = 4 + 1;
	/**
	 * @return The E component (fifth last; synonym for None).
	 */
	public ScalarElement getE() {
		return components[components.length - OFFSET_E];
	}
	/**
	 * Set the E component (fifth last; synonym for None).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setE(ScalarElement value) {
		components[components.length - OFFSET_E].set(value);
	}
	/**
	 * Set the E component (fifth last; synonym for None).
	 * @param value The value to assign to the component.
	 */
	public void setE(Number value) {
		components[components.length - OFFSET_E].set(value);
	}

	private final static int OFFSET_F = 5 + 1;
	/**
	 * @return The F component (sixth last; synonym for None).
	 */
	public ScalarElement getF() {
		return components[components.length - OFFSET_F];
	}
	/**
	 * Set the F component (sixth last; synonym for None).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setF(ScalarElement value) {
		components[components.length - OFFSET_F].set(value);
	}
	/**
	 * Set the F component (sixth last; synonym for None).
	 * @param value The value to assign to the component.
	 */
	public void setF(Number value) {
		components[components.length - OFFSET_F].set(value);
	}

	private final static int OFFSET_G = 6 + 1;
	/**
	 * @return The G component (seventh last; synonym for None).
	 */
	public ScalarElement getG() {
		return components[components.length - OFFSET_G];
	}
	/**
	 * Set the G component (seventh last; synonym for None).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setG(ScalarElement value) {
		components[components.length - OFFSET_G].set(value);
	}
	/**
	 * Set the G component (seventh last; synonym for None).
	 * @param value The value to assign to the component.
	 */
	public void setG(Number value) {
		components[components.length - OFFSET_G].set(value);
	}

	private final static int OFFSET_H = 7 + 1;
	/**
	 * @return The H component (eighth last; synonym for None).
	 */
	public ScalarElement getH() {
		return components[components.length - OFFSET_H];
	}
	/**
	 * Set the H component (eighth last; synonym for None).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setH(ScalarElement value) {
		components[components.length - OFFSET_H].set(value);
	}
	/**
	 * Set the H component (eighth last; synonym for None).
	 * @param value The value to assign to the component.
	 */
	public void setH(Number value) {
		components[components.length - OFFSET_H].set(value);
	}

	private final static int OFFSET_I = 8 + 1;
	/**
	 * @return The I component (ninth last; synonym for None).
	 */
	public ScalarElement getI() {
		return components[components.length - OFFSET_I];
	}
	/**
	 * Set the I component (ninth last; synonym for None).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setI(ScalarElement value) {
		components[components.length - OFFSET_I].set(value);
	}
	/**
	 * Set the I component (ninth last; synonym for None).
	 * @param value The value to assign to the component.
	 */
	public void setI(Number value) {
		components[components.length - OFFSET_I].set(value);
	}

	private final static int OFFSET_J = 9 + 1;
	/**
	 * @return The J component (tenth last; synonym for None).
	 */
	public ScalarElement getJ() {
		return components[components.length - OFFSET_J];
	}
	/**
	 * Set the J component (tenth last; synonym for None).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setJ(ScalarElement value) {
		components[components.length - OFFSET_J].set(value);
	}
	/**
	 * Set the J component (tenth last; synonym for None).
	 * @param value The value to assign to the component.
	 */
	public void setJ(Number value) {
		components[components.length - OFFSET_J].set(value);
	}

	// ARITHMETIC

	// add

	@Override
	public VectorElement add(long other) {
		for (ScalarElement c : components)
			c.add(other);
		return this;
	}
	@Override
	public VectorElement add(double other) {
		for (ScalarElement c : components)
			c.add(other);
		return this;
	}
	@Override
	public VectorElement add(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			add((VectorElement) other);
		} else {
			add((ScalarElement) other);
		}
		return this;
	}
	public VectorElement add(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].add(other);
		return this;
	}
	public VectorElement add(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].add(other.components[i]);
		return this;
	}

	@Override
	public VectorElement addIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			addIfValid(other, (VectorElement) mask);
		} else {
			addIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement addIfValid(long other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other, mask);
		return this;
	}
	public VectorElement addIfValid(long other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			addIfValid(other, (VectorElement) mask);
		} else {
			addIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement addIfValid(double other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other, mask);
		return this;
	}
	public VectorElement addIfValid(double other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			addIfValid((VectorElement) other);
		} else {
			addIfValid((ScalarElement) other);
		}
		return this;
	}
	public VectorElement addIfValid(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other);
		return this;
	}
	public VectorElement addIfValid(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other.components[i]);
		return this;
	}
	@Override
	public VectorElement addIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return addIfValid((VectorElement) other, (VectorElement) mask);
			else
				return addIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return addIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return addIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement addIfValid(ScalarElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other, mask);
		return this;
	}
	public VectorElement addIfValid(ScalarElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other, mask.components[i]);
		return this;
	}
	public VectorElement addIfValid(VectorElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other.components[i], mask);
		return this;
	}
	public VectorElement addIfValid(VectorElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addIfValid(other.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement addNew(long other) {
		VectorElement res = copy();
		return res.add(other);
	}
	@Override
	public VectorElement addNew(double other) {
		VectorElement res = copy();
		return res.add(other);
	}
	@Override
	public VectorElement addNew(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return addNew((VectorElement) other);
		} else {
			return addNew((ScalarElement) other);
		}
	}
	public VectorElement addNew(ScalarElement other) {
		VectorElement res = copy();
		return res.add(other);
	}
	public VectorElement addNew(VectorElement other) {
		VectorElement res = copy();
		return res.add(other);
	}

	@Override
	public VectorElement addNewIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return addNewIfValid(other, (VectorElement) mask);
		} else {
			return addNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement addNewIfValid(long other, ScalarElement mask) {
		VectorElement res = copy();
		return res.addIfValid(other, mask);
	}
	public VectorElement addNewIfValid(long other, VectorElement mask) {
		VectorElement res = copy();
		return res.addIfValid(other, mask);
	}
	@Override
	public VectorElement addNewIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return addNewIfValid(other, (VectorElement) mask);
		} else {
			return addNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement addNewIfValid(double other, ScalarElement mask) {
		VectorElement res = copy();
		return res.addIfValid(other, mask);
	}
	public VectorElement addNewIfValid(double other, VectorElement mask) {
		VectorElement res = copy();
		return res.addIfValid(other, mask);
	}
	@Override
	public VectorElement addNewIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return addNewIfValid((VectorElement) other);
		} else {
			return addNewIfValid((ScalarElement) other);
		}
	}
	public VectorElement addNewIfValid(ScalarElement other) {
		VectorElement res = copy();
		return res.addIfValid(other);
	}
	public VectorElement addNewIfValid(VectorElement other) {
		VectorElement res = copy();
		return res.addIfValid(other);
	}
	@Override
	public VectorElement addNewIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return addNewIfValid((VectorElement) other, (VectorElement) mask);
			else
				return addNewIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return addNewIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return addNewIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement addNewIfValid(ScalarElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.addIfValid(other, mask);
	}
	public VectorElement addNewIfValid(ScalarElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.addIfValid(other, mask);
	}
	public VectorElement addNewIfValid(VectorElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.addIfValid(other, mask);
	}
	public VectorElement addNewIfValid(VectorElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.addIfValid(other, mask);
	}

	@Override
	public VectorElement addOf(long a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].addOf(a, b);
		return this;
	}
	@Override
	public VectorElement addOf(double a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].addOf(a, b);
		return this;
	}
	@Override
	public VectorElement addOf(long a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].addOf(a, b);
		return this;
	}
	@Override
	public VectorElement addOf(double a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].addOf(a, b);
		return this;
	}
	@Override
	public VectorElement addOf(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].addOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].addOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement addOf(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].addOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].addOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement addOf(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].addOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].addOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement addOf(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].addOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].addOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement addOf(Element<?> a, Element<?> b) {
		if (a.getClass() == VectorElement.class && b.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].addOf(av.components[i], bv.components[i]);
		} else if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].addOf(av.components[i], b);
		} else if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].addOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].addOf(a, b);
		}
		return this;
	}

	@Override
	public VectorElement addOfIfValid(long a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return addOfIfValid(a, b, (VectorElement) mask);
		} else {
			return addOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement addOfIfValid(long a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(long a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(double a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return addOfIfValid(a, b, (VectorElement) mask);
		} else {
			return addOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement addOfIfValid(double a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(double a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(long a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return addOfIfValid(a, b, (VectorElement) mask);
		} else {
			return addOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement addOfIfValid(long a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(long a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(double a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return addOfIfValid(a, b, (VectorElement) mask);
		} else {
			return addOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement addOfIfValid(double a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(double a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			return addOfIfValid((VectorElement) a, b);
		} else {
			return addOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement addOfIfValid(ScalarElement a, long b) {
		if (!a.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(VectorElement a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return addOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return addOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return addOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return addOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	public VectorElement addOfIfValid(ScalarElement a, long b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOf(a, b);
		return this;
	}
	public VectorElement addOfIfValid(ScalarElement a, long b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return addOfIfValid(a, (VectorElement) b);
		} else {
			return addOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement addOfIfValid(long a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(long a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return addOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return addOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return addOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return addOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement addOfIfValid(long a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(long a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(long a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement addOfIfValid(long a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			return addOfIfValid((VectorElement) a, b);
		} else {
			return addOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement addOfIfValid(ScalarElement a, double b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(ScalarElement a, double b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b, mask);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return addOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return addOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return addOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return addOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	@Override
	public VectorElement addOfIfValid(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return addOfIfValid(a, (VectorElement) b);
		} else {
			return addOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement addOfIfValid(double a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(double a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return addOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return addOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return addOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return addOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement addOfIfValid(double a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(double a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(double a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement addOfIfValid(double a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(Element<?> a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			if (a.getClass() == VectorElement.class)
				return addOfIfValid((VectorElement) a, (VectorElement) b);
			else
				return addOfIfValid((ScalarElement) a, (VectorElement) b);
		} else {
			if (a.getClass() == VectorElement.class)
				return addOfIfValid((VectorElement) a, (ScalarElement) b);
			else
				return addOfIfValid((ScalarElement) a, (ScalarElement) b);
		}
	}
	public VectorElement addOfIfValid(ScalarElement a, ScalarElement b) {
		if (!a.isValid() || !b.isValid())
			return this;
		return addOf(a, b);
	}
	public VectorElement addOfIfValid(ScalarElement a, VectorElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, ScalarElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	@Override
	public VectorElement addOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return addOfIfValid((VectorElement) a, (VectorElement) b, (VectorElement) mask);
				else
					return addOfIfValid((ScalarElement) a, (VectorElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return addOfIfValid((VectorElement) a, (VectorElement) b, (ScalarElement) mask);
				else
					return addOfIfValid((ScalarElement) a, (VectorElement) b, (ScalarElement) mask);
			}
		} else {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return addOfIfValid((VectorElement) a, (ScalarElement) b, (VectorElement) mask);
				else
					return addOfIfValid((ScalarElement) a, (ScalarElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return addOfIfValid((VectorElement) a, (ScalarElement) b, (ScalarElement) mask);
				else
					return addOfIfValid((ScalarElement) a, (ScalarElement) b, (ScalarElement) mask);
			}
		}
	}
	public VectorElement addOfIfValid(ScalarElement a, ScalarElement b, ScalarElement mask) {
		if (!a.isValid() || !b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOf(a, b);
		return this;
	}
	public VectorElement addOfIfValid(ScalarElement a, ScalarElement b, VectorElement mask) {
		if (!a.isValid() || !b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(ScalarElement a, VectorElement b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(ScalarElement a, VectorElement b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	public VectorElement addOfIfValid(VectorElement a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].addOfIfValid(a.components[i], b.components[i], mask.components[i]);
		return this;
	}

	// subtract

	@Override
	public VectorElement sub(long other) {
		for (ScalarElement c : components)
			c.sub(other);
		return this;
	}
	@Override
	public VectorElement sub(double other) {
		for (ScalarElement c : components)
			c.sub(other);
		return this;
	}
	@Override
	public VectorElement sub(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			sub((VectorElement) other);
		} else {
			sub((ScalarElement) other);
		}
		return this;
	}
	public VectorElement sub(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].sub(other);
		return this;
	}
	public VectorElement sub(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].sub(other.components[i]);
		return this;
	}

	@Override
	public VectorElement subIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			subIfValid(other, (VectorElement) mask);
		} else {
			subIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement subIfValid(long other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other, mask);
		return this;
	}
	public VectorElement subIfValid(long other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			subIfValid(other, (VectorElement) mask);
		} else {
			subIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement subIfValid(double other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other, mask);
		return this;
	}
	public VectorElement subIfValid(double other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			subIfValid((VectorElement) other);
		} else {
			subIfValid((ScalarElement) other);
		}
		return this;
	}
	public VectorElement subIfValid(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other);
		return this;
	}
	public VectorElement subIfValid(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other.components[i]);
		return this;
	}
	@Override
	public VectorElement subIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return subIfValid((VectorElement) other, (VectorElement) mask);
			else
				return subIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return subIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return subIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement subIfValid(ScalarElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other, mask);
		return this;
	}
	public VectorElement subIfValid(ScalarElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other, mask.components[i]);
		return this;
	}
	public VectorElement subIfValid(VectorElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other.components[i], mask);
		return this;
	}
	public VectorElement subIfValid(VectorElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subIfValid(other.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement subNew(long other) {
		VectorElement res = copy();
		return res.sub(other);
	}
	@Override
	public VectorElement subNew(double other) {
		VectorElement res = copy();
		return res.sub(other);
	}
	@Override
	public VectorElement subNew(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return subNew((VectorElement) other);
		} else {
			return subNew((ScalarElement) other);
		}
	}
	public VectorElement subNew(ScalarElement other) {
		VectorElement res = copy();
		return res.sub(other);
	}
	public VectorElement subNew(VectorElement other) {
		VectorElement res = copy();
		return res.sub(other);
	}

	@Override
	public VectorElement subNewIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return subNewIfValid(other, (VectorElement) mask);
		} else {
			return subNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement subNewIfValid(long other, ScalarElement mask) {
		VectorElement res = copy();
		return res.subIfValid(other, mask);
	}
	public VectorElement subNewIfValid(long other, VectorElement mask) {
		VectorElement res = copy();
		return res.subIfValid(other, mask);
	}
	@Override
	public VectorElement subNewIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return subNewIfValid(other, (VectorElement) mask);
		} else {
			return subNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement subNewIfValid(double other, ScalarElement mask) {
		VectorElement res = copy();
		return res.subIfValid(other, mask);
	}
	public VectorElement subNewIfValid(double other, VectorElement mask) {
		VectorElement res = copy();
		return res.subIfValid(other, mask);
	}
	@Override
	public VectorElement subNewIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return subNewIfValid((VectorElement) other);
		} else {
			return subNewIfValid((ScalarElement) other);
		}
	}
	public VectorElement subNewIfValid(ScalarElement other) {
		VectorElement res = copy();
		return res.subIfValid(other);
	}
	public VectorElement subNewIfValid(VectorElement other) {
		VectorElement res = copy();
		return res.subIfValid(other);
	}
	@Override
	public VectorElement subNewIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return subNewIfValid((VectorElement) other, (VectorElement) mask);
			else
				return subNewIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return subNewIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return subNewIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement subNewIfValid(ScalarElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.subIfValid(other, mask);
	}
	public VectorElement subNewIfValid(ScalarElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.subIfValid(other, mask);
	}
	public VectorElement subNewIfValid(VectorElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.subIfValid(other, mask);
	}
	public VectorElement subNewIfValid(VectorElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.subIfValid(other, mask);
	}

	@Override
	public VectorElement subOf(long a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].subOf(a, b);
		return this;
	}
	@Override
	public VectorElement subOf(double a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].subOf(a, b);
		return this;
	}
	@Override
	public VectorElement subOf(long a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].subOf(a, b);
		return this;
	}
	@Override
	public VectorElement subOf(double a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].subOf(a, b);
		return this;
	}
	@Override
	public VectorElement subOf(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].subOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].subOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement subOf(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].subOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].subOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement subOf(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].subOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].subOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement subOf(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].subOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].subOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement subOf(Element<?> a, Element<?> b) {
		if (a.getClass() == VectorElement.class && b.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].subOf(av.components[i], bv.components[i]);
		} else if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].subOf(av.components[i], b);
		} else if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].subOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].subOf(a, b);
		}
		return this;
	}

	@Override
	public VectorElement subOfIfValid(long a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return subOfIfValid(a, b, (VectorElement) mask);
		} else {
			return subOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement subOfIfValid(long a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(long a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(double a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return subOfIfValid(a, b, (VectorElement) mask);
		} else {
			return subOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement subOfIfValid(double a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(double a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(long a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return subOfIfValid(a, b, (VectorElement) mask);
		} else {
			return subOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement subOfIfValid(long a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(long a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(double a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return subOfIfValid(a, b, (VectorElement) mask);
		} else {
			return subOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement subOfIfValid(double a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(double a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			return subOfIfValid((VectorElement) a, b);
		} else {
			return subOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement subOfIfValid(ScalarElement a, long b) {
		if (!a.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(VectorElement a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return subOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return subOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return subOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return subOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	public VectorElement subOfIfValid(ScalarElement a, long b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOf(a, b);
		return this;
	}
	public VectorElement subOfIfValid(ScalarElement a, long b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return subOfIfValid(a, (VectorElement) b);
		} else {
			return subOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement subOfIfValid(long a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(long a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return subOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return subOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return subOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return subOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement subOfIfValid(long a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(long a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(long a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement subOfIfValid(long a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			return subOfIfValid((VectorElement) a, b);
		} else {
			return subOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement subOfIfValid(ScalarElement a, double b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(ScalarElement a, double b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b, mask);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return subOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return subOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return subOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return subOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	@Override
	public VectorElement subOfIfValid(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return subOfIfValid(a, (VectorElement) b);
		} else {
			return subOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement subOfIfValid(double a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(double a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return subOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return subOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return subOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return subOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement subOfIfValid(double a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(double a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(double a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement subOfIfValid(double a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(Element<?> a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			if (a.getClass() == VectorElement.class)
				return subOfIfValid((VectorElement) a, (VectorElement) b);
			else
				return subOfIfValid((ScalarElement) a, (VectorElement) b);
		} else {
			if (a.getClass() == VectorElement.class)
				return subOfIfValid((VectorElement) a, (ScalarElement) b);
			else
				return subOfIfValid((ScalarElement) a, (ScalarElement) b);
		}
	}
	public VectorElement subOfIfValid(ScalarElement a, ScalarElement b) {
		if (!a.isValid() || !b.isValid())
			return this;
		return subOf(a, b);
	}
	public VectorElement subOfIfValid(ScalarElement a, VectorElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, ScalarElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	@Override
	public VectorElement subOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return subOfIfValid((VectorElement) a, (VectorElement) b, (VectorElement) mask);
				else
					return subOfIfValid((ScalarElement) a, (VectorElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return subOfIfValid((VectorElement) a, (VectorElement) b, (ScalarElement) mask);
				else
					return subOfIfValid((ScalarElement) a, (VectorElement) b, (ScalarElement) mask);
			}
		} else {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return subOfIfValid((VectorElement) a, (ScalarElement) b, (VectorElement) mask);
				else
					return subOfIfValid((ScalarElement) a, (ScalarElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return subOfIfValid((VectorElement) a, (ScalarElement) b, (ScalarElement) mask);
				else
					return subOfIfValid((ScalarElement) a, (ScalarElement) b, (ScalarElement) mask);
			}
		}
	}
	public VectorElement subOfIfValid(ScalarElement a, ScalarElement b, ScalarElement mask) {
		if (!a.isValid() || !b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOf(a, b);
		return this;
	}
	public VectorElement subOfIfValid(ScalarElement a, ScalarElement b, VectorElement mask) {
		if (!a.isValid() || !b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(ScalarElement a, VectorElement b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(ScalarElement a, VectorElement b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	public VectorElement subOfIfValid(VectorElement a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].subOfIfValid(a.components[i], b.components[i], mask.components[i]);
		return this;
	}

	// multiply

	@Override
	public VectorElement mul(long other) {
		for (ScalarElement c : components)
			c.mul(other);
		return this;
	}
	@Override
	public VectorElement mul(double other) {
		for (ScalarElement c : components)
			c.mul(other);
		return this;
	}
	@Override
	public VectorElement mul(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			mul((VectorElement) other);
		} else {
			mul((ScalarElement) other);
		}
		return this;
	}
	public VectorElement mul(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].mul(other);
		return this;
	}
	public VectorElement mul(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].mul(other.components[i]);
		return this;
	}

	@Override
	public VectorElement mulIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			mulIfValid(other, (VectorElement) mask);
		} else {
			mulIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement mulIfValid(long other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other, mask);
		return this;
	}
	public VectorElement mulIfValid(long other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			mulIfValid(other, (VectorElement) mask);
		} else {
			mulIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement mulIfValid(double other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other, mask);
		return this;
	}
	public VectorElement mulIfValid(double other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			mulIfValid((VectorElement) other);
		} else {
			mulIfValid((ScalarElement) other);
		}
		return this;
	}
	public VectorElement mulIfValid(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other);
		return this;
	}
	public VectorElement mulIfValid(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other.components[i]);
		return this;
	}
	@Override
	public VectorElement mulIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return mulIfValid((VectorElement) other, (VectorElement) mask);
			else
				return mulIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return mulIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return mulIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement mulIfValid(ScalarElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other, mask);
		return this;
	}
	public VectorElement mulIfValid(ScalarElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other, mask.components[i]);
		return this;
	}
	public VectorElement mulIfValid(VectorElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other.components[i], mask);
		return this;
	}
	public VectorElement mulIfValid(VectorElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulIfValid(other.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement mulNew(long other) {
		VectorElement res = copy();
		return res.mul(other);
	}
	@Override
	public VectorElement mulNew(double other) {
		VectorElement res = copy();
		return res.mul(other);
	}
	@Override
	public VectorElement mulNew(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return mulNew((VectorElement) other);
		} else {
			return mulNew((ScalarElement) other);
		}
	}
	public VectorElement mulNew(ScalarElement other) {
		VectorElement res = copy();
		return res.mul(other);
	}
	public VectorElement mulNew(VectorElement other) {
		VectorElement res = copy();
		return res.mul(other);
	}

	@Override
	public VectorElement mulNewIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return mulNewIfValid(other, (VectorElement) mask);
		} else {
			return mulNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement mulNewIfValid(long other, ScalarElement mask) {
		VectorElement res = copy();
		return res.mulIfValid(other, mask);
	}
	public VectorElement mulNewIfValid(long other, VectorElement mask) {
		VectorElement res = copy();
		return res.mulIfValid(other, mask);
	}
	@Override
	public VectorElement mulNewIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return mulNewIfValid(other, (VectorElement) mask);
		} else {
			return mulNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement mulNewIfValid(double other, ScalarElement mask) {
		VectorElement res = copy();
		return res.mulIfValid(other, mask);
	}
	public VectorElement mulNewIfValid(double other, VectorElement mask) {
		VectorElement res = copy();
		return res.mulIfValid(other, mask);
	}
	@Override
	public VectorElement mulNewIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return mulNewIfValid((VectorElement) other);
		} else {
			return mulNewIfValid((ScalarElement) other);
		}
	}
	public VectorElement mulNewIfValid(ScalarElement other) {
		VectorElement res = copy();
		return res.mulIfValid(other);
	}
	public VectorElement mulNewIfValid(VectorElement other) {
		VectorElement res = copy();
		return res.mulIfValid(other);
	}
	@Override
	public VectorElement mulNewIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return mulNewIfValid((VectorElement) other, (VectorElement) mask);
			else
				return mulNewIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return mulNewIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return mulNewIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement mulNewIfValid(ScalarElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.mulIfValid(other, mask);
	}
	public VectorElement mulNewIfValid(ScalarElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.mulIfValid(other, mask);
	}
	public VectorElement mulNewIfValid(VectorElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.mulIfValid(other, mask);
	}
	public VectorElement mulNewIfValid(VectorElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.mulIfValid(other, mask);
	}

	@Override
	public VectorElement mulOf(long a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOf(a, b);
		return this;
	}
	@Override
	public VectorElement mulOf(double a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOf(a, b);
		return this;
	}
	@Override
	public VectorElement mulOf(long a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOf(a, b);
		return this;
	}
	@Override
	public VectorElement mulOf(double a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOf(a, b);
		return this;
	}
	@Override
	public VectorElement mulOf(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement mulOf(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement mulOf(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement mulOf(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement mulOf(Element<?> a, Element<?> b) {
		if (a.getClass() == VectorElement.class && b.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(av.components[i], bv.components[i]);
		} else if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(av.components[i], b);
		} else if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].mulOf(a, b);
		}
		return this;
	}

	@Override
	public VectorElement mulOfIfValid(long a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return mulOfIfValid(a, b, (VectorElement) mask);
		} else {
			return mulOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement mulOfIfValid(long a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(long a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(double a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return mulOfIfValid(a, b, (VectorElement) mask);
		} else {
			return mulOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement mulOfIfValid(double a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(double a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(long a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return mulOfIfValid(a, b, (VectorElement) mask);
		} else {
			return mulOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement mulOfIfValid(long a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(long a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(double a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return mulOfIfValid(a, b, (VectorElement) mask);
		} else {
			return mulOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement mulOfIfValid(double a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(double a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			return mulOfIfValid((VectorElement) a, b);
		} else {
			return mulOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement mulOfIfValid(ScalarElement a, long b) {
		if (!a.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(VectorElement a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return mulOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return mulOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return mulOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return mulOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	public VectorElement mulOfIfValid(ScalarElement a, long b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOf(a, b);
		return this;
	}
	public VectorElement mulOfIfValid(ScalarElement a, long b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return mulOfIfValid(a, (VectorElement) b);
		} else {
			return mulOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement mulOfIfValid(long a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(long a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return mulOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return mulOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return mulOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return mulOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement mulOfIfValid(long a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(long a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(long a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement mulOfIfValid(long a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			return mulOfIfValid((VectorElement) a, b);
		} else {
			return mulOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement mulOfIfValid(ScalarElement a, double b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(ScalarElement a, double b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b, mask);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return mulOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return mulOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return mulOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return mulOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	@Override
	public VectorElement mulOfIfValid(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return mulOfIfValid(a, (VectorElement) b);
		} else {
			return mulOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement mulOfIfValid(double a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(double a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return mulOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return mulOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return mulOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return mulOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement mulOfIfValid(double a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(double a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(double a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement mulOfIfValid(double a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(Element<?> a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			if (a.getClass() == VectorElement.class)
				return mulOfIfValid((VectorElement) a, (VectorElement) b);
			else
				return mulOfIfValid((ScalarElement) a, (VectorElement) b);
		} else {
			if (a.getClass() == VectorElement.class)
				return mulOfIfValid((VectorElement) a, (ScalarElement) b);
			else
				return mulOfIfValid((ScalarElement) a, (ScalarElement) b);
		}
	}
	public VectorElement mulOfIfValid(ScalarElement a, ScalarElement b) {
		if (!a.isValid() || !b.isValid())
			return this;
		return mulOf(a, b);
	}
	public VectorElement mulOfIfValid(ScalarElement a, VectorElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, ScalarElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	@Override
	public VectorElement mulOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return mulOfIfValid((VectorElement) a, (VectorElement) b, (VectorElement) mask);
				else
					return mulOfIfValid((ScalarElement) a, (VectorElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return mulOfIfValid((VectorElement) a, (VectorElement) b, (ScalarElement) mask);
				else
					return mulOfIfValid((ScalarElement) a, (VectorElement) b, (ScalarElement) mask);
			}
		} else {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return mulOfIfValid((VectorElement) a, (ScalarElement) b, (VectorElement) mask);
				else
					return mulOfIfValid((ScalarElement) a, (ScalarElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return mulOfIfValid((VectorElement) a, (ScalarElement) b, (ScalarElement) mask);
				else
					return mulOfIfValid((ScalarElement) a, (ScalarElement) b, (ScalarElement) mask);
			}
		}
	}
	public VectorElement mulOfIfValid(ScalarElement a, ScalarElement b, ScalarElement mask) {
		if (!a.isValid() || !b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOf(a, b);
		return this;
	}
	public VectorElement mulOfIfValid(ScalarElement a, ScalarElement b, VectorElement mask) {
		if (!a.isValid() || !b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(ScalarElement a, VectorElement b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(ScalarElement a, VectorElement b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	public VectorElement mulOfIfValid(VectorElement a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].mulOfIfValid(a.components[i], b.components[i], mask.components[i]);
		return this;
	}

	// divide

	@Override
	public VectorElement div(long other) {
		for (ScalarElement c : components)
			c.div(other);
		return this;
	}
	@Override
	public VectorElement div(double other) {
		for (ScalarElement c : components)
			c.div(other);
		return this;
	}
	@Override
	public VectorElement div(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			div((VectorElement) other);
		} else {
			div((ScalarElement) other);
		}
		return this;
	}
	public VectorElement div(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].div(other);
		return this;
	}
	public VectorElement div(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].div(other.components[i]);
		return this;
	}

	@Override
	public VectorElement divIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			divIfValid(other, (VectorElement) mask);
		} else {
			divIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement divIfValid(long other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other, mask);
		return this;
	}
	public VectorElement divIfValid(long other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			divIfValid(other, (VectorElement) mask);
		} else {
			divIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement divIfValid(double other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other, mask);
		return this;
	}
	public VectorElement divIfValid(double other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			divIfValid((VectorElement) other);
		} else {
			divIfValid((ScalarElement) other);
		}
		return this;
	}
	public VectorElement divIfValid(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other);
		return this;
	}
	public VectorElement divIfValid(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other.components[i]);
		return this;
	}
	@Override
	public VectorElement divIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return divIfValid((VectorElement) other, (VectorElement) mask);
			else
				return divIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return divIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return divIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement divIfValid(ScalarElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other, mask);
		return this;
	}
	public VectorElement divIfValid(ScalarElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other, mask.components[i]);
		return this;
	}
	public VectorElement divIfValid(VectorElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other.components[i], mask);
		return this;
	}
	public VectorElement divIfValid(VectorElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divIfValid(other.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement divNew(long other) {
		VectorElement res = copy();
		return res.div(other);
	}
	@Override
	public VectorElement divNew(double other) {
		VectorElement res = copy();
		return res.div(other);
	}
	@Override
	public VectorElement divNew(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return divNew((VectorElement) other);
		} else {
			return divNew((ScalarElement) other);
		}
	}
	public VectorElement divNew(ScalarElement other) {
		VectorElement res = copy();
		return res.div(other);
	}
	public VectorElement divNew(VectorElement other) {
		VectorElement res = copy();
		return res.div(other);
	}

	@Override
	public VectorElement divNewIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return divNewIfValid(other, (VectorElement) mask);
		} else {
			return divNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement divNewIfValid(long other, ScalarElement mask) {
		VectorElement res = copy();
		return res.divIfValid(other, mask);
	}
	public VectorElement divNewIfValid(long other, VectorElement mask) {
		VectorElement res = copy();
		return res.divIfValid(other, mask);
	}
	@Override
	public VectorElement divNewIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return divNewIfValid(other, (VectorElement) mask);
		} else {
			return divNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement divNewIfValid(double other, ScalarElement mask) {
		VectorElement res = copy();
		return res.divIfValid(other, mask);
	}
	public VectorElement divNewIfValid(double other, VectorElement mask) {
		VectorElement res = copy();
		return res.divIfValid(other, mask);
	}
	@Override
	public VectorElement divNewIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return divNewIfValid((VectorElement) other);
		} else {
			return divNewIfValid((ScalarElement) other);
		}
	}
	public VectorElement divNewIfValid(ScalarElement other) {
		VectorElement res = copy();
		return res.divIfValid(other);
	}
	public VectorElement divNewIfValid(VectorElement other) {
		VectorElement res = copy();
		return res.divIfValid(other);
	}
	@Override
	public VectorElement divNewIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return divNewIfValid((VectorElement) other, (VectorElement) mask);
			else
				return divNewIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return divNewIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return divNewIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement divNewIfValid(ScalarElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.divIfValid(other, mask);
	}
	public VectorElement divNewIfValid(ScalarElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.divIfValid(other, mask);
	}
	public VectorElement divNewIfValid(VectorElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.divIfValid(other, mask);
	}
	public VectorElement divNewIfValid(VectorElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.divIfValid(other, mask);
	}

	@Override
	public VectorElement divOf(long a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].divOf(a, b);
		return this;
	}
	@Override
	public VectorElement divOf(double a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].divOf(a, b);
		return this;
	}
	@Override
	public VectorElement divOf(long a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].divOf(a, b);
		return this;
	}
	@Override
	public VectorElement divOf(double a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].divOf(a, b);
		return this;
	}
	@Override
	public VectorElement divOf(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].divOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].divOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement divOf(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].divOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].divOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement divOf(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].divOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].divOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement divOf(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].divOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].divOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement divOf(Element<?> a, Element<?> b) {
		if (a.getClass() == VectorElement.class && b.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].divOf(av.components[i], bv.components[i]);
		} else if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].divOf(av.components[i], b);
		} else if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].divOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].divOf(a, b);
		}
		return this;
	}

	@Override
	public VectorElement divOfIfValid(long a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return divOfIfValid(a, b, (VectorElement) mask);
		} else {
			return divOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement divOfIfValid(long a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(long a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(double a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return divOfIfValid(a, b, (VectorElement) mask);
		} else {
			return divOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement divOfIfValid(double a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(double a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(long a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return divOfIfValid(a, b, (VectorElement) mask);
		} else {
			return divOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement divOfIfValid(long a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(long a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(double a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return divOfIfValid(a, b, (VectorElement) mask);
		} else {
			return divOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement divOfIfValid(double a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(double a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			return divOfIfValid((VectorElement) a, b);
		} else {
			return divOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement divOfIfValid(ScalarElement a, long b) {
		if (!a.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(VectorElement a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return divOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return divOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return divOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return divOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	public VectorElement divOfIfValid(ScalarElement a, long b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOf(a, b);
		return this;
	}
	public VectorElement divOfIfValid(ScalarElement a, long b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return divOfIfValid(a, (VectorElement) b);
		} else {
			return divOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement divOfIfValid(long a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(long a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return divOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return divOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return divOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return divOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement divOfIfValid(long a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(long a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(long a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement divOfIfValid(long a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			return divOfIfValid((VectorElement) a, b);
		} else {
			return divOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement divOfIfValid(ScalarElement a, double b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(ScalarElement a, double b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b, mask);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return divOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return divOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return divOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return divOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	@Override
	public VectorElement divOfIfValid(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return divOfIfValid(a, (VectorElement) b);
		} else {
			return divOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement divOfIfValid(double a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(double a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return divOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return divOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return divOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return divOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement divOfIfValid(double a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(double a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(double a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement divOfIfValid(double a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(Element<?> a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			if (a.getClass() == VectorElement.class)
				return divOfIfValid((VectorElement) a, (VectorElement) b);
			else
				return divOfIfValid((ScalarElement) a, (VectorElement) b);
		} else {
			if (a.getClass() == VectorElement.class)
				return divOfIfValid((VectorElement) a, (ScalarElement) b);
			else
				return divOfIfValid((ScalarElement) a, (ScalarElement) b);
		}
	}
	public VectorElement divOfIfValid(ScalarElement a, ScalarElement b) {
		if (!a.isValid() || !b.isValid())
			return this;
		return divOf(a, b);
	}
	public VectorElement divOfIfValid(ScalarElement a, VectorElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, ScalarElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	@Override
	public VectorElement divOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return divOfIfValid((VectorElement) a, (VectorElement) b, (VectorElement) mask);
				else
					return divOfIfValid((ScalarElement) a, (VectorElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return divOfIfValid((VectorElement) a, (VectorElement) b, (ScalarElement) mask);
				else
					return divOfIfValid((ScalarElement) a, (VectorElement) b, (ScalarElement) mask);
			}
		} else {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return divOfIfValid((VectorElement) a, (ScalarElement) b, (VectorElement) mask);
				else
					return divOfIfValid((ScalarElement) a, (ScalarElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return divOfIfValid((VectorElement) a, (ScalarElement) b, (ScalarElement) mask);
				else
					return divOfIfValid((ScalarElement) a, (ScalarElement) b, (ScalarElement) mask);
			}
		}
	}
	public VectorElement divOfIfValid(ScalarElement a, ScalarElement b, ScalarElement mask) {
		if (!a.isValid() || !b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOf(a, b);
		return this;
	}
	public VectorElement divOfIfValid(ScalarElement a, ScalarElement b, VectorElement mask) {
		if (!a.isValid() || !b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(ScalarElement a, VectorElement b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(ScalarElement a, VectorElement b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	public VectorElement divOfIfValid(VectorElement a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].divOfIfValid(a.components[i], b.components[i], mask.components[i]);
		return this;
	}

	// modulo

	@Override
	public VectorElement mod(long other) {
		for (ScalarElement c : components)
			c.mod(other);
		return this;
	}
	@Override
	public VectorElement mod(double other) {
		for (ScalarElement c : components)
			c.mod(other);
		return this;
	}
	@Override
	public VectorElement mod(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			mod((VectorElement) other);
		} else {
			mod((ScalarElement) other);
		}
		return this;
	}
	public VectorElement mod(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].mod(other);
		return this;
	}
	public VectorElement mod(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].mod(other.components[i]);
		return this;
	}

	@Override
	public VectorElement modIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			modIfValid(other, (VectorElement) mask);
		} else {
			modIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement modIfValid(long other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other, mask);
		return this;
	}
	public VectorElement modIfValid(long other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			modIfValid(other, (VectorElement) mask);
		} else {
			modIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement modIfValid(double other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other, mask);
		return this;
	}
	public VectorElement modIfValid(double other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			modIfValid((VectorElement) other);
		} else {
			modIfValid((ScalarElement) other);
		}
		return this;
	}
	public VectorElement modIfValid(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other);
		return this;
	}
	public VectorElement modIfValid(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other.components[i]);
		return this;
	}
	@Override
	public VectorElement modIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return modIfValid((VectorElement) other, (VectorElement) mask);
			else
				return modIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return modIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return modIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement modIfValid(ScalarElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other, mask);
		return this;
	}
	public VectorElement modIfValid(ScalarElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other, mask.components[i]);
		return this;
	}
	public VectorElement modIfValid(VectorElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other.components[i], mask);
		return this;
	}
	public VectorElement modIfValid(VectorElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modIfValid(other.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement modNew(long other) {
		VectorElement res = copy();
		return res.mod(other);
	}
	@Override
	public VectorElement modNew(double other) {
		VectorElement res = copy();
		return res.mod(other);
	}
	@Override
	public VectorElement modNew(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return modNew((VectorElement) other);
		} else {
			return modNew((ScalarElement) other);
		}
	}
	public VectorElement modNew(ScalarElement other) {
		VectorElement res = copy();
		return res.mod(other);
	}
	public VectorElement modNew(VectorElement other) {
		VectorElement res = copy();
		return res.mod(other);
	}

	@Override
	public VectorElement modNewIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return modNewIfValid(other, (VectorElement) mask);
		} else {
			return modNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement modNewIfValid(long other, ScalarElement mask) {
		VectorElement res = copy();
		return res.modIfValid(other, mask);
	}
	public VectorElement modNewIfValid(long other, VectorElement mask) {
		VectorElement res = copy();
		return res.modIfValid(other, mask);
	}
	@Override
	public VectorElement modNewIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return modNewIfValid(other, (VectorElement) mask);
		} else {
			return modNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement modNewIfValid(double other, ScalarElement mask) {
		VectorElement res = copy();
		return res.modIfValid(other, mask);
	}
	public VectorElement modNewIfValid(double other, VectorElement mask) {
		VectorElement res = copy();
		return res.modIfValid(other, mask);
	}
	@Override
	public VectorElement modNewIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return modNewIfValid((VectorElement) other);
		} else {
			return modNewIfValid((ScalarElement) other);
		}
	}
	public VectorElement modNewIfValid(ScalarElement other) {
		VectorElement res = copy();
		return res.modIfValid(other);
	}
	public VectorElement modNewIfValid(VectorElement other) {
		VectorElement res = copy();
		return res.modIfValid(other);
	}
	@Override
	public VectorElement modNewIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return modNewIfValid((VectorElement) other, (VectorElement) mask);
			else
				return modNewIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return modNewIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return modNewIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement modNewIfValid(ScalarElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.modIfValid(other, mask);
	}
	public VectorElement modNewIfValid(ScalarElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.modIfValid(other, mask);
	}
	public VectorElement modNewIfValid(VectorElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.modIfValid(other, mask);
	}
	public VectorElement modNewIfValid(VectorElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.modIfValid(other, mask);
	}

	@Override
	public VectorElement modOf(long a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].modOf(a, b);
		return this;
	}
	@Override
	public VectorElement modOf(double a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].modOf(a, b);
		return this;
	}
	@Override
	public VectorElement modOf(long a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].modOf(a, b);
		return this;
	}
	@Override
	public VectorElement modOf(double a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].modOf(a, b);
		return this;
	}
	@Override
	public VectorElement modOf(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].modOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].modOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement modOf(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].modOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].modOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement modOf(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].modOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].modOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement modOf(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].modOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].modOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement modOf(Element<?> a, Element<?> b) {
		if (a.getClass() == VectorElement.class && b.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].modOf(av.components[i], bv.components[i]);
		} else if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].modOf(av.components[i], b);
		} else if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].modOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].modOf(a, b);
		}
		return this;
	}

	@Override
	public VectorElement modOfIfValid(long a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return modOfIfValid(a, b, (VectorElement) mask);
		} else {
			return modOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement modOfIfValid(long a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(long a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(double a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return modOfIfValid(a, b, (VectorElement) mask);
		} else {
			return modOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement modOfIfValid(double a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(double a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(long a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return modOfIfValid(a, b, (VectorElement) mask);
		} else {
			return modOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement modOfIfValid(long a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(long a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(double a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return modOfIfValid(a, b, (VectorElement) mask);
		} else {
			return modOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement modOfIfValid(double a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(double a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			return modOfIfValid((VectorElement) a, b);
		} else {
			return modOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement modOfIfValid(ScalarElement a, long b) {
		if (!a.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(VectorElement a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return modOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return modOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return modOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return modOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	public VectorElement modOfIfValid(ScalarElement a, long b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOf(a, b);
		return this;
	}
	public VectorElement modOfIfValid(ScalarElement a, long b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return modOfIfValid(a, (VectorElement) b);
		} else {
			return modOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement modOfIfValid(long a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(long a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return modOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return modOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return modOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return modOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement modOfIfValid(long a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(long a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(long a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement modOfIfValid(long a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			return modOfIfValid((VectorElement) a, b);
		} else {
			return modOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement modOfIfValid(ScalarElement a, double b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(ScalarElement a, double b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b, mask);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return modOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return modOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return modOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return modOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	@Override
	public VectorElement modOfIfValid(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return modOfIfValid(a, (VectorElement) b);
		} else {
			return modOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement modOfIfValid(double a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(double a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return modOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return modOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return modOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return modOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement modOfIfValid(double a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(double a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(double a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement modOfIfValid(double a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(Element<?> a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			if (a.getClass() == VectorElement.class)
				return modOfIfValid((VectorElement) a, (VectorElement) b);
			else
				return modOfIfValid((ScalarElement) a, (VectorElement) b);
		} else {
			if (a.getClass() == VectorElement.class)
				return modOfIfValid((VectorElement) a, (ScalarElement) b);
			else
				return modOfIfValid((ScalarElement) a, (ScalarElement) b);
		}
	}
	public VectorElement modOfIfValid(ScalarElement a, ScalarElement b) {
		if (!a.isValid() || !b.isValid())
			return this;
		return modOf(a, b);
	}
	public VectorElement modOfIfValid(ScalarElement a, VectorElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, ScalarElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	@Override
	public VectorElement modOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return modOfIfValid((VectorElement) a, (VectorElement) b, (VectorElement) mask);
				else
					return modOfIfValid((ScalarElement) a, (VectorElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return modOfIfValid((VectorElement) a, (VectorElement) b, (ScalarElement) mask);
				else
					return modOfIfValid((ScalarElement) a, (VectorElement) b, (ScalarElement) mask);
			}
		} else {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return modOfIfValid((VectorElement) a, (ScalarElement) b, (VectorElement) mask);
				else
					return modOfIfValid((ScalarElement) a, (ScalarElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return modOfIfValid((VectorElement) a, (ScalarElement) b, (ScalarElement) mask);
				else
					return modOfIfValid((ScalarElement) a, (ScalarElement) b, (ScalarElement) mask);
			}
		}
	}
	public VectorElement modOfIfValid(ScalarElement a, ScalarElement b, ScalarElement mask) {
		if (!a.isValid() || !b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOf(a, b);
		return this;
	}
	public VectorElement modOfIfValid(ScalarElement a, ScalarElement b, VectorElement mask) {
		if (!a.isValid() || !b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(ScalarElement a, VectorElement b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(ScalarElement a, VectorElement b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	public VectorElement modOfIfValid(VectorElement a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].modOfIfValid(a.components[i], b.components[i], mask.components[i]);
		return this;
	}

	// BOUNDING

	// minimum

	@Override
	public VectorElement min(long other) {
		for (ScalarElement c : components)
			c.min(other);
		return this;
	}
	@Override
	public VectorElement min(double other) {
		for (ScalarElement c : components)
			c.min(other);
		return this;
	}
	@Override
	public VectorElement min(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			min((VectorElement) other);
		} else {
			min((ScalarElement) other);
		}
		return this;
	}
	public VectorElement min(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].min(other);
		return this;
	}
	public VectorElement min(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].min(other.components[i]);
		return this;
	}

	@Override
	public VectorElement minIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			minIfValid(other, (VectorElement) mask);
		} else {
			minIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement minIfValid(long other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other, mask);
		return this;
	}
	public VectorElement minIfValid(long other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			minIfValid(other, (VectorElement) mask);
		} else {
			minIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement minIfValid(double other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other, mask);
		return this;
	}
	public VectorElement minIfValid(double other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			minIfValid((VectorElement) other);
		} else {
			minIfValid((ScalarElement) other);
		}
		return this;
	}
	public VectorElement minIfValid(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other);
		return this;
	}
	public VectorElement minIfValid(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other.components[i]);
		return this;
	}
	@Override
	public VectorElement minIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return minIfValid((VectorElement) other, (VectorElement) mask);
			else
				return minIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return minIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return minIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement minIfValid(ScalarElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other, mask);
		return this;
	}
	public VectorElement minIfValid(ScalarElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other, mask.components[i]);
		return this;
	}
	public VectorElement minIfValid(VectorElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other.components[i], mask);
		return this;
	}
	public VectorElement minIfValid(VectorElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minIfValid(other.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement minNew(long other) {
		VectorElement res = copy();
		return res.min(other);
	}
	@Override
	public VectorElement minNew(double other) {
		VectorElement res = copy();
		return res.min(other);
	}
	@Override
	public VectorElement minNew(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return minNew((VectorElement) other);
		} else {
			return minNew((ScalarElement) other);
		}
	}
	public VectorElement minNew(ScalarElement other) {
		VectorElement res = copy();
		return res.min(other);
	}
	public VectorElement minNew(VectorElement other) {
		VectorElement res = copy();
		return res.min(other);
	}

	@Override
	public VectorElement minNewIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return minNewIfValid(other, (VectorElement) mask);
		} else {
			return minNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement minNewIfValid(long other, ScalarElement mask) {
		VectorElement res = copy();
		return res.minIfValid(other, mask);
	}
	public VectorElement minNewIfValid(long other, VectorElement mask) {
		VectorElement res = copy();
		return res.minIfValid(other, mask);
	}
	@Override
	public VectorElement minNewIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return minNewIfValid(other, (VectorElement) mask);
		} else {
			return minNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement minNewIfValid(double other, ScalarElement mask) {
		VectorElement res = copy();
		return res.minIfValid(other, mask);
	}
	public VectorElement minNewIfValid(double other, VectorElement mask) {
		VectorElement res = copy();
		return res.minIfValid(other, mask);
	}
	@Override
	public VectorElement minNewIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return minNewIfValid((VectorElement) other);
		} else {
			return minNewIfValid((ScalarElement) other);
		}
	}
	public VectorElement minNewIfValid(ScalarElement other) {
		VectorElement res = copy();
		return res.minIfValid(other);
	}
	public VectorElement minNewIfValid(VectorElement other) {
		VectorElement res = copy();
		return res.minIfValid(other);
	}
	@Override
	public VectorElement minNewIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return minNewIfValid((VectorElement) other, (VectorElement) mask);
			else
				return minNewIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return minNewIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return minNewIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement minNewIfValid(ScalarElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.minIfValid(other, mask);
	}
	public VectorElement minNewIfValid(ScalarElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.minIfValid(other, mask);
	}
	public VectorElement minNewIfValid(VectorElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.minIfValid(other, mask);
	}
	public VectorElement minNewIfValid(VectorElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.minIfValid(other, mask);
	}

	@Override
	public VectorElement minOf(long a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].minOf(a, b);
		return this;
	}
	@Override
	public VectorElement minOf(double a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].minOf(a, b);
		return this;
	}
	@Override
	public VectorElement minOf(long a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].minOf(a, b);
		return this;
	}
	@Override
	public VectorElement minOf(double a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].minOf(a, b);
		return this;
	}
	@Override
	public VectorElement minOf(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].minOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].minOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement minOf(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].minOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].minOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement minOf(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].minOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].minOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement minOf(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].minOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].minOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement minOf(Element<?> a, Element<?> b) {
		if (a.getClass() == VectorElement.class && b.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].minOf(av.components[i], bv.components[i]);
		} else if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].minOf(av.components[i], b);
		} else if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].minOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].minOf(a, b);
		}
		return this;
	}

	@Override
	public VectorElement minOfIfValid(long a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return minOfIfValid(a, b, (VectorElement) mask);
		} else {
			return minOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement minOfIfValid(long a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(long a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(double a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return minOfIfValid(a, b, (VectorElement) mask);
		} else {
			return minOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement minOfIfValid(double a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(double a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(long a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return minOfIfValid(a, b, (VectorElement) mask);
		} else {
			return minOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement minOfIfValid(long a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(long a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(double a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return minOfIfValid(a, b, (VectorElement) mask);
		} else {
			return minOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement minOfIfValid(double a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(double a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			return minOfIfValid((VectorElement) a, b);
		} else {
			return minOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement minOfIfValid(ScalarElement a, long b) {
		if (!a.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(VectorElement a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return minOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return minOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return minOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return minOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	public VectorElement minOfIfValid(ScalarElement a, long b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOf(a, b);
		return this;
	}
	public VectorElement minOfIfValid(ScalarElement a, long b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return minOfIfValid(a, (VectorElement) b);
		} else {
			return minOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement minOfIfValid(long a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(long a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return minOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return minOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return minOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return minOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement minOfIfValid(long a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(long a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(long a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement minOfIfValid(long a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			return minOfIfValid((VectorElement) a, b);
		} else {
			return minOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement minOfIfValid(ScalarElement a, double b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(ScalarElement a, double b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b, mask);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return minOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return minOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return minOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return minOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	@Override
	public VectorElement minOfIfValid(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return minOfIfValid(a, (VectorElement) b);
		} else {
			return minOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement minOfIfValid(double a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(double a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return minOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return minOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return minOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return minOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement minOfIfValid(double a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(double a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(double a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement minOfIfValid(double a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(Element<?> a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			if (a.getClass() == VectorElement.class)
				return minOfIfValid((VectorElement) a, (VectorElement) b);
			else
				return minOfIfValid((ScalarElement) a, (VectorElement) b);
		} else {
			if (a.getClass() == VectorElement.class)
				return minOfIfValid((VectorElement) a, (ScalarElement) b);
			else
				return minOfIfValid((ScalarElement) a, (ScalarElement) b);
		}
	}
	public VectorElement minOfIfValid(ScalarElement a, ScalarElement b) {
		if (!a.isValid() || !b.isValid())
			return this;
		return minOf(a, b);
	}
	public VectorElement minOfIfValid(ScalarElement a, VectorElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, ScalarElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	@Override
	public VectorElement minOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return minOfIfValid((VectorElement) a, (VectorElement) b, (VectorElement) mask);
				else
					return minOfIfValid((ScalarElement) a, (VectorElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return minOfIfValid((VectorElement) a, (VectorElement) b, (ScalarElement) mask);
				else
					return minOfIfValid((ScalarElement) a, (VectorElement) b, (ScalarElement) mask);
			}
		} else {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return minOfIfValid((VectorElement) a, (ScalarElement) b, (VectorElement) mask);
				else
					return minOfIfValid((ScalarElement) a, (ScalarElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return minOfIfValid((VectorElement) a, (ScalarElement) b, (ScalarElement) mask);
				else
					return minOfIfValid((ScalarElement) a, (ScalarElement) b, (ScalarElement) mask);
			}
		}
	}
	public VectorElement minOfIfValid(ScalarElement a, ScalarElement b, ScalarElement mask) {
		if (!a.isValid() || !b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOf(a, b);
		return this;
	}
	public VectorElement minOfIfValid(ScalarElement a, ScalarElement b, VectorElement mask) {
		if (!a.isValid() || !b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(ScalarElement a, VectorElement b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(ScalarElement a, VectorElement b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	public VectorElement minOfIfValid(VectorElement a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].minOfIfValid(a.components[i], b.components[i], mask.components[i]);
		return this;
	}

	// maximum

	@Override
	public VectorElement max(long other) {
		for (ScalarElement c : components)
			c.max(other);
		return this;
	}
	@Override
	public VectorElement max(double other) {
		for (ScalarElement c : components)
			c.max(other);
		return this;
	}
	@Override
	public VectorElement max(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			max((VectorElement) other);
		} else {
			max((ScalarElement) other);
		}
		return this;
	}
	public VectorElement max(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].max(other);
		return this;
	}
	public VectorElement max(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].max(other.components[i]);
		return this;
	}

	@Override
	public VectorElement maxIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			maxIfValid(other, (VectorElement) mask);
		} else {
			maxIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement maxIfValid(long other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other, mask);
		return this;
	}
	public VectorElement maxIfValid(long other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			maxIfValid(other, (VectorElement) mask);
		} else {
			maxIfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement maxIfValid(double other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other, mask);
		return this;
	}
	public VectorElement maxIfValid(double other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			maxIfValid((VectorElement) other);
		} else {
			maxIfValid((ScalarElement) other);
		}
		return this;
	}
	public VectorElement maxIfValid(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other);
		return this;
	}
	public VectorElement maxIfValid(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other.components[i]);
		return this;
	}
	@Override
	public VectorElement maxIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return maxIfValid((VectorElement) other, (VectorElement) mask);
			else
				return maxIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return maxIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return maxIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement maxIfValid(ScalarElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other, mask);
		return this;
	}
	public VectorElement maxIfValid(ScalarElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other, mask.components[i]);
		return this;
	}
	public VectorElement maxIfValid(VectorElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other.components[i], mask);
		return this;
	}
	public VectorElement maxIfValid(VectorElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxIfValid(other.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement maxNew(long other) {
		VectorElement res = copy();
		return res.max(other);
	}
	@Override
	public VectorElement maxNew(double other) {
		VectorElement res = copy();
		return res.max(other);
	}
	@Override
	public VectorElement maxNew(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return maxNew((VectorElement) other);
		} else {
			return maxNew((ScalarElement) other);
		}
	}
	public VectorElement maxNew(ScalarElement other) {
		VectorElement res = copy();
		return res.max(other);
	}
	public VectorElement maxNew(VectorElement other) {
		VectorElement res = copy();
		return res.max(other);
	}

	@Override
	public VectorElement maxNewIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return maxNewIfValid(other, (VectorElement) mask);
		} else {
			return maxNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement maxNewIfValid(long other, ScalarElement mask) {
		VectorElement res = copy();
		return res.maxIfValid(other, mask);
	}
	public VectorElement maxNewIfValid(long other, VectorElement mask) {
		VectorElement res = copy();
		return res.maxIfValid(other, mask);
	}
	@Override
	public VectorElement maxNewIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return maxNewIfValid(other, (VectorElement) mask);
		} else {
			return maxNewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement maxNewIfValid(double other, ScalarElement mask) {
		VectorElement res = copy();
		return res.maxIfValid(other, mask);
	}
	public VectorElement maxNewIfValid(double other, VectorElement mask) {
		VectorElement res = copy();
		return res.maxIfValid(other, mask);
	}
	@Override
	public VectorElement maxNewIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return maxNewIfValid((VectorElement) other);
		} else {
			return maxNewIfValid((ScalarElement) other);
		}
	}
	public VectorElement maxNewIfValid(ScalarElement other) {
		VectorElement res = copy();
		return res.maxIfValid(other);
	}
	public VectorElement maxNewIfValid(VectorElement other) {
		VectorElement res = copy();
		return res.maxIfValid(other);
	}
	@Override
	public VectorElement maxNewIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return maxNewIfValid((VectorElement) other, (VectorElement) mask);
			else
				return maxNewIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return maxNewIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return maxNewIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement maxNewIfValid(ScalarElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.maxIfValid(other, mask);
	}
	public VectorElement maxNewIfValid(ScalarElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.maxIfValid(other, mask);
	}
	public VectorElement maxNewIfValid(VectorElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.maxIfValid(other, mask);
	}
	public VectorElement maxNewIfValid(VectorElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.maxIfValid(other, mask);
	}

	@Override
	public VectorElement maxOf(long a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOf(a, b);
		return this;
	}
	@Override
	public VectorElement maxOf(double a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOf(a, b);
		return this;
	}
	@Override
	public VectorElement maxOf(long a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOf(a, b);
		return this;
	}
	@Override
	public VectorElement maxOf(double a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOf(a, b);
		return this;
	}
	@Override
	public VectorElement maxOf(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement maxOf(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement maxOf(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement maxOf(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(a, b);
		}
		return this;
	}
	@Override
	public VectorElement maxOf(Element<?> a, Element<?> b) {
		if (a.getClass() == VectorElement.class && b.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(av.components[i], bv.components[i]);
		} else if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(av.components[i], b);
		} else if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].maxOf(a, b);
		}
		return this;
	}

	@Override
	public VectorElement maxOfIfValid(long a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return maxOfIfValid(a, b, (VectorElement) mask);
		} else {
			return maxOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement maxOfIfValid(long a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(long a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(double a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return maxOfIfValid(a, b, (VectorElement) mask);
		} else {
			return maxOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement maxOfIfValid(double a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(double a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(long a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return maxOfIfValid(a, b, (VectorElement) mask);
		} else {
			return maxOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement maxOfIfValid(long a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(long a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(double a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return maxOfIfValid(a, b, (VectorElement) mask);
		} else {
			return maxOfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement maxOfIfValid(double a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(double a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			return maxOfIfValid((VectorElement) a, b);
		} else {
			return maxOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement maxOfIfValid(ScalarElement a, long b) {
		if (!a.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(VectorElement a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return maxOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return maxOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return maxOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return maxOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	public VectorElement maxOfIfValid(ScalarElement a, long b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOf(a, b);
		return this;
	}
	public VectorElement maxOfIfValid(ScalarElement a, long b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return maxOfIfValid(a, (VectorElement) b);
		} else {
			return maxOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement maxOfIfValid(long a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(long a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return maxOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return maxOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return maxOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return maxOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement maxOfIfValid(long a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(long a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(long a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement maxOfIfValid(long a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			return maxOfIfValid((VectorElement) a, b);
		} else {
			return maxOfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement maxOfIfValid(ScalarElement a, double b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(ScalarElement a, double b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b, mask);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return maxOfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return maxOfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return maxOfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return maxOfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	@Override
	public VectorElement maxOfIfValid(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return maxOfIfValid(a, (VectorElement) b);
		} else {
			return maxOfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement maxOfIfValid(double a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(double a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return maxOfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return maxOfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return maxOfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return maxOfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement maxOfIfValid(double a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(double a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(double a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement maxOfIfValid(double a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(Element<?> a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			if (a.getClass() == VectorElement.class)
				return maxOfIfValid((VectorElement) a, (VectorElement) b);
			else
				return maxOfIfValid((ScalarElement) a, (VectorElement) b);
		} else {
			if (a.getClass() == VectorElement.class)
				return maxOfIfValid((VectorElement) a, (ScalarElement) b);
			else
				return maxOfIfValid((ScalarElement) a, (ScalarElement) b);
		}
	}
	public VectorElement maxOfIfValid(ScalarElement a, ScalarElement b) {
		if (!a.isValid() || !b.isValid())
			return this;
		return maxOf(a, b);
	}
	public VectorElement maxOfIfValid(ScalarElement a, VectorElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, ScalarElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	@Override
	public VectorElement maxOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return maxOfIfValid((VectorElement) a, (VectorElement) b, (VectorElement) mask);
				else
					return maxOfIfValid((ScalarElement) a, (VectorElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return maxOfIfValid((VectorElement) a, (VectorElement) b, (ScalarElement) mask);
				else
					return maxOfIfValid((ScalarElement) a, (VectorElement) b, (ScalarElement) mask);
			}
		} else {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return maxOfIfValid((VectorElement) a, (ScalarElement) b, (VectorElement) mask);
				else
					return maxOfIfValid((ScalarElement) a, (ScalarElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return maxOfIfValid((VectorElement) a, (ScalarElement) b, (ScalarElement) mask);
				else
					return maxOfIfValid((ScalarElement) a, (ScalarElement) b, (ScalarElement) mask);
			}
		}
	}
	public VectorElement maxOfIfValid(ScalarElement a, ScalarElement b, ScalarElement mask) {
		if (!a.isValid() || !b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOf(a, b);
		return this;
	}
	public VectorElement maxOfIfValid(ScalarElement a, ScalarElement b, VectorElement mask) {
		if (!a.isValid() || !b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(ScalarElement a, VectorElement b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(ScalarElement a, VectorElement b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b.components[i]);
		return this;
	}
	public VectorElement maxOfIfValid(VectorElement a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].maxOfIfValid(a.components[i], b.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement clamp(long min, long max) {
		for (int i = 0; i < components.length; i++) {
			components[i].clamp(min, max);
		}
		return this;
	}
	@Override
	public VectorElement clamp(double min, double max) {
		for (int i = 0; i < components.length; i++) {
			components[i].clamp(min, max);
		}
		return this;
	}
	@Override
	public VectorElement clamp(Element<?> min, Element<?> max) {
		for (int i = 0; i < components.length; i++) {
			components[i].clamp(min, max);
		}
		return this;
	}
	public VectorElement clamp(VectorElement min, VectorElement max) {
		for (int i = 0; i < components.length; i++) {
			components[i].clamp(min.components[i], max.components[i]);
		}
		return this;
	}

	@Override
	public VectorElement clampNew(long min, long max) {
		VectorElement res = copy();
		return res.clamp(min, max);
	}
	@Override
	public VectorElement clampNew(double min, double max) {
		VectorElement res = copy();
		return res.clamp(min, max);
	}
	@Override
	public VectorElement clampNew(Element<?> min, Element<?> max) {
		VectorElement res = copy();
		return res.clamp(min, max);
	}
	public VectorElement clampNew(VectorElement min, VectorElement max) {
		VectorElement res = copy();
		return res.clamp(min, max);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VectorElement other = (VectorElement) obj;
		if (!Arrays.equals(components, other.components))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(components);
		return result;
	}

}
