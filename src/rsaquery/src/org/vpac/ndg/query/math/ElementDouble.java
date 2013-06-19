
package org.vpac.ndg.query.math;

/**
 * A pixel that stores a double value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See ElementX_gen.py.
public class ElementDouble implements ScalarElement {
	private double value;
	private boolean valid;

	/**
	 * Create a new ElementDouble, initalised to zero.
	 */
	public ElementDouble() {
		this.value = 0;
		this.valid = true;
	}
	/**
	 * Create a new ElementDouble.
	 * @param value The initial value for the element.
	 */
	public ElementDouble(double value) {
		this.value = value;
		this.valid = true;
	}
	@Override
	public ElementDouble copy() {
		ElementDouble res = new ElementDouble(value);
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
	public Double getValue() {
		return value;
	}

	// CASTING

	@Override
	public ElementDouble set(Element<?> value) {
		this.value = ((ScalarElement)value).doubleValue();
		this.valid = value.isValid();
		return this;
	}
	@Override
	public ElementDouble set(Number value) {
		this.value = value.doubleValue();
		this.valid = true;
		return this;
	}

	@Override
	public byte byteValue() {
		return (byte)value;
	}
	@Override
	public ElementDouble set(byte value) {
		this.value = (double)value;
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
	public ElementDouble set(short value) {
		this.value = (double)value;
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
		return (int)value;
	}
	@Override
	public ElementDouble set(int value) {
		this.value = (double)value;
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
	public ElementDouble set(long value) {
		this.value = (double)value;
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
	public ElementDouble set(float value) {
		this.value = (double)value;
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
		return value;
	}
	@Override
	public ElementDouble set(double value) {
		this.value = value;
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
	public ElementDouble add(long other) {
		try {
			value = (double)(value + (double)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble add(double other) {
		try {
			value = (double)(value + other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementDouble add(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (double)(value + ((ScalarElement)other).doubleValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble addNew(long other) {
		ElementDouble res = copy();
		return res.add(other);
	}
	@Override
	public ElementDouble addNew(double other) {
		ElementDouble res = copy();
		return res.add(other);
	}
	@Override
	public ElementDouble addNew(Element<?> other) {
		ElementDouble res = copy();
		return res.add(other);
	}

	@Override
	public ElementDouble addOf(long a, long b) {
		try {
			value = (double)(a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble addOf(double a, long b) {
		try {
			value = (double)(a + (double)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble addOf(long a, double b) {
		try {
			value = (double)((double)a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble addOf(double a, double b) {
		try {
			value = (double)((double)a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble addOf(Element<?> a, long b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble addOf(long a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble addOf(Element<?> a, double b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble addOf(double a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementDouble addOf(Element<?> a, Element<?> b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(av + bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble sub(long other) {
		try {
			value = (double)(value - (double)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble sub(double other) {
		try {
			value = (double)(value - other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementDouble sub(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (double)(value - ((ScalarElement)other).doubleValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble subNew(long other) {
		ElementDouble res = copy();
		return res.sub(other);
	}
	@Override
	public ElementDouble subNew(double other) {
		ElementDouble res = copy();
		return res.sub(other);
	}
	@Override
	public ElementDouble subNew(Element<?> other) {
		ElementDouble res = copy();
		return res.sub(other);
	}

	@Override
	public ElementDouble subOf(long a, long b) {
		try {
			value = (double)(a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble subOf(double a, long b) {
		try {
			value = (double)(a - (double)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble subOf(long a, double b) {
		try {
			value = (double)((double)a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble subOf(double a, double b) {
		try {
			value = (double)((double)a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble subOf(Element<?> a, long b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble subOf(long a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble subOf(Element<?> a, double b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble subOf(double a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementDouble subOf(Element<?> a, Element<?> b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(av - bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble mul(long other) {
		try {
			value = (double)(value * (double)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble mul(double other) {
		try {
			value = (double)(value * other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementDouble mul(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (double)(value * ((ScalarElement)other).doubleValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble mulNew(long other) {
		ElementDouble res = copy();
		return res.mul(other);
	}
	@Override
	public ElementDouble mulNew(double other) {
		ElementDouble res = copy();
		return res.mul(other);
	}
	@Override
	public ElementDouble mulNew(Element<?> other) {
		ElementDouble res = copy();
		return res.mul(other);
	}

	@Override
	public ElementDouble mulOf(long a, long b) {
		try {
			value = (double)(a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble mulOf(double a, long b) {
		try {
			value = (double)(a * (double)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble mulOf(long a, double b) {
		try {
			value = (double)((double)a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble mulOf(double a, double b) {
		try {
			value = (double)((double)a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble mulOf(Element<?> a, long b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble mulOf(long a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble mulOf(Element<?> a, double b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble mulOf(double a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementDouble mulOf(Element<?> a, Element<?> b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(av * bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble div(long other) {
		try {
			value = (double)(value / (double)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble div(double other) {
		try {
			value = (double)(value / other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementDouble div(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (double)(value / ((ScalarElement)other).doubleValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble divNew(long other) {
		ElementDouble res = copy();
		return res.div(other);
	}
	@Override
	public ElementDouble divNew(double other) {
		ElementDouble res = copy();
		return res.div(other);
	}
	@Override
	public ElementDouble divNew(Element<?> other) {
		ElementDouble res = copy();
		return res.div(other);
	}

	@Override
	public ElementDouble divOf(long a, long b) {
		try {
			value = (double)(a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble divOf(double a, long b) {
		try {
			value = (double)(a / (double)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble divOf(long a, double b) {
		try {
			value = (double)((double)a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble divOf(double a, double b) {
		try {
			value = (double)((double)a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble divOf(Element<?> a, long b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble divOf(long a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble divOf(Element<?> a, double b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble divOf(double a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementDouble divOf(Element<?> a, Element<?> b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(av / bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble mod(long other) {
		try {
			value = (double)(value % (double)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble mod(double other) {
		try {
			value = (double)(value % other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementDouble mod(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (double)(value % ((ScalarElement)other).doubleValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementDouble modNew(long other) {
		ElementDouble res = copy();
		return res.mod(other);
	}
	@Override
	public ElementDouble modNew(double other) {
		ElementDouble res = copy();
		return res.mod(other);
	}
	@Override
	public ElementDouble modNew(Element<?> other) {
		ElementDouble res = copy();
		return res.mod(other);
	}

	@Override
	public ElementDouble modOf(long a, long b) {
		try {
			value = (double)(a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble modOf(double a, long b) {
		try {
			value = (double)(a % (double)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble modOf(long a, double b) {
		try {
			value = (double)((double)a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementDouble modOf(double a, double b) {
		try {
			value = (double)((double)a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble modOf(Element<?> a, long b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble modOf(long a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble modOf(Element<?> a, double b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			value = (double)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble modOf(double a, Element<?> b) {
		try {
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementDouble modOf(Element<?> a, Element<?> b) {
		try {
			double av = ((ScalarElement)a).doubleValue();
			double bv = ((ScalarElement)b).doubleValue();
			value = (double)(av % bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	// BOUNDING

	@Override
	public ElementDouble min(long other) {
		if (other < value)
			value = (double)other;
		return this;
	}
	@Override
	public ElementDouble min(double other) {
		if (other < value)
			value = other;
		return this;
	}
	@Override
	public ElementDouble min(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).doubleValue() < value)
			value = ((ScalarElement)other).doubleValue();
		return this;
	}

	@Override
	public ElementDouble minNew(long other) {
		ElementDouble res = copy();
		return res.min(other);
	}
	@Override
	public ElementDouble minNew(double other) {
		ElementDouble res = copy();
		return res.min(other);
	}
	@Override
	public ElementDouble minNew(Element<?> other) {
		ElementDouble res = copy();
		return res.min(other);
	}

	@Override
	public ElementDouble minOf(long a, long b) {
		if (a < b)
			value = (double)a;
		else
			value = (double)b;
		valid = true;
		return this;
	}
	@Override
	public ElementDouble minOf(double a, long b) {
		if (a < b)
			value = (double)a;
		else
			value = (double)b;
		valid = true;
		return this;
	}
	@Override
	public ElementDouble minOf(long a, double b) {
		if (a < b)
			value = (double)a;
		else
			value = (double)b;
		valid = true;
		return this;
	}
	@Override
	public ElementDouble minOf(double a, double b) {
		if (a < b)
			value = (double)a;
		else
			value = (double)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble minOf(Element<?> a, long b) {
		double av = ((ScalarElement)a).doubleValue();
		if (av < b)
			value = (double)av;
		else
			value = (double)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble minOf(long a, Element<?> b) {
		double bv = ((ScalarElement)b).doubleValue();
		if (a < bv)
			value = (double)a;
		else
			value = (double)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble minOf(Element<?> a, double b) {
		double av = ((ScalarElement)a).doubleValue();
		if (av < b)
			value = (double)av;
		else
			value = (double)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble minOf(double a, Element<?> b) {
		double bv = ((ScalarElement)b).doubleValue();
		if (a < bv)
			value = (double)a;
		else
			value = (double)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementDouble minOf(Element<?> a, Element<?> b) {
		double av = ((ScalarElement)a).doubleValue();
		double bv = ((ScalarElement)b).doubleValue();
		if (av < bv)
			value = (double)av;
		else
			value = (double)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementDouble max(long other) {
		if (other > value)
			value = (double)other;
		return this;
	}
	@Override
	public ElementDouble max(double other) {
		if (other > value)
			value = other;
		return this;
	}
	@Override
	public ElementDouble max(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).doubleValue() > value)
			value = ((ScalarElement)other).doubleValue();
		return this;
	}

	@Override
	public ElementDouble maxNew(long other) {
		ElementDouble res = copy();
		return res.max(other);
	}
	@Override
	public ElementDouble maxNew(double other) {
		ElementDouble res = copy();
		return res.max(other);
	}
	@Override
	public ElementDouble maxNew(Element<?> other) {
		ElementDouble res = copy();
		return res.max(other);
	}

	@Override
	public ElementDouble maxOf(long a, long b) {
		if (a > b)
			value = (double)a;
		else
			value = (double)b;
		valid = true;
		return this;
	}
	@Override
	public ElementDouble maxOf(double a, long b) {
		if (a > b)
			value = (double)a;
		else
			value = (double)b;
		valid = true;
		return this;
	}
	@Override
	public ElementDouble maxOf(long a, double b) {
		if (a > b)
			value = (double)a;
		else
			value = (double)b;
		valid = true;
		return this;
	}
	@Override
	public ElementDouble maxOf(double a, double b) {
		if (a > b)
			value = (double)a;
		else
			value = (double)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble maxOf(Element<?> a, long b) {
		double av = ((ScalarElement)a).doubleValue();
		if (av > b)
			value = (double)av;
		else
			value = (double)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble maxOf(long a, Element<?> b) {
		double bv = ((ScalarElement)b).doubleValue();
		if (a > bv)
			value = (double)a;
		else
			value = (double)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementDouble maxOf(Element<?> a, double b) {
		double av = ((ScalarElement)a).doubleValue();
		if (av > b)
			value = (double)av;
		else
			value = (double)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementDouble maxOf(double a, Element<?> b) {
		double bv = ((ScalarElement)b).doubleValue();
		if (a > bv)
			value = (double)a;
		else
			value = (double)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to double before the operation.
	 * @param b This will be converted to double before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementDouble maxOf(Element<?> a, Element<?> b) {
		double av = ((ScalarElement)a).doubleValue();
		double bv = ((ScalarElement)b).doubleValue();
		if (av > bv)
			value = (double)av;
		else
			value = (double)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementDouble clamp(long min, long max) {
		if (value < min)
			value = (double)min;
		else if (value > max)
			value = (double)max;
		return this;
	}
	@Override
	public ElementDouble clamp(double min, double max) {
		if (value < min)
			value = min;
		else if (value > max)
			value = max;
		return this;
	}
	@Override
	public ElementDouble clamp(Element<?> min, Element<?> max) {
		if (!min.isValid() || !max.isValid()) {
			this.setValid(false);
			return this;
		}
		// This may throw an exception - but that's OK, because VectorElements
		// are not comparable.
		ScalarElement minS = (ScalarElement)min;
		ScalarElement maxS = (ScalarElement)max;
		if (value < minS.doubleValue())
			value = minS.doubleValue();
		else if (value > maxS.doubleValue())
			value = maxS.doubleValue();
		return this;
	}

	@Override
	public ElementDouble clampNew(long min, long max) {
		ElementDouble res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementDouble clampNew(double min, double max) {
		ElementDouble res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementDouble clampNew(Element<?> min, Element<?> max) {
		ElementDouble res = copy();
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
		long temp;
		temp = Double.doubleToLongBits(value);
		return prime + (int) (temp ^ (temp >>> 32));
	}


	@Override
	public String toString() {
		return String.format("%g", value);
	}
}
