
package org.vpac.ndg.query.math;

/**
 * A pixel that stores a long value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See ElementX_gen.py.
public class ElementLong implements ScalarElement {
	private long value;
	private boolean valid;

	/**
	 * Create a new ElementLong, initalised to zero.
	 */
	public ElementLong() {
		this.value = 0;
		this.valid = true;
	}
	/**
	 * Create a new ElementLong.
	 * @param value The initial value for the element.
	 */
	public ElementLong(long value) {
		this.value = value;
		this.valid = true;
	}
	@Override
	public ElementLong copy() {
		ElementLong res = new ElementLong(value);
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
	public void setValid(Element<?> mask) {
		this.valid = mask.isValid();
	}

	@Override
	public Long getValue() {
		return value;
	}

	// CASTING

	@Override
	public ElementLong set(Element<?> value) {
		this.value = ((ScalarElement)value).longValue();
		this.valid = value.isValid();
		return this;
	}
	@Override
	public ElementLong set(Number value) {
		this.value = value.longValue();
		this.valid = true;
		return this;
	}

	@Override
	public byte byteValue() {
		return (byte)value;
	}
	@Override
	public ElementLong set(byte value) {
		this.value = (long)value;
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
	public ElementLong set(short value) {
		this.value = (long)value;
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
	public ElementLong set(int value) {
		this.value = (long)value;
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
		return value;
	}
	@Override
	public ElementLong set(long value) {
		this.value = value;
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
	public ElementLong set(float value) {
		this.value = (long)value;
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
	public ElementLong set(double value) {
		this.value = (long)value;
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
	public ElementLong add(long other) {
		try {
			value = (long)(value + other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong add(double other) {
		try {
			value = (long)(value + (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementLong add(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (long)(value + ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong addIfValid(long other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value + other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if mask is a vector.
	 */
	@Override
	public ElementLong addIfValid(double other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value + (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong addIfValid(Element<?> other) {
		if (!other.isValid())
			return this;
		try {
			value = (long)(value + ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}
	@Override
	public ElementLong addIfValid(Element<?> other, Element<?> mask) {
		if (!other.isValid() || !mask.isValid())
			return this;
		try {
			value = (long)(value + ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}

	@Override
	public ElementLong addNew(long other) {
		ElementLong res = copy();
		return res.add(other);
	}
	@Override
	public ElementLong addNew(double other) {
		ElementLong res = copy();
		return res.add(other);
	}
	@Override
	public ElementLong addNew(Element<?> other) {
		ElementLong res = copy();
		return res.add(other);
	}

	@Override
	public ElementLong addNewIfValid(long other, Element<?> mask) {
		ElementLong res = copy();
		return res.addIfValid(other, mask);
	}
	@Override
	public ElementLong addNewIfValid(double other, Element<?> mask) {
		ElementLong res = copy();
		return res.addIfValid(other, mask);
	}
	@Override
	public ElementLong addNewIfValid(Element<?> other) {
		ElementLong res = copy();
		return res.addIfValid(other);
	}
	@Override
	public ElementLong addNewIfValid(Element<?> other, Element<?> mask) {
		ElementLong res = copy();
		return res.addIfValid(other, mask);
	}

	@Override
	public ElementLong addOf(long a, long b) {
		try {
			value = (long)(a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong addOf(double a, long b) {
		try {
			value = (long)((long)a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong addOf(long a, double b) {
		try {
			value = (long)(a + (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong addOf(double a, double b) {
		try {
			value = (long)(a + (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong addOf(Element<?> a, long b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong addOf(long a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong addOf(Element<?> a, double b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong addOf(double a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementLong addOf(Element<?> a, Element<?> b) {
		try {
			long av = ((ScalarElement)a).longValue();
			long bv = ((ScalarElement)b).longValue();
			value = (long)(av + bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong addOfIfValid(long a, long b, Element<?> mask) {
		if (mask.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(double a, long b, Element<?> mask) {
		if (mask.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(long a, double b, Element<?> mask) {
		if (mask.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(double a, double b, Element<?> mask) {
		if (mask.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(Element<?> a, long b) {
		if (a.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(long a, Element<?> b) {
		if (b.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(Element<?> a, double b) {
		if (a.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(double a, Element<?> b) {
		if (b.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(Element<?> a, Element<?> b) {
		if (a.isValid() && b.isValid())
			addOf(a, b);
		return this;
	}
	@Override
	public ElementLong addOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (a.isValid() && b.isValid() && mask.isValid())
			addOf(a, b);
		return this;
	}

	@Override
	public ElementLong sub(long other) {
		try {
			value = (long)(value - other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong sub(double other) {
		try {
			value = (long)(value - (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementLong sub(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (long)(value - ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong subIfValid(long other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value - other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if mask is a vector.
	 */
	@Override
	public ElementLong subIfValid(double other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value - (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong subIfValid(Element<?> other) {
		if (!other.isValid())
			return this;
		try {
			value = (long)(value - ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}
	@Override
	public ElementLong subIfValid(Element<?> other, Element<?> mask) {
		if (!other.isValid() || !mask.isValid())
			return this;
		try {
			value = (long)(value - ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}

	@Override
	public ElementLong subNew(long other) {
		ElementLong res = copy();
		return res.sub(other);
	}
	@Override
	public ElementLong subNew(double other) {
		ElementLong res = copy();
		return res.sub(other);
	}
	@Override
	public ElementLong subNew(Element<?> other) {
		ElementLong res = copy();
		return res.sub(other);
	}

	@Override
	public ElementLong subNewIfValid(long other, Element<?> mask) {
		ElementLong res = copy();
		return res.subIfValid(other, mask);
	}
	@Override
	public ElementLong subNewIfValid(double other, Element<?> mask) {
		ElementLong res = copy();
		return res.subIfValid(other, mask);
	}
	@Override
	public ElementLong subNewIfValid(Element<?> other) {
		ElementLong res = copy();
		return res.subIfValid(other);
	}
	@Override
	public ElementLong subNewIfValid(Element<?> other, Element<?> mask) {
		ElementLong res = copy();
		return res.subIfValid(other, mask);
	}

	@Override
	public ElementLong subOf(long a, long b) {
		try {
			value = (long)(a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong subOf(double a, long b) {
		try {
			value = (long)((long)a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong subOf(long a, double b) {
		try {
			value = (long)(a - (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong subOf(double a, double b) {
		try {
			value = (long)(a - (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong subOf(Element<?> a, long b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong subOf(long a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong subOf(Element<?> a, double b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong subOf(double a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementLong subOf(Element<?> a, Element<?> b) {
		try {
			long av = ((ScalarElement)a).longValue();
			long bv = ((ScalarElement)b).longValue();
			value = (long)(av - bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong subOfIfValid(long a, long b, Element<?> mask) {
		if (mask.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(double a, long b, Element<?> mask) {
		if (mask.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(long a, double b, Element<?> mask) {
		if (mask.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(double a, double b, Element<?> mask) {
		if (mask.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(Element<?> a, long b) {
		if (a.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(long a, Element<?> b) {
		if (b.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(Element<?> a, double b) {
		if (a.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(double a, Element<?> b) {
		if (b.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(Element<?> a, Element<?> b) {
		if (a.isValid() && b.isValid())
			subOf(a, b);
		return this;
	}
	@Override
	public ElementLong subOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (a.isValid() && b.isValid() && mask.isValid())
			subOf(a, b);
		return this;
	}

	@Override
	public ElementLong mul(long other) {
		try {
			value = (long)(value * other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong mul(double other) {
		try {
			value = (long)(value * (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementLong mul(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (long)(value * ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong mulIfValid(long other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value * other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if mask is a vector.
	 */
	@Override
	public ElementLong mulIfValid(double other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value * (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong mulIfValid(Element<?> other) {
		if (!other.isValid())
			return this;
		try {
			value = (long)(value * ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}
	@Override
	public ElementLong mulIfValid(Element<?> other, Element<?> mask) {
		if (!other.isValid() || !mask.isValid())
			return this;
		try {
			value = (long)(value * ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}

	@Override
	public ElementLong mulNew(long other) {
		ElementLong res = copy();
		return res.mul(other);
	}
	@Override
	public ElementLong mulNew(double other) {
		ElementLong res = copy();
		return res.mul(other);
	}
	@Override
	public ElementLong mulNew(Element<?> other) {
		ElementLong res = copy();
		return res.mul(other);
	}

	@Override
	public ElementLong mulNewIfValid(long other, Element<?> mask) {
		ElementLong res = copy();
		return res.mulIfValid(other, mask);
	}
	@Override
	public ElementLong mulNewIfValid(double other, Element<?> mask) {
		ElementLong res = copy();
		return res.mulIfValid(other, mask);
	}
	@Override
	public ElementLong mulNewIfValid(Element<?> other) {
		ElementLong res = copy();
		return res.mulIfValid(other);
	}
	@Override
	public ElementLong mulNewIfValid(Element<?> other, Element<?> mask) {
		ElementLong res = copy();
		return res.mulIfValid(other, mask);
	}

	@Override
	public ElementLong mulOf(long a, long b) {
		try {
			value = (long)(a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong mulOf(double a, long b) {
		try {
			value = (long)((long)a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong mulOf(long a, double b) {
		try {
			value = (long)(a * (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong mulOf(double a, double b) {
		try {
			value = (long)(a * (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong mulOf(Element<?> a, long b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong mulOf(long a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong mulOf(Element<?> a, double b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong mulOf(double a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementLong mulOf(Element<?> a, Element<?> b) {
		try {
			long av = ((ScalarElement)a).longValue();
			long bv = ((ScalarElement)b).longValue();
			value = (long)(av * bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong mulOfIfValid(long a, long b, Element<?> mask) {
		if (mask.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(double a, long b, Element<?> mask) {
		if (mask.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(long a, double b, Element<?> mask) {
		if (mask.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(double a, double b, Element<?> mask) {
		if (mask.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(Element<?> a, long b) {
		if (a.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(long a, Element<?> b) {
		if (b.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(Element<?> a, double b) {
		if (a.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(double a, Element<?> b) {
		if (b.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(Element<?> a, Element<?> b) {
		if (a.isValid() && b.isValid())
			mulOf(a, b);
		return this;
	}
	@Override
	public ElementLong mulOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (a.isValid() && b.isValid() && mask.isValid())
			mulOf(a, b);
		return this;
	}

	@Override
	public ElementLong div(long other) {
		try {
			value = (long)(value / other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong div(double other) {
		try {
			value = (long)(value / (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementLong div(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (long)(value / ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong divIfValid(long other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value / other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if mask is a vector.
	 */
	@Override
	public ElementLong divIfValid(double other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value / (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong divIfValid(Element<?> other) {
		if (!other.isValid())
			return this;
		try {
			value = (long)(value / ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}
	@Override
	public ElementLong divIfValid(Element<?> other, Element<?> mask) {
		if (!other.isValid() || !mask.isValid())
			return this;
		try {
			value = (long)(value / ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}

	@Override
	public ElementLong divNew(long other) {
		ElementLong res = copy();
		return res.div(other);
	}
	@Override
	public ElementLong divNew(double other) {
		ElementLong res = copy();
		return res.div(other);
	}
	@Override
	public ElementLong divNew(Element<?> other) {
		ElementLong res = copy();
		return res.div(other);
	}

	@Override
	public ElementLong divNewIfValid(long other, Element<?> mask) {
		ElementLong res = copy();
		return res.divIfValid(other, mask);
	}
	@Override
	public ElementLong divNewIfValid(double other, Element<?> mask) {
		ElementLong res = copy();
		return res.divIfValid(other, mask);
	}
	@Override
	public ElementLong divNewIfValid(Element<?> other) {
		ElementLong res = copy();
		return res.divIfValid(other);
	}
	@Override
	public ElementLong divNewIfValid(Element<?> other, Element<?> mask) {
		ElementLong res = copy();
		return res.divIfValid(other, mask);
	}

	@Override
	public ElementLong divOf(long a, long b) {
		try {
			value = (long)(a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong divOf(double a, long b) {
		try {
			value = (long)((long)a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong divOf(long a, double b) {
		try {
			value = (long)(a / (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong divOf(double a, double b) {
		try {
			value = (long)(a / (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong divOf(Element<?> a, long b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong divOf(long a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong divOf(Element<?> a, double b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong divOf(double a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementLong divOf(Element<?> a, Element<?> b) {
		try {
			long av = ((ScalarElement)a).longValue();
			long bv = ((ScalarElement)b).longValue();
			value = (long)(av / bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong divOfIfValid(long a, long b, Element<?> mask) {
		if (mask.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(double a, long b, Element<?> mask) {
		if (mask.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(long a, double b, Element<?> mask) {
		if (mask.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(double a, double b, Element<?> mask) {
		if (mask.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(Element<?> a, long b) {
		if (a.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(long a, Element<?> b) {
		if (b.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(Element<?> a, double b) {
		if (a.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(double a, Element<?> b) {
		if (b.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(Element<?> a, Element<?> b) {
		if (a.isValid() && b.isValid())
			divOf(a, b);
		return this;
	}
	@Override
	public ElementLong divOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (a.isValid() && b.isValid() && mask.isValid())
			divOf(a, b);
		return this;
	}

	@Override
	public ElementLong mod(long other) {
		try {
			value = (long)(value % other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong mod(double other) {
		try {
			value = (long)(value % (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementLong mod(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (long)(value % ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong modIfValid(long other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value % other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if mask is a vector.
	 */
	@Override
	public ElementLong modIfValid(double other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		try {
			value = (long)(value % (long)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong modIfValid(Element<?> other) {
		if (!other.isValid())
			return this;
		try {
			value = (long)(value % ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}
	@Override
	public ElementLong modIfValid(Element<?> other, Element<?> mask) {
		if (!other.isValid() || !mask.isValid())
			return this;
		try {
			value = (long)(value % ((ScalarElement)other).longValue());
		} catch (ArithmeticException e) {
			// do nothing.
		}
		return this;
	}

	@Override
	public ElementLong modNew(long other) {
		ElementLong res = copy();
		return res.mod(other);
	}
	@Override
	public ElementLong modNew(double other) {
		ElementLong res = copy();
		return res.mod(other);
	}
	@Override
	public ElementLong modNew(Element<?> other) {
		ElementLong res = copy();
		return res.mod(other);
	}

	@Override
	public ElementLong modNewIfValid(long other, Element<?> mask) {
		ElementLong res = copy();
		return res.modIfValid(other, mask);
	}
	@Override
	public ElementLong modNewIfValid(double other, Element<?> mask) {
		ElementLong res = copy();
		return res.modIfValid(other, mask);
	}
	@Override
	public ElementLong modNewIfValid(Element<?> other) {
		ElementLong res = copy();
		return res.modIfValid(other);
	}
	@Override
	public ElementLong modNewIfValid(Element<?> other, Element<?> mask) {
		ElementLong res = copy();
		return res.modIfValid(other, mask);
	}

	@Override
	public ElementLong modOf(long a, long b) {
		try {
			value = (long)(a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong modOf(double a, long b) {
		try {
			value = (long)((long)a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong modOf(long a, double b) {
		try {
			value = (long)(a % (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementLong modOf(double a, double b) {
		try {
			value = (long)(a % (long)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong modOf(Element<?> a, long b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong modOf(long a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong modOf(Element<?> a, double b) {
		try {
			long av = ((ScalarElement)a).longValue();
			value = (long)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong modOf(double a, Element<?> b) {
		try {
			long bv = ((ScalarElement)b).longValue();
			value = (long)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementLong modOf(Element<?> a, Element<?> b) {
		try {
			long av = ((ScalarElement)a).longValue();
			long bv = ((ScalarElement)b).longValue();
			value = (long)(av % bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementLong modOfIfValid(long a, long b, Element<?> mask) {
		if (mask.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(double a, long b, Element<?> mask) {
		if (mask.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(long a, double b, Element<?> mask) {
		if (mask.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(double a, double b, Element<?> mask) {
		if (mask.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(Element<?> a, long b) {
		if (a.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(long a, Element<?> b) {
		if (b.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(Element<?> a, double b) {
		if (a.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(double a, Element<?> b) {
		if (b.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(Element<?> a, Element<?> b) {
		if (a.isValid() && b.isValid())
			modOf(a, b);
		return this;
	}
	@Override
	public ElementLong modOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (a.isValid() && b.isValid() && mask.isValid())
			modOf(a, b);
		return this;
	}

	// BOUNDING

	@Override
	public ElementLong min(long other) {
		if (other < value)
			value = other;
		return this;
	}
	@Override
	public ElementLong min(double other) {
		if (other < value)
			value = (long)other;
		return this;
	}
	@Override
	public ElementLong min(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).longValue() < value)
			value = ((ScalarElement)other).longValue();
		return this;
	}

	@Override
	public ElementLong minIfValid(long other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		if (other < value)
			value = other;
		return this;
	}
	@Override
	public ElementLong minIfValid(double other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		if (other < value)
			value = (long)other;
		return this;
	}
	@Override
	public ElementLong minIfValid(Element<?> other) {
		if (!other.isValid())
			return this;
		if (((ScalarElement)other).longValue() < value)
			value = ((ScalarElement)other).longValue();
		return this;
	}
	@Override
	public ElementLong minIfValid(Element<?> other, Element<?> mask) {
		if (!other.isValid() || !mask.isValid())
			return this;
		if (((ScalarElement)other).longValue() < value)
			value = ((ScalarElement)other).longValue();
		return this;
	}

	@Override
	public ElementLong minNew(long other) {
		ElementLong res = copy();
		return res.min(other);
	}
	@Override
	public ElementLong minNew(double other) {
		ElementLong res = copy();
		return res.min(other);
	}
	@Override
	public ElementLong minNew(Element<?> other) {
		ElementLong res = copy();
		return res.min(other);
	}

	@Override
	public ElementLong minNewIfValid(long other, Element<?> mask) {
		ElementLong res = copy();
		return res.minIfValid(other, mask);
	}
	@Override
	public ElementLong minNewIfValid(double other, Element<?> mask) {
		ElementLong res = copy();
		return res.minIfValid(other, mask);
	}
	@Override
	public ElementLong minNewIfValid(Element<?> other) {
		ElementLong res = copy();
		return res.minIfValid(other);
	}
	@Override
	public ElementLong minNewIfValid(Element<?> other, Element<?> mask) {
		ElementLong res = copy();
		return res.minIfValid(other, mask);
	}

	@Override
	public ElementLong minOf(long a, long b) {
		if (a < b)
			value = (long)a;
		else
			value = (long)b;
		valid = true;
		return this;
	}
	@Override
	public ElementLong minOf(double a, long b) {
		if (a < b)
			value = (long)a;
		else
			value = (long)b;
		valid = true;
		return this;
	}
	@Override
	public ElementLong minOf(long a, double b) {
		if (a < b)
			value = (long)a;
		else
			value = (long)b;
		valid = true;
		return this;
	}
	@Override
	public ElementLong minOf(double a, double b) {
		if (a < b)
			value = (long)a;
		else
			value = (long)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong minOf(Element<?> a, long b) {
		long av = ((ScalarElement)a).longValue();
		if (av < b)
			value = (long)av;
		else
			value = (long)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong minOf(long a, Element<?> b) {
		long bv = ((ScalarElement)b).longValue();
		if (a < bv)
			value = (long)a;
		else
			value = (long)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong minOf(Element<?> a, double b) {
		long av = ((ScalarElement)a).longValue();
		if (av < b)
			value = (long)av;
		else
			value = (long)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong minOf(double a, Element<?> b) {
		long bv = ((ScalarElement)b).longValue();
		if (a < bv)
			value = (long)a;
		else
			value = (long)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementLong minOf(Element<?> a, Element<?> b) {
		long av = ((ScalarElement)a).longValue();
		long bv = ((ScalarElement)b).longValue();
		if (av < bv)
			value = (long)av;
		else
			value = (long)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementLong minOfIfValid(long a, long b, Element<?> mask) {
		if (mask.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(double a, long b, Element<?> mask) {
		if (mask.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(long a, double b, Element<?> mask) {
		if (mask.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(double a, double b, Element<?> mask) {
		if (mask.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(Element<?> a, long b) {
		if (a.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(long a, Element<?> b) {
		if (b.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(Element<?> a, double b) {
		if (a.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(double a, Element<?> b) {
		if (b.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(Element<?> a, Element<?> b) {
		if (a.isValid() && b.isValid())
			minOf(a, b);
		return this;
	}
	@Override
	public ElementLong minOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (a.isValid() && b.isValid() && mask.isValid())
			minOf(a, b);
		return this;
	}

	@Override
	public ElementLong max(long other) {
		if (other > value)
			value = other;
		return this;
	}
	@Override
	public ElementLong max(double other) {
		if (other > value)
			value = (long)other;
		return this;
	}
	@Override
	public ElementLong max(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).longValue() > value)
			value = ((ScalarElement)other).longValue();
		return this;
	}

	@Override
	public ElementLong maxIfValid(long other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		if (other > value)
			value = other;
		return this;
	}
	@Override
	public ElementLong maxIfValid(double other, Element<?> mask) {
		if (!mask.isValid())
			return this;
		if (other > value)
			value = (long)other;
		return this;
	}
	@Override
	public ElementLong maxIfValid(Element<?> other) {
		if (!other.isValid())
			return this;
		if (((ScalarElement)other).longValue() > value)
			value = ((ScalarElement)other).longValue();
		return this;
	}
	@Override
	public ElementLong maxIfValid(Element<?> other, Element<?> mask) {
		if (!other.isValid() || !mask.isValid())
			return this;
		if (((ScalarElement)other).longValue() > value)
			value = ((ScalarElement)other).longValue();
		return this;
	}

	@Override
	public ElementLong maxNew(long other) {
		ElementLong res = copy();
		return res.max(other);
	}
	@Override
	public ElementLong maxNew(double other) {
		ElementLong res = copy();
		return res.max(other);
	}
	@Override
	public ElementLong maxNew(Element<?> other) {
		ElementLong res = copy();
		return res.max(other);
	}

	@Override
	public ElementLong maxNewIfValid(long other, Element<?> mask) {
		ElementLong res = copy();
		return res.maxIfValid(other, mask);
	}
	@Override
	public ElementLong maxNewIfValid(double other, Element<?> mask) {
		ElementLong res = copy();
		return res.maxIfValid(other, mask);
	}
	@Override
	public ElementLong maxNewIfValid(Element<?> other) {
		ElementLong res = copy();
		return res.maxIfValid(other);
	}
	@Override
	public ElementLong maxNewIfValid(Element<?> other, Element<?> mask) {
		ElementLong res = copy();
		return res.maxIfValid(other, mask);
	}

	@Override
	public ElementLong maxOf(long a, long b) {
		if (a > b)
			value = (long)a;
		else
			value = (long)b;
		valid = true;
		return this;
	}
	@Override
	public ElementLong maxOf(double a, long b) {
		if (a > b)
			value = (long)a;
		else
			value = (long)b;
		valid = true;
		return this;
	}
	@Override
	public ElementLong maxOf(long a, double b) {
		if (a > b)
			value = (long)a;
		else
			value = (long)b;
		valid = true;
		return this;
	}
	@Override
	public ElementLong maxOf(double a, double b) {
		if (a > b)
			value = (long)a;
		else
			value = (long)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong maxOf(Element<?> a, long b) {
		long av = ((ScalarElement)a).longValue();
		if (av > b)
			value = (long)av;
		else
			value = (long)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong maxOf(long a, Element<?> b) {
		long bv = ((ScalarElement)b).longValue();
		if (a > bv)
			value = (long)a;
		else
			value = (long)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementLong maxOf(Element<?> a, double b) {
		long av = ((ScalarElement)a).longValue();
		if (av > b)
			value = (long)av;
		else
			value = (long)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementLong maxOf(double a, Element<?> b) {
		long bv = ((ScalarElement)b).longValue();
		if (a > bv)
			value = (long)a;
		else
			value = (long)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to long before the operation.
	 * @param b This will be converted to long before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementLong maxOf(Element<?> a, Element<?> b) {
		long av = ((ScalarElement)a).longValue();
		long bv = ((ScalarElement)b).longValue();
		if (av > bv)
			value = (long)av;
		else
			value = (long)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementLong maxOfIfValid(long a, long b, Element<?> mask) {
		if (mask.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(double a, long b, Element<?> mask) {
		if (mask.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(long a, double b, Element<?> mask) {
		if (mask.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(double a, double b, Element<?> mask) {
		if (mask.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(Element<?> a, long b) {
		if (a.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(long a, Element<?> b) {
		if (b.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(Element<?> a, double b) {
		if (a.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.isValid() && mask.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(double a, Element<?> b) {
		if (b.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.isValid() && mask.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(Element<?> a, Element<?> b) {
		if (a.isValid() && b.isValid())
			maxOf(a, b);
		return this;
	}
	@Override
	public ElementLong maxOfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (a.isValid() && b.isValid() && mask.isValid())
			maxOf(a, b);
		return this;
	}

	@Override
	public ElementLong clamp(long min, long max) {
		if (value < min)
			value = min;
		else if (value > max)
			value = max;
		return this;
	}
	@Override
	public ElementLong clamp(double min, double max) {
		if (value < min)
			value = (long)min;
		else if (value > max)
			value = (long)max;
		return this;
	}
	@Override
	public ElementLong clamp(Element<?> min, Element<?> max) {
		if (!min.isValid() || !max.isValid()) {
			this.setValid(false);
			return this;
		}
		// This may throw an exception - but that's OK, because VectorElements
		// are not comparable.
		ScalarElement minS = (ScalarElement)min;
		ScalarElement maxS = (ScalarElement)max;
		if (value < minS.longValue())
			value = minS.longValue();
		else if (value > maxS.longValue())
			value = maxS.longValue();
		return this;
	}

	@Override
	public ElementLong clampNew(long min, long max) {
		ElementLong res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementLong clampNew(double min, double max) {
		ElementLong res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementLong clampNew(Element<?> min, Element<?> max) {
		ElementLong res = copy();
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
		return prime + (int) (value ^ (value >>> 32));
	}


	@Override
	public String toString() {
		if (!isValid())
			return String.format("!%d", value);
		else
			return String.format("%d", value);
	}
}
