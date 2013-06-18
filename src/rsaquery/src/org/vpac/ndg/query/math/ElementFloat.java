
package org.vpac.ndg.query.math;

/**
 * A pixel that stores a float value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See ElementX_gen.py.
public class ElementFloat implements ScalarElement {
	private float value;
	private boolean valid;

	/**
	 * Create a new ElementFloat, initalised to zero.
	 */
	public ElementFloat() {
		this.value = 0;
		this.valid = true;
	}
	/**
	 * Create a new ElementFloat.
	 * @param value The initial value for the element.
	 */
	public ElementFloat(float value) {
		this.value = value;
		this.valid = true;
	}
	@Override
	public ElementFloat copy() {
		ElementFloat res = new ElementFloat(value);
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
	public Float getValue() {
		return value;
	}

	// CASTING

	@Override
	public ElementFloat set(Element<?> value) {
		this.value = ((ScalarElement)value).floatValue();
		this.valid = value.isValid();
		return this;
	}
	@Override
	public ElementFloat set(Number value) {
		this.value = value.floatValue();
		this.valid = true;
		return this;
	}

	@Override
	public byte byteValue() {
		return (byte)value;
	}
	@Override
	public ElementFloat set(byte value) {
		this.value = (float)value;
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
	public ElementFloat set(short value) {
		this.value = (float)value;
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
	public ElementFloat set(int value) {
		this.value = (float)value;
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
	public ElementFloat set(long value) {
		this.value = (float)value;
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
		return value;
	}
	@Override
	public ElementFloat set(float value) {
		this.value = value;
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
	public ElementFloat set(double value) {
		this.value = (float)value;
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
	public ElementFloat add(long other) {
		try {
			value = (float)(value + (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat add(double other) {
		try {
			value = (float)(value + (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementFloat add(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (float)(value + ((ScalarElement)other).floatValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat addNew(long other) {
		ElementFloat res = copy();
		return res.add(other);
	}
	@Override
	public ElementFloat addNew(double other) {
		ElementFloat res = copy();
		return res.add(other);
	}
	@Override
	public ElementFloat addNew(Element<?> other) {
		ElementFloat res = copy();
		return res.add(other);
	}

	@Override
	public ElementFloat addOf(long a, long b) {
		try {
			value = (float)(a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat addOf(double a, long b) {
		try {
			value = (float)((float)a + (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat addOf(long a, double b) {
		try {
			value = (float)((float)a + (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat addOf(double a, double b) {
		try {
			value = (float)((float)a + (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat addOf(Element<?> a, long b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat addOf(long a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat addOf(Element<?> a, double b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat addOf(double a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementFloat addOf(Element<?> a, Element<?> b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(av + bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat sub(long other) {
		try {
			value = (float)(value - (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat sub(double other) {
		try {
			value = (float)(value - (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementFloat sub(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (float)(value - ((ScalarElement)other).floatValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat subNew(long other) {
		ElementFloat res = copy();
		return res.sub(other);
	}
	@Override
	public ElementFloat subNew(double other) {
		ElementFloat res = copy();
		return res.sub(other);
	}
	@Override
	public ElementFloat subNew(Element<?> other) {
		ElementFloat res = copy();
		return res.sub(other);
	}

	@Override
	public ElementFloat subOf(long a, long b) {
		try {
			value = (float)(a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat subOf(double a, long b) {
		try {
			value = (float)((float)a - (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat subOf(long a, double b) {
		try {
			value = (float)((float)a - (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat subOf(double a, double b) {
		try {
			value = (float)((float)a - (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat subOf(Element<?> a, long b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat subOf(long a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat subOf(Element<?> a, double b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat subOf(double a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementFloat subOf(Element<?> a, Element<?> b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(av - bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat mul(long other) {
		try {
			value = (float)(value * (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat mul(double other) {
		try {
			value = (float)(value * (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementFloat mul(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (float)(value * ((ScalarElement)other).floatValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat mulNew(long other) {
		ElementFloat res = copy();
		return res.mul(other);
	}
	@Override
	public ElementFloat mulNew(double other) {
		ElementFloat res = copy();
		return res.mul(other);
	}
	@Override
	public ElementFloat mulNew(Element<?> other) {
		ElementFloat res = copy();
		return res.mul(other);
	}

	@Override
	public ElementFloat mulOf(long a, long b) {
		try {
			value = (float)(a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat mulOf(double a, long b) {
		try {
			value = (float)((float)a * (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat mulOf(long a, double b) {
		try {
			value = (float)((float)a * (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat mulOf(double a, double b) {
		try {
			value = (float)((float)a * (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat mulOf(Element<?> a, long b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat mulOf(long a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat mulOf(Element<?> a, double b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat mulOf(double a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementFloat mulOf(Element<?> a, Element<?> b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(av * bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat div(long other) {
		try {
			value = (float)(value / (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat div(double other) {
		try {
			value = (float)(value / (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementFloat div(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (float)(value / ((ScalarElement)other).floatValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat divNew(long other) {
		ElementFloat res = copy();
		return res.div(other);
	}
	@Override
	public ElementFloat divNew(double other) {
		ElementFloat res = copy();
		return res.div(other);
	}
	@Override
	public ElementFloat divNew(Element<?> other) {
		ElementFloat res = copy();
		return res.div(other);
	}

	@Override
	public ElementFloat divOf(long a, long b) {
		try {
			value = (float)(a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat divOf(double a, long b) {
		try {
			value = (float)((float)a / (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat divOf(long a, double b) {
		try {
			value = (float)((float)a / (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat divOf(double a, double b) {
		try {
			value = (float)((float)a / (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat divOf(Element<?> a, long b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat divOf(long a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat divOf(Element<?> a, double b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat divOf(double a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementFloat divOf(Element<?> a, Element<?> b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(av / bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat mod(long other) {
		try {
			value = (float)(value % (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat mod(double other) {
		try {
			value = (float)(value % (float)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementFloat mod(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (float)(value % ((ScalarElement)other).floatValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementFloat modNew(long other) {
		ElementFloat res = copy();
		return res.mod(other);
	}
	@Override
	public ElementFloat modNew(double other) {
		ElementFloat res = copy();
		return res.mod(other);
	}
	@Override
	public ElementFloat modNew(Element<?> other) {
		ElementFloat res = copy();
		return res.mod(other);
	}

	@Override
	public ElementFloat modOf(long a, long b) {
		try {
			value = (float)(a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat modOf(double a, long b) {
		try {
			value = (float)((float)a % (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat modOf(long a, double b) {
		try {
			value = (float)((float)a % (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementFloat modOf(double a, double b) {
		try {
			value = (float)((float)a % (float)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat modOf(Element<?> a, long b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat modOf(long a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat modOf(Element<?> a, double b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			value = (float)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat modOf(double a, Element<?> b) {
		try {
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementFloat modOf(Element<?> a, Element<?> b) {
		try {
			float av = ((ScalarElement)a).floatValue();
			float bv = ((ScalarElement)b).floatValue();
			value = (float)(av % bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	// BOUNDING

	@Override
	public ElementFloat min(long other) {
		if (other < value)
			value = (float)other;
		return this;
	}
	@Override
	public ElementFloat min(double other) {
		if (other < value)
			value = (float)other;
		return this;
	}
	@Override
	public ElementFloat min(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).floatValue() < value)
			value = ((ScalarElement)other).floatValue();
		return this;
	}

	@Override
	public ElementFloat minNew(long other) {
		ElementFloat res = copy();
		return res.min(other);
	}
	@Override
	public ElementFloat minNew(double other) {
		ElementFloat res = copy();
		return res.min(other);
	}
	@Override
	public ElementFloat minNew(Element<?> other) {
		ElementFloat res = copy();
		return res.min(other);
	}

	@Override
	public ElementFloat minOf(long a, long b) {
		if (a < b)
			value = (float)a;
		else
			value = (float)b;
		valid = true;
		return this;
	}
	@Override
	public ElementFloat minOf(double a, long b) {
		if (a < b)
			value = (float)a;
		else
			value = (float)b;
		valid = true;
		return this;
	}
	@Override
	public ElementFloat minOf(long a, double b) {
		if (a < b)
			value = (float)a;
		else
			value = (float)b;
		valid = true;
		return this;
	}
	@Override
	public ElementFloat minOf(double a, double b) {
		if (a < b)
			value = (float)a;
		else
			value = (float)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat minOf(Element<?> a, long b) {
		float av = ((ScalarElement)a).floatValue();
		if (av < b)
			value = (float)av;
		else
			value = (float)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat minOf(long a, Element<?> b) {
		float bv = ((ScalarElement)b).floatValue();
		if (a < bv)
			value = (float)a;
		else
			value = (float)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat minOf(Element<?> a, double b) {
		float av = ((ScalarElement)a).floatValue();
		if (av < b)
			value = (float)av;
		else
			value = (float)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat minOf(double a, Element<?> b) {
		float bv = ((ScalarElement)b).floatValue();
		if (a < bv)
			value = (float)a;
		else
			value = (float)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementFloat minOf(Element<?> a, Element<?> b) {
		float av = ((ScalarElement)a).floatValue();
		float bv = ((ScalarElement)b).floatValue();
		if (av < bv)
			value = (float)av;
		else
			value = (float)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementFloat max(long other) {
		if (other > value)
			value = (float)other;
		return this;
	}
	@Override
	public ElementFloat max(double other) {
		if (other > value)
			value = (float)other;
		return this;
	}
	@Override
	public ElementFloat max(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).floatValue() > value)
			value = ((ScalarElement)other).floatValue();
		return this;
	}

	@Override
	public ElementFloat maxNew(long other) {
		ElementFloat res = copy();
		return res.max(other);
	}
	@Override
	public ElementFloat maxNew(double other) {
		ElementFloat res = copy();
		return res.max(other);
	}
	@Override
	public ElementFloat maxNew(Element<?> other) {
		ElementFloat res = copy();
		return res.max(other);
	}

	@Override
	public ElementFloat maxOf(long a, long b) {
		if (a > b)
			value = (float)a;
		else
			value = (float)b;
		valid = true;
		return this;
	}
	@Override
	public ElementFloat maxOf(double a, long b) {
		if (a > b)
			value = (float)a;
		else
			value = (float)b;
		valid = true;
		return this;
	}
	@Override
	public ElementFloat maxOf(long a, double b) {
		if (a > b)
			value = (float)a;
		else
			value = (float)b;
		valid = true;
		return this;
	}
	@Override
	public ElementFloat maxOf(double a, double b) {
		if (a > b)
			value = (float)a;
		else
			value = (float)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat maxOf(Element<?> a, long b) {
		float av = ((ScalarElement)a).floatValue();
		if (av > b)
			value = (float)av;
		else
			value = (float)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat maxOf(long a, Element<?> b) {
		float bv = ((ScalarElement)b).floatValue();
		if (a > bv)
			value = (float)a;
		else
			value = (float)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementFloat maxOf(Element<?> a, double b) {
		float av = ((ScalarElement)a).floatValue();
		if (av > b)
			value = (float)av;
		else
			value = (float)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementFloat maxOf(double a, Element<?> b) {
		float bv = ((ScalarElement)b).floatValue();
		if (a > bv)
			value = (float)a;
		else
			value = (float)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to float before the operation.
	 * @param b This will be converted to float before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementFloat maxOf(Element<?> a, Element<?> b) {
		float av = ((ScalarElement)a).floatValue();
		float bv = ((ScalarElement)b).floatValue();
		if (av > bv)
			value = (float)av;
		else
			value = (float)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementFloat clamp(long min, long max) {
		if (value < min)
			value = (float)min;
		else if (value > max)
			value = (float)max;
		return this;
	}
	@Override
	public ElementFloat clamp(double min, double max) {
		if (value < min)
			value = (float)min;
		else if (value > max)
			value = (float)max;
		return this;
	}
	@Override
	public ElementFloat clamp(Element<?> min, Element<?> max) {
		if (!min.isValid() || !max.isValid()) {
			this.setValid(false);
			return this;
		}
		// This may throw an exception - but that's OK, because VectorElements
		// are not comparable.
		ScalarElement minS = (ScalarElement)min;
		ScalarElement maxS = (ScalarElement)max;
		if (value < minS.floatValue())
			value = minS.floatValue();
		else if (value > maxS.floatValue())
			value = maxS.floatValue();
		return this;
	}

	@Override
	public ElementFloat clampNew(long min, long max) {
		ElementFloat res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementFloat clampNew(double min, double max) {
		ElementFloat res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementFloat clampNew(Element<?> min, Element<?> max) {
		ElementFloat res = copy();
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
		return prime + Float.floatToIntBits(value);
	}


	@Override
	public String toString() {
		return String.format("%g", value);
	}
}
