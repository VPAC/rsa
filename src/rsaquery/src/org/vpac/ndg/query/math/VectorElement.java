
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
		return this;
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
		return this;
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
		return this;
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
		return this;
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
		return this;
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
		return this;
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
			addNew((VectorElement) other);
		} else {
			addNew((ScalarElement) other);
		}
		return this;
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
			subNew((VectorElement) other);
		} else {
			subNew((ScalarElement) other);
		}
		return this;
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
			mulNew((VectorElement) other);
		} else {
			mulNew((ScalarElement) other);
		}
		return this;
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
			divNew((VectorElement) other);
		} else {
			divNew((ScalarElement) other);
		}
		return this;
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
			modNew((VectorElement) other);
		} else {
			modNew((ScalarElement) other);
		}
		return this;
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

	// BOUNDING

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
			minNew((VectorElement) other);
		} else {
			minNew((ScalarElement) other);
		}
		return this;
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
			maxNew((VectorElement) other);
		} else {
			maxNew((ScalarElement) other);
		}
		return this;
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
