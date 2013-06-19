
package org.vpac.ndg.query.math;

/**
 * A pixel that stores a byte value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See ElementX_gen.py.
public class ElementByte implements ScalarElement {
	private byte value;
	private boolean valid;

	/**
	 * Create a new ElementByte, initalised to zero.
	 */
	public ElementByte() {
		this.value = 0;
		this.valid = true;
	}
	/**
	 * Create a new ElementByte.
	 * @param value The initial value for the element.
	 */
	public ElementByte(byte value) {
		this.value = value;
		this.valid = true;
	}
	@Override
	public ElementByte copy() {
		ElementByte res = new ElementByte(value);
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
	public Byte getValue() {
		return value;
	}

	// CASTING

	@Override
	public ElementByte set(Element<?> value) {
		this.value = ((ScalarElement)value).byteValue();
		this.valid = value.isValid();
		return this;
	}
	@Override
	public ElementByte set(Number value) {
		this.value = value.byteValue();
		this.valid = true;
		return this;
	}

	@Override
	public byte byteValue() {
		return value;
	}
	@Override
	public ElementByte set(byte value) {
		this.value = value;
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
	public ElementByte set(short value) {
		this.value = (byte)value;
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
	public ElementByte set(int value) {
		this.value = (byte)value;
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
	public ElementByte set(long value) {
		this.value = (byte)value;
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
	public ElementByte set(float value) {
		this.value = (byte)value;
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
	public ElementByte set(double value) {
		this.value = (byte)value;
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
	public ElementByte add(long other) {
		try {
			value = (byte)(value + (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte add(double other) {
		try {
			value = (byte)(value + (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementByte add(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (byte)(value + ((ScalarElement)other).byteValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte addNew(long other) {
		ElementByte res = copy();
		return res.add(other);
	}
	@Override
	public ElementByte addNew(double other) {
		ElementByte res = copy();
		return res.add(other);
	}
	@Override
	public ElementByte addNew(Element<?> other) {
		ElementByte res = copy();
		return res.add(other);
	}

	@Override
	public ElementByte addOf(long a, long b) {
		try {
			value = (byte)(a + b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte addOf(double a, long b) {
		try {
			value = (byte)((byte)a + (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte addOf(long a, double b) {
		try {
			value = (byte)((byte)a + (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte addOf(double a, double b) {
		try {
			value = (byte)((byte)a + (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte addOf(Element<?> a, long b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte addOf(long a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte addOf(Element<?> a, double b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av + b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte addOf(double a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a + bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementByte addOf(Element<?> a, Element<?> b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(av + bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte sub(long other) {
		try {
			value = (byte)(value - (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte sub(double other) {
		try {
			value = (byte)(value - (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementByte sub(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (byte)(value - ((ScalarElement)other).byteValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte subNew(long other) {
		ElementByte res = copy();
		return res.sub(other);
	}
	@Override
	public ElementByte subNew(double other) {
		ElementByte res = copy();
		return res.sub(other);
	}
	@Override
	public ElementByte subNew(Element<?> other) {
		ElementByte res = copy();
		return res.sub(other);
	}

	@Override
	public ElementByte subOf(long a, long b) {
		try {
			value = (byte)(a - b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte subOf(double a, long b) {
		try {
			value = (byte)((byte)a - (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte subOf(long a, double b) {
		try {
			value = (byte)((byte)a - (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte subOf(double a, double b) {
		try {
			value = (byte)((byte)a - (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte subOf(Element<?> a, long b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte subOf(long a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte subOf(Element<?> a, double b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av - b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte subOf(double a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a - bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementByte subOf(Element<?> a, Element<?> b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(av - bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte mul(long other) {
		try {
			value = (byte)(value * (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte mul(double other) {
		try {
			value = (byte)(value * (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementByte mul(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (byte)(value * ((ScalarElement)other).byteValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte mulNew(long other) {
		ElementByte res = copy();
		return res.mul(other);
	}
	@Override
	public ElementByte mulNew(double other) {
		ElementByte res = copy();
		return res.mul(other);
	}
	@Override
	public ElementByte mulNew(Element<?> other) {
		ElementByte res = copy();
		return res.mul(other);
	}

	@Override
	public ElementByte mulOf(long a, long b) {
		try {
			value = (byte)(a * b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte mulOf(double a, long b) {
		try {
			value = (byte)((byte)a * (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte mulOf(long a, double b) {
		try {
			value = (byte)((byte)a * (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte mulOf(double a, double b) {
		try {
			value = (byte)((byte)a * (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte mulOf(Element<?> a, long b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte mulOf(long a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte mulOf(Element<?> a, double b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av * b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte mulOf(double a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a * bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementByte mulOf(Element<?> a, Element<?> b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(av * bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte div(long other) {
		try {
			value = (byte)(value / (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte div(double other) {
		try {
			value = (byte)(value / (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementByte div(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (byte)(value / ((ScalarElement)other).byteValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte divNew(long other) {
		ElementByte res = copy();
		return res.div(other);
	}
	@Override
	public ElementByte divNew(double other) {
		ElementByte res = copy();
		return res.div(other);
	}
	@Override
	public ElementByte divNew(Element<?> other) {
		ElementByte res = copy();
		return res.div(other);
	}

	@Override
	public ElementByte divOf(long a, long b) {
		try {
			value = (byte)(a / b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte divOf(double a, long b) {
		try {
			value = (byte)((byte)a / (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte divOf(long a, double b) {
		try {
			value = (byte)((byte)a / (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte divOf(double a, double b) {
		try {
			value = (byte)((byte)a / (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte divOf(Element<?> a, long b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte divOf(long a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte divOf(Element<?> a, double b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av / b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte divOf(double a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a / bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementByte divOf(Element<?> a, Element<?> b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(av / bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte mod(long other) {
		try {
			value = (byte)(value % (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte mod(double other) {
		try {
			value = (byte)(value % (byte)other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public ElementByte mod(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = (byte)(value % ((ScalarElement)other).byteValue());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public ElementByte modNew(long other) {
		ElementByte res = copy();
		return res.mod(other);
	}
	@Override
	public ElementByte modNew(double other) {
		ElementByte res = copy();
		return res.mod(other);
	}
	@Override
	public ElementByte modNew(Element<?> other) {
		ElementByte res = copy();
		return res.mod(other);
	}

	@Override
	public ElementByte modOf(long a, long b) {
		try {
			value = (byte)(a % b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte modOf(double a, long b) {
		try {
			value = (byte)((byte)a % (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte modOf(long a, double b) {
		try {
			value = (byte)((byte)a % (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public ElementByte modOf(double a, double b) {
		try {
			value = (byte)((byte)a % (byte)b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte modOf(Element<?> a, long b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte modOf(long a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte modOf(Element<?> a, double b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			value = (byte)(av % b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte modOf(double a, Element<?> b) {
		try {
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(a % bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementByte modOf(Element<?> a, Element<?> b) {
		try {
			byte av = ((ScalarElement)a).byteValue();
			byte bv = ((ScalarElement)b).byteValue();
			value = (byte)(av % bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	// BOUNDING

	@Override
	public ElementByte min(long other) {
		if (other < value)
			value = (byte)other;
		return this;
	}
	@Override
	public ElementByte min(double other) {
		if (other < value)
			value = (byte)other;
		return this;
	}
	@Override
	public ElementByte min(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).byteValue() < value)
			value = ((ScalarElement)other).byteValue();
		return this;
	}

	@Override
	public ElementByte minNew(long other) {
		ElementByte res = copy();
		return res.min(other);
	}
	@Override
	public ElementByte minNew(double other) {
		ElementByte res = copy();
		return res.min(other);
	}
	@Override
	public ElementByte minNew(Element<?> other) {
		ElementByte res = copy();
		return res.min(other);
	}

	@Override
	public ElementByte minOf(long a, long b) {
		if (a < b)
			value = (byte)a;
		else
			value = (byte)b;
		valid = true;
		return this;
	}
	@Override
	public ElementByte minOf(double a, long b) {
		if (a < b)
			value = (byte)a;
		else
			value = (byte)b;
		valid = true;
		return this;
	}
	@Override
	public ElementByte minOf(long a, double b) {
		if (a < b)
			value = (byte)a;
		else
			value = (byte)b;
		valid = true;
		return this;
	}
	@Override
	public ElementByte minOf(double a, double b) {
		if (a < b)
			value = (byte)a;
		else
			value = (byte)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte minOf(Element<?> a, long b) {
		byte av = ((ScalarElement)a).byteValue();
		if (av < b)
			value = (byte)av;
		else
			value = (byte)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte minOf(long a, Element<?> b) {
		byte bv = ((ScalarElement)b).byteValue();
		if (a < bv)
			value = (byte)a;
		else
			value = (byte)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte minOf(Element<?> a, double b) {
		byte av = ((ScalarElement)a).byteValue();
		if (av < b)
			value = (byte)av;
		else
			value = (byte)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte minOf(double a, Element<?> b) {
		byte bv = ((ScalarElement)b).byteValue();
		if (a < bv)
			value = (byte)a;
		else
			value = (byte)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementByte minOf(Element<?> a, Element<?> b) {
		byte av = ((ScalarElement)a).byteValue();
		byte bv = ((ScalarElement)b).byteValue();
		if (av < bv)
			value = (byte)av;
		else
			value = (byte)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementByte max(long other) {
		if (other > value)
			value = (byte)other;
		return this;
	}
	@Override
	public ElementByte max(double other) {
		if (other > value)
			value = (byte)other;
		return this;
	}
	@Override
	public ElementByte max(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).byteValue() > value)
			value = ((ScalarElement)other).byteValue();
		return this;
	}

	@Override
	public ElementByte maxNew(long other) {
		ElementByte res = copy();
		return res.max(other);
	}
	@Override
	public ElementByte maxNew(double other) {
		ElementByte res = copy();
		return res.max(other);
	}
	@Override
	public ElementByte maxNew(Element<?> other) {
		ElementByte res = copy();
		return res.max(other);
	}

	@Override
	public ElementByte maxOf(long a, long b) {
		if (a > b)
			value = (byte)a;
		else
			value = (byte)b;
		valid = true;
		return this;
	}
	@Override
	public ElementByte maxOf(double a, long b) {
		if (a > b)
			value = (byte)a;
		else
			value = (byte)b;
		valid = true;
		return this;
	}
	@Override
	public ElementByte maxOf(long a, double b) {
		if (a > b)
			value = (byte)a;
		else
			value = (byte)b;
		valid = true;
		return this;
	}
	@Override
	public ElementByte maxOf(double a, double b) {
		if (a > b)
			value = (byte)a;
		else
			value = (byte)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte maxOf(Element<?> a, long b) {
		byte av = ((ScalarElement)a).byteValue();
		if (av > b)
			value = (byte)av;
		else
			value = (byte)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte maxOf(long a, Element<?> b) {
		byte bv = ((ScalarElement)b).byteValue();
		if (a > bv)
			value = (byte)a;
		else
			value = (byte)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public ElementByte maxOf(Element<?> a, double b) {
		byte av = ((ScalarElement)a).byteValue();
		if (av > b)
			value = (byte)av;
		else
			value = (byte)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public ElementByte maxOf(double a, Element<?> b) {
		byte bv = ((ScalarElement)b).byteValue();
		if (a > bv)
			value = (byte)a;
		else
			value = (byte)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to byte before the operation.
	 * @param b This will be converted to byte before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public ElementByte maxOf(Element<?> a, Element<?> b) {
		byte av = ((ScalarElement)a).byteValue();
		byte bv = ((ScalarElement)b).byteValue();
		if (av > bv)
			value = (byte)av;
		else
			value = (byte)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}

	@Override
	public ElementByte clamp(long min, long max) {
		if (value < min)
			value = (byte)min;
		else if (value > max)
			value = (byte)max;
		return this;
	}
	@Override
	public ElementByte clamp(double min, double max) {
		if (value < min)
			value = (byte)min;
		else if (value > max)
			value = (byte)max;
		return this;
	}
	@Override
	public ElementByte clamp(Element<?> min, Element<?> max) {
		if (!min.isValid() || !max.isValid()) {
			this.setValid(false);
			return this;
		}
		// This may throw an exception - but that's OK, because VectorElements
		// are not comparable.
		ScalarElement minS = (ScalarElement)min;
		ScalarElement maxS = (ScalarElement)max;
		if (value < minS.byteValue())
			value = minS.byteValue();
		else if (value > maxS.byteValue())
			value = maxS.byteValue();
		return this;
	}

	@Override
	public ElementByte clampNew(long min, long max) {
		ElementByte res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementByte clampNew(double min, double max) {
		ElementByte res = copy();
		return res.clamp(min, max);
	}
	@Override
	public ElementByte clampNew(Element<?> min, Element<?> max) {
		ElementByte res = copy();
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
