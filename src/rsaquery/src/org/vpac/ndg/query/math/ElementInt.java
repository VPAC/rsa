
package org.vpac.ndg.query.math;

/**
 * A pixel that stores a int value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See ElementX_gen.py.
public class ElementInt implements ScalarElement {
	private int value;
	private boolean valid;

	/**
	 * Create a new ElementInt, initalised to zero.
	 */
	public ElementInt() {
		this.value = 0;
		this.valid = true;
	}
	/**
	 * Create a new ElementInt.
	 * @param value The initial value for the element.
	 */
	public ElementInt(int value) {
		this.value = value;
		this.valid = true;
	}
	@Override
	public ElementInt copy() {
		ElementInt res = new ElementInt(value);
		res.valid = valid;
		return res;
	}
	@Override
	public ScalarElement[] getComponents() {
		return new ScalarElement[] { this };
	}

	@Override
	public boolean isValid() {
		return valid;
	}
	@Override
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	// CASTING

	@Override
	public ElementInt set(Element<?> value) {
		this.value = ((ScalarElement)value).intValue();
		this.valid = value.isValid();
		return this;
	}
	@Override
	public ElementInt set(Number value) {
		this.value = value.intValue();
		this.valid = true;
		return this;
	}

	@Override
	public byte byteValue() {
		return (byte)value;
	}
	@Override
	public ElementInt set(byte value) {
		this.value = (int)value;
		this.valid = true;
		return this;
	}
	@Override
	public ElementByte asByte() {
		ElementByte res = new ElementByte(this.byteValue());
		res.setValid(valid);
		return res;
	}

	@Override
	public short shortValue() {
		return (short)value;
	}
	@Override
	public ElementInt set(short value) {
		this.value = (int)value;
		this.valid = true;
		return this;
	}
	@Override
	public ElementShort asShort() {
		ElementShort res = new ElementShort(this.shortValue());
		res.setValid(valid);
		return res;
	}

	@Override
	public int intValue() {
		return value;
	}
	@Override
	public ElementInt set(int value) {
		this.value = value;
		this.valid = true;
		return this;
	}
	@Override
	public ElementInt asInt() {
		ElementInt res = new ElementInt(this.intValue());
		res.setValid(valid);
		return res;
	}

	@Override
	public long longValue() {
		return (long)value;
	}
	@Override
	public ElementInt set(long value) {
		this.value = (int)value;
		this.valid = true;
		return this;
	}
	@Override
	public ElementLong asLong() {
		ElementLong res = new ElementLong(this.longValue());
		res.setValid(valid);
		return res;
	}

	@Override
	public float floatValue() {
		return (float)value;
	}
	@Override
	public ElementInt set(float value) {
		this.value = (int)value;
		this.valid = true;
		return this;
	}
	@Override
	public ElementFloat asFloat() {
		ElementFloat res = new ElementFloat(this.floatValue());
		res.setValid(valid);
		return res;
	}

	@Override
	public double doubleValue() {
		return (double)value;
	}
	@Override
	public ElementInt set(double value) {
		this.value = (int)value;
		this.valid = true;
		return this;
	}
	@Override
	public ElementDouble asDouble() {
		ElementDouble res = new ElementDouble(this.doubleValue());
		res.setValid(valid);
		return res;
	}

	// ARITHMETIC

	@Override
	public ElementInt add(long other) {
		try {
			value = (int)(value + (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt add(double other) {
		try {
			value = (int)(value + (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementInt add(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (int)(value + ((ScalarElement)other).intValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt addNew(long other) {
		ElementInt res = copy();
		return res.add(other);
	}
	@Override
	public ElementInt addNew(double other) {
		ElementInt res = copy();
		return res.add(other);
	}
	@Override
	public ElementInt addNew(Element<?> other) {
		ElementInt res = copy();
		return res.add(other);
	}

	@Override
	public ElementInt addOf(long a, long b) {
		try {
			value = (int)(a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt addOf(double a, long b) {
		try {
			value = (int)((int)a + (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt addOf(long a, double b) {
		try {
			value = (int)((int)a + (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt addOf(double a, double b) {
		try {
			value = (int)((int)a + (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt addOf(Element<?> a, long b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt addOf(long a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt addOf(Element<?> a, double b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt addOf(double a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementInt addOf(Element<?> a, Element<?> b) {
		try {
			int av = ((ScalarElement)a).intValue();
			int bv = ((ScalarElement)b).intValue();
			value = (int)(av + bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt sub(long other) {
		try {
			value = (int)(value - (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt sub(double other) {
		try {
			value = (int)(value - (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementInt sub(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (int)(value - ((ScalarElement)other).intValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt subNew(long other) {
		ElementInt res = copy();
		return res.sub(other);
	}
	@Override
	public ElementInt subNew(double other) {
		ElementInt res = copy();
		return res.sub(other);
	}
	@Override
	public ElementInt subNew(Element<?> other) {
		ElementInt res = copy();
		return res.sub(other);
	}

	@Override
	public ElementInt subOf(long a, long b) {
		try {
			value = (int)(a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt subOf(double a, long b) {
		try {
			value = (int)((int)a - (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt subOf(long a, double b) {
		try {
			value = (int)((int)a - (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt subOf(double a, double b) {
		try {
			value = (int)((int)a - (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt subOf(Element<?> a, long b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt subOf(long a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt subOf(Element<?> a, double b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt subOf(double a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementInt subOf(Element<?> a, Element<?> b) {
		try {
			int av = ((ScalarElement)a).intValue();
			int bv = ((ScalarElement)b).intValue();
			value = (int)(av - bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt mul(long other) {
		try {
			value = (int)(value * (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt mul(double other) {
		try {
			value = (int)(value * (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementInt mul(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (int)(value * ((ScalarElement)other).intValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt mulNew(long other) {
		ElementInt res = copy();
		return res.mul(other);
	}
	@Override
	public ElementInt mulNew(double other) {
		ElementInt res = copy();
		return res.mul(other);
	}
	@Override
	public ElementInt mulNew(Element<?> other) {
		ElementInt res = copy();
		return res.mul(other);
	}

	@Override
	public ElementInt mulOf(long a, long b) {
		try {
			value = (int)(a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt mulOf(double a, long b) {
		try {
			value = (int)((int)a * (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt mulOf(long a, double b) {
		try {
			value = (int)((int)a * (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt mulOf(double a, double b) {
		try {
			value = (int)((int)a * (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt mulOf(Element<?> a, long b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt mulOf(long a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt mulOf(Element<?> a, double b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt mulOf(double a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementInt mulOf(Element<?> a, Element<?> b) {
		try {
			int av = ((ScalarElement)a).intValue();
			int bv = ((ScalarElement)b).intValue();
			value = (int)(av * bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt div(long other) {
		try {
			value = (int)(value / (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt div(double other) {
		try {
			value = (int)(value / (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementInt div(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (int)(value / ((ScalarElement)other).intValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt divNew(long other) {
		ElementInt res = copy();
		return res.div(other);
	}
	@Override
	public ElementInt divNew(double other) {
		ElementInt res = copy();
		return res.div(other);
	}
	@Override
	public ElementInt divNew(Element<?> other) {
		ElementInt res = copy();
		return res.div(other);
	}

	@Override
	public ElementInt divOf(long a, long b) {
		try {
			value = (int)(a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt divOf(double a, long b) {
		try {
			value = (int)((int)a / (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt divOf(long a, double b) {
		try {
			value = (int)((int)a / (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt divOf(double a, double b) {
		try {
			value = (int)((int)a / (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt divOf(Element<?> a, long b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt divOf(long a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt divOf(Element<?> a, double b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt divOf(double a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementInt divOf(Element<?> a, Element<?> b) {
		try {
			int av = ((ScalarElement)a).intValue();
			int bv = ((ScalarElement)b).intValue();
			value = (int)(av / bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt mod(long other) {
		try {
			value = (int)(value % (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt mod(double other) {
		try {
			value = (int)(value % (int)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementInt mod(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (int)(value % ((ScalarElement)other).intValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementInt modNew(long other) {
		ElementInt res = copy();
		return res.mod(other);
	}
	@Override
	public ElementInt modNew(double other) {
		ElementInt res = copy();
		return res.mod(other);
	}
	@Override
	public ElementInt modNew(Element<?> other) {
		ElementInt res = copy();
		return res.mod(other);
	}

	@Override
	public ElementInt modOf(long a, long b) {
		try {
			value = (int)(a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt modOf(double a, long b) {
		try {
			value = (int)((int)a % (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt modOf(long a, double b) {
		try {
			value = (int)((int)a % (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementInt modOf(double a, double b) {
		try {
			value = (int)((int)a % (int)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt modOf(Element<?> a, long b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt modOf(long a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt modOf(Element<?> a, double b) {
		try {
			int av = ((ScalarElement)a).intValue();
			value = (int)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt modOf(double a, Element<?> b) {
		try {
			int bv = ((ScalarElement)b).intValue();
			value = (int)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementInt modOf(Element<?> a, Element<?> b) {
		try {
			int av = ((ScalarElement)a).intValue();
			int bv = ((ScalarElement)b).intValue();
			value = (int)(av % bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	// BOUNDING

	@Override
	public ElementInt min(long other) {
		if (other < value)
			value = (int)other;
		return this;
	}
	@Override
	public ElementInt min(double other) {
		if (other < value)
			value = (int)other;
		return this;
	}
	@Override
	public ElementInt min(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).intValue() < value)
			value = ((ScalarElement)other).intValue();
		return this;
	}

	@Override
	public ElementInt minNew(long other) {
		ElementInt res = copy();
		return res.min(other);
	}
	@Override
	public ElementInt minNew(double other) {
		ElementInt res = copy();
		return res.min(other);
	}
	@Override
	public ElementInt minNew(Element<?> other) {
		ElementInt res = copy();
		return res.min(other);
	}

	@Override
	public ElementInt minOf(long a, long b) {
		if (a < b)
			value = (int)a;
		else
			value = (int)b;
		valid = true;
		return this;
	}
	@Override
	public ElementInt minOf(double a, long b) {
		if (a < b)
			value = (int)a;
		else
			value = (int)b;
		valid = true;
		return this;
	}
	@Override
	public ElementInt minOf(long a, double b) {
		if (a < b)
			value = (int)a;
		else
			value = (int)b;
		valid = true;
		return this;
	}
	@Override
	public ElementInt minOf(double a, double b) {
		if (a < b)
			value = (int)a;
		else
			value = (int)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt minOf(Element<?> a, long b) {
		int av = ((ScalarElement)a).intValue();
		if (av < b)
			value = (int)av;
		else
			value = (int)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt minOf(long a, Element<?> b) {
		int bv = ((ScalarElement)b).intValue();
		if (a < bv)
			value = (int)a;
		else
			value = (int)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt minOf(Element<?> a, double b) {
		int av = ((ScalarElement)a).intValue();
		if (av < b)
			value = (int)av;
		else
			value = (int)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt minOf(double a, Element<?> b) {
		int bv = ((ScalarElement)b).intValue();
		if (a < bv)
			value = (int)a;
		else
			value = (int)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementInt minOf(Element<?> a, Element<?> b) {
		int av = ((ScalarElement)a).intValue();
		int bv = ((ScalarElement)b).intValue();
		if (av < bv)
			value = (int)av;
		else
			value = (int)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementInt max(long other) {
		if (other > value)
			value = (int)other;
		return this;
	}
	@Override
	public ElementInt max(double other) {
		if (other > value)
			value = (int)other;
		return this;
	}
	@Override
	public ElementInt max(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).intValue() > value)
			value = ((ScalarElement)other).intValue();
		return this;
	}

	@Override
	public ElementInt maxNew(long other) {
		ElementInt res = copy();
		return res.max(other);
	}
	@Override
	public ElementInt maxNew(double other) {
		ElementInt res = copy();
		return res.max(other);
	}
	@Override
	public ElementInt maxNew(Element<?> other) {
		ElementInt res = copy();
		return res.max(other);
	}

	@Override
	public ElementInt maxOf(long a, long b) {
		if (a > b)
			value = (int)a;
		else
			value = (int)b;
		valid = true;
		return this;
	}
	@Override
	public ElementInt maxOf(double a, long b) {
		if (a > b)
			value = (int)a;
		else
			value = (int)b;
		valid = true;
		return this;
	}
	@Override
	public ElementInt maxOf(long a, double b) {
		if (a > b)
			value = (int)a;
		else
			value = (int)b;
		valid = true;
		return this;
	}
	@Override
	public ElementInt maxOf(double a, double b) {
		if (a > b)
			value = (int)a;
		else
			value = (int)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt maxOf(Element<?> a, long b) {
		int av = ((ScalarElement)a).intValue();
		if (av > b)
			value = (int)av;
		else
			value = (int)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt maxOf(long a, Element<?> b) {
		int bv = ((ScalarElement)b).intValue();
		if (a > bv)
			value = (int)a;
		else
			value = (int)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementInt maxOf(Element<?> a, double b) {
		int av = ((ScalarElement)a).intValue();
		if (av > b)
			value = (int)av;
		else
			value = (int)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementInt maxOf(double a, Element<?> b) {
		int bv = ((ScalarElement)b).intValue();
		if (a > bv)
			value = (int)a;
		else
			value = (int)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to int before the operation.
	 * @param b This will be converted to int before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementInt maxOf(Element<?> a, Element<?> b) {
		int av = ((ScalarElement)a).intValue();
		int bv = ((ScalarElement)b).intValue();
		if (av > bv)
			value = (int)av;
		else
			value = (int)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementInt clamp(long min, long max) {
		if (value < min)
			value = (int)min;
		else if (value > max)
			value = (int)max;
		return this;
	}
	@Override
	public ElementInt clamp(double min, double max) {
		if (value < min)
			value = (int)min;
		else if (value > max)
			value = (int)max;
		return this;
	}
	@Override
	public ElementInt clamp(Element<?> min, Element<?> max) {
		if (!min.isValid() || !max.isValid()) {
			this.setValid(false);
			return this;
		}
		// This may throw an exception - but that's OK, because VectorElements
		// are not comparable.
		ScalarElement minS = (ScalarElement)min;
		ScalarElement maxS = (ScalarElement)max;
		if (value < minS.intValue())
			value = minS.intValue();
		else if (value > maxS.intValue())
			value = maxS.intValue();
		return this;
	}

	@Override
	public ElementInt clampNew(long min, long max) {
		ElementInt res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementInt clampNew(double min, double max) {
		ElementInt res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementInt clampNew(Element<?> min, Element<?> max) {
		ElementInt res = copy();
		return res.clamp(min, max);
	}

	@Override
	public int compareTo(long other) {
		if (value > other)
			return 1;
		else if (other == value)
			return 0;
		else
			return -1;
	}
	@Override
	public int compareTo(double other) {
		if (value > other)
			return 1;
		else if (other == value)
			return 0;
		else
			return -1;
	}
	@Override
	public int compareTo(ScalarElement other) {
		if (ElementByte.class == other.getClass()) {
			if (value > other.byteValue())
				return 1;
			else if (value == other.byteValue())
				return 0;
			else
				return -1;

		} else if (ElementShort.class == other.getClass()) {
			if (value > other.shortValue())
				return 1;
			else if (value == other.shortValue())
				return 0;
			else
				return -1;

		} else if (ElementInt.class == other.getClass()) {
			if (value > other.intValue())
				return 1;
			else if (value == other.intValue())
				return 0;
			else
				return -1;

		} else if (ElementLong.class == other.getClass()) {
			if (value > other.longValue())
				return 1;
			else if (value == other.longValue())
				return 0;
			else
				return -1;

		} else if (ElementFloat.class == other.getClass()) {
			if (value > other.floatValue())
				return 1;
			else if (value == other.floatValue())
				return 0;
			else
				return -1;

		} else if (ElementDouble.class == other.getClass()) {
			if (value > other.doubleValue())
				return 1;
			else if (value == other.doubleValue())
				return 0;
			else
				return -1;

		} else {
			throw new UnsupportedOperationException(String.format(
					"No known comparison for type %s", other.getClass()));
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!ScalarElement.class.isAssignableFrom(obj.getClass()))
			return false;

		ScalarElement other = (ScalarElement) obj;

		if (ElementByte.class == obj.getClass()) {
			return value == other.byteValue();
		} else if (ElementShort.class == obj.getClass()) {
			return value == other.shortValue();
		} else if (ElementInt.class == obj.getClass()) {
			return value == other.intValue();
		} else if (ElementLong.class == obj.getClass()) {
			return value == other.longValue();
		} else if (ElementFloat.class == obj.getClass()) {
			return value == other.floatValue();
		} else if (ElementDouble.class == obj.getClass()) {
			return value == other.doubleValue();
		} else {
			throw new UnsupportedOperationException(String.format(
					"No known comparison for type %s", other.getClass()));
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + value;
	}


	@Override
	public String toString() {
		return String.format("%d", value);
	}
}
