
package org.vpac.ndg.query.math;

/**
 * A pixel that stores a short value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See ElementX_gen.py.
public class ElementShort implements ScalarElement {
	private short value;
	private boolean valid;

	/**
	 * Create a new ElementShort, initalised to zero.
	 */
	public ElementShort() {
		this.value = 0;
		this.valid = true;
	}
	/**
	 * Create a new ElementShort.
	 * @param value The initial value for the element.
	 */
	public ElementShort(short value) {
		this.value = value;
		this.valid = true;
	}
	@Override
	public ElementShort copy() {
		ElementShort res = new ElementShort(value);
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
	public Short getValue() {
		return value;
	}

	// CASTING

	@Override
	public ElementShort set(Element<?> value) {
		this.value = ((ScalarElement)value).shortValue();
		this.valid = value.isValid();
		return this;
	}
	@Override
	public ElementShort set(Number value) {
		this.value = value.shortValue();
		this.valid = true;
		return this;
	}

	@Override
	public byte byteValue() {
		return (byte)value;
	}
	@Override
	public ElementShort set(byte value) {
		this.value = (short)value;
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
		return value;
	}
	@Override
	public ElementShort set(short value) {
		this.value = value;
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
	public ElementShort set(int value) {
		this.value = (short)value;
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
	public ElementShort set(long value) {
		this.value = (short)value;
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
	public ElementShort set(float value) {
		this.value = (short)value;
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
	public ElementShort set(double value) {
		this.value = (short)value;
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
	public ElementShort add(long other) {
		try {
			value = (short)(value + (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort add(double other) {
		try {
			value = (short)(value + (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementShort add(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (short)(value + ((ScalarElement)other).shortValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort addNew(long other) {
		ElementShort res = copy();
		return res.add(other);
	}
	@Override
	public ElementShort addNew(double other) {
		ElementShort res = copy();
		return res.add(other);
	}
	@Override
	public ElementShort addNew(Element<?> other) {
		ElementShort res = copy();
		return res.add(other);
	}

	@Override
	public ElementShort addOf(long a, long b) {
		try {
			value = (short)(a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort addOf(double a, long b) {
		try {
			value = (short)((short)a + (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort addOf(long a, double b) {
		try {
			value = (short)((short)a + (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort addOf(double a, double b) {
		try {
			value = (short)((short)a + (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort addOf(Element<?> a, long b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort addOf(long a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort addOf(Element<?> a, double b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort addOf(double a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementShort addOf(Element<?> a, Element<?> b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(av + bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort sub(long other) {
		try {
			value = (short)(value - (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort sub(double other) {
		try {
			value = (short)(value - (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementShort sub(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (short)(value - ((ScalarElement)other).shortValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort subNew(long other) {
		ElementShort res = copy();
		return res.sub(other);
	}
	@Override
	public ElementShort subNew(double other) {
		ElementShort res = copy();
		return res.sub(other);
	}
	@Override
	public ElementShort subNew(Element<?> other) {
		ElementShort res = copy();
		return res.sub(other);
	}

	@Override
	public ElementShort subOf(long a, long b) {
		try {
			value = (short)(a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort subOf(double a, long b) {
		try {
			value = (short)((short)a - (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort subOf(long a, double b) {
		try {
			value = (short)((short)a - (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort subOf(double a, double b) {
		try {
			value = (short)((short)a - (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort subOf(Element<?> a, long b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort subOf(long a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort subOf(Element<?> a, double b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort subOf(double a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementShort subOf(Element<?> a, Element<?> b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(av - bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort mul(long other) {
		try {
			value = (short)(value * (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort mul(double other) {
		try {
			value = (short)(value * (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementShort mul(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (short)(value * ((ScalarElement)other).shortValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort mulNew(long other) {
		ElementShort res = copy();
		return res.mul(other);
	}
	@Override
	public ElementShort mulNew(double other) {
		ElementShort res = copy();
		return res.mul(other);
	}
	@Override
	public ElementShort mulNew(Element<?> other) {
		ElementShort res = copy();
		return res.mul(other);
	}

	@Override
	public ElementShort mulOf(long a, long b) {
		try {
			value = (short)(a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort mulOf(double a, long b) {
		try {
			value = (short)((short)a * (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort mulOf(long a, double b) {
		try {
			value = (short)((short)a * (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort mulOf(double a, double b) {
		try {
			value = (short)((short)a * (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort mulOf(Element<?> a, long b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort mulOf(long a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort mulOf(Element<?> a, double b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort mulOf(double a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementShort mulOf(Element<?> a, Element<?> b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(av * bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort div(long other) {
		try {
			value = (short)(value / (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort div(double other) {
		try {
			value = (short)(value / (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementShort div(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (short)(value / ((ScalarElement)other).shortValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort divNew(long other) {
		ElementShort res = copy();
		return res.div(other);
	}
	@Override
	public ElementShort divNew(double other) {
		ElementShort res = copy();
		return res.div(other);
	}
	@Override
	public ElementShort divNew(Element<?> other) {
		ElementShort res = copy();
		return res.div(other);
	}

	@Override
	public ElementShort divOf(long a, long b) {
		try {
			value = (short)(a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort divOf(double a, long b) {
		try {
			value = (short)((short)a / (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort divOf(long a, double b) {
		try {
			value = (short)((short)a / (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort divOf(double a, double b) {
		try {
			value = (short)((short)a / (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort divOf(Element<?> a, long b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort divOf(long a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort divOf(Element<?> a, double b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort divOf(double a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementShort divOf(Element<?> a, Element<?> b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(av / bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort mod(long other) {
		try {
			value = (short)(value % (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort mod(double other) {
		try {
			value = (short)(value % (short)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementShort mod(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (short)(value % ((ScalarElement)other).shortValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementShort modNew(long other) {
		ElementShort res = copy();
		return res.mod(other);
	}
	@Override
	public ElementShort modNew(double other) {
		ElementShort res = copy();
		return res.mod(other);
	}
	@Override
	public ElementShort modNew(Element<?> other) {
		ElementShort res = copy();
		return res.mod(other);
	}

	@Override
	public ElementShort modOf(long a, long b) {
		try {
			value = (short)(a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort modOf(double a, long b) {
		try {
			value = (short)((short)a % (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort modOf(long a, double b) {
		try {
			value = (short)((short)a % (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementShort modOf(double a, double b) {
		try {
			value = (short)((short)a % (short)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort modOf(Element<?> a, long b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort modOf(long a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort modOf(Element<?> a, double b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			value = (short)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort modOf(double a, Element<?> b) {
		try {
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementShort modOf(Element<?> a, Element<?> b) {
		try {
			short av = ((ScalarElement)a).shortValue();
			short bv = ((ScalarElement)b).shortValue();
			value = (short)(av % bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	// BOUNDING

	@Override
	public ElementShort min(long other) {
		if (other < value)
			value = (short)other;
		return this;
	}
	@Override
	public ElementShort min(double other) {
		if (other < value)
			value = (short)other;
		return this;
	}
	@Override
	public ElementShort min(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).shortValue() < value)
			value = ((ScalarElement)other).shortValue();
		return this;
	}

	@Override
	public ElementShort minNew(long other) {
		ElementShort res = copy();
		return res.min(other);
	}
	@Override
	public ElementShort minNew(double other) {
		ElementShort res = copy();
		return res.min(other);
	}
	@Override
	public ElementShort minNew(Element<?> other) {
		ElementShort res = copy();
		return res.min(other);
	}

	@Override
	public ElementShort minOf(long a, long b) {
		if (a < b)
			value = (short)a;
		else
			value = (short)b;
		valid = true;
		return this;
	}
	@Override
	public ElementShort minOf(double a, long b) {
		if (a < b)
			value = (short)a;
		else
			value = (short)b;
		valid = true;
		return this;
	}
	@Override
	public ElementShort minOf(long a, double b) {
		if (a < b)
			value = (short)a;
		else
			value = (short)b;
		valid = true;
		return this;
	}
	@Override
	public ElementShort minOf(double a, double b) {
		if (a < b)
			value = (short)a;
		else
			value = (short)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort minOf(Element<?> a, long b) {
		short av = ((ScalarElement)a).shortValue();
		if (av < b)
			value = (short)av;
		else
			value = (short)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort minOf(long a, Element<?> b) {
		short bv = ((ScalarElement)b).shortValue();
		if (a < bv)
			value = (short)a;
		else
			value = (short)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort minOf(Element<?> a, double b) {
		short av = ((ScalarElement)a).shortValue();
		if (av < b)
			value = (short)av;
		else
			value = (short)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort minOf(double a, Element<?> b) {
		short bv = ((ScalarElement)b).shortValue();
		if (a < bv)
			value = (short)a;
		else
			value = (short)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementShort minOf(Element<?> a, Element<?> b) {
		short av = ((ScalarElement)a).shortValue();
		short bv = ((ScalarElement)b).shortValue();
		if (av < bv)
			value = (short)av;
		else
			value = (short)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementShort max(long other) {
		if (other > value)
			value = (short)other;
		return this;
	}
	@Override
	public ElementShort max(double other) {
		if (other > value)
			value = (short)other;
		return this;
	}
	@Override
	public ElementShort max(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).shortValue() > value)
			value = ((ScalarElement)other).shortValue();
		return this;
	}

	@Override
	public ElementShort maxNew(long other) {
		ElementShort res = copy();
		return res.max(other);
	}
	@Override
	public ElementShort maxNew(double other) {
		ElementShort res = copy();
		return res.max(other);
	}
	@Override
	public ElementShort maxNew(Element<?> other) {
		ElementShort res = copy();
		return res.max(other);
	}

	@Override
	public ElementShort maxOf(long a, long b) {
		if (a > b)
			value = (short)a;
		else
			value = (short)b;
		valid = true;
		return this;
	}
	@Override
	public ElementShort maxOf(double a, long b) {
		if (a > b)
			value = (short)a;
		else
			value = (short)b;
		valid = true;
		return this;
	}
	@Override
	public ElementShort maxOf(long a, double b) {
		if (a > b)
			value = (short)a;
		else
			value = (short)b;
		valid = true;
		return this;
	}
	@Override
	public ElementShort maxOf(double a, double b) {
		if (a > b)
			value = (short)a;
		else
			value = (short)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort maxOf(Element<?> a, long b) {
		short av = ((ScalarElement)a).shortValue();
		if (av > b)
			value = (short)av;
		else
			value = (short)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort maxOf(long a, Element<?> b) {
		short bv = ((ScalarElement)b).shortValue();
		if (a > bv)
			value = (short)a;
		else
			value = (short)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementShort maxOf(Element<?> a, double b) {
		short av = ((ScalarElement)a).shortValue();
		if (av > b)
			value = (short)av;
		else
			value = (short)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementShort maxOf(double a, Element<?> b) {
		short bv = ((ScalarElement)b).shortValue();
		if (a > bv)
			value = (short)a;
		else
			value = (short)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to short before the operation.
	 * @param b This will be converted to short before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementShort maxOf(Element<?> a, Element<?> b) {
		short av = ((ScalarElement)a).shortValue();
		short bv = ((ScalarElement)b).shortValue();
		if (av > bv)
			value = (short)av;
		else
			value = (short)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementShort clamp(long min, long max) {
		if (value < min)
			value = (short)min;
		else if (value > max)
			value = (short)max;
		return this;
	}
	@Override
	public ElementShort clamp(double min, double max) {
		if (value < min)
			value = (short)min;
		else if (value > max)
			value = (short)max;
		return this;
	}
	@Override
	public ElementShort clamp(Element<?> min, Element<?> max) {
		if (!min.isValid() || !max.isValid()) {
			this.setValid(false);
			return this;
		}
		// This may throw an exception - but that's OK, because VectorElements
		// are not comparable.
		ScalarElement minS = (ScalarElement)min;
		ScalarElement maxS = (ScalarElement)max;
		if (value < minS.shortValue())
			value = minS.shortValue();
		else if (value > maxS.shortValue())
			value = maxS.shortValue();
		return this;
	}

	@Override
	public ElementShort clampNew(long min, long max) {
		ElementShort res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementShort clampNew(double min, double max) {
		ElementShort res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementShort clampNew(Element<?> min, Element<?> max) {
		ElementShort res = copy();
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
