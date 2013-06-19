package org.vpac.ndg.query.math;

import ucar.ma2.DataType;

/**
 * Data types that can be used in the query engine for arithemetic and storage.
 * These closely follow those defined by {@link DataType}, however this
 * enumeration encapsulates the behaviour of signed/unsigned types: unsigned
 * types will be promoted to the next-larger data type to prevent overflows. The
 * original DataType is also retained, as this is needed for the creation of
 * storage arrays.
 * @author Alex Fraser
 */
public enum Type {

	BYTE("byte", DataType.BYTE, new ElementByte(), false),
	UBYTE("ubyte", DataType.BYTE, new ElementShort(), true),
	// Could support char as short?
	SHORT("short", DataType.SHORT, new ElementShort(), false),
	USHORT("ushort", DataType.SHORT, new ElementInt(), true),
	INT("int", DataType.INT, new ElementInt(), false),
	UINT("uint", DataType.INT, new ElementLong(), true),
	LONG("long", DataType.LONG, new ElementLong(), false),
	// No unsigned long. How would you store that?
	FLOAT("float", DataType.FLOAT, new ElementFloat(), false),
	DOUBLE("double", DataType.DOUBLE, new ElementDouble(), false);

	private String name;
	private DataType storageType;
	private ScalarElement prototype;
	private boolean unsigned;

	Type(String name, DataType storageType, ScalarElement prototype,
			boolean unsigned) {
		this.name = name;
		this.storageType = storageType;
		this.prototype = prototype;
		this.unsigned = unsigned;
	}

	/**
	 * Convert a value to this data type.
	 * 
	 * @param value The value to convert.
	 * @return A new element of this type, with the given value. If this type is
	 *         too small to store the value, it will be truncated.
	 */
	public Element<?> convert(Element<?> value) {
		if (ScalarElement.class.isAssignableFrom(value.getClass()))
			return convert((ScalarElement) value);
		else
			return convert((VectorElement) value);
	}

	/**
	 * Convert a value to this data type.
	 * 
	 * @param value The value to convert.
	 * @return A new vector element of this type, with the given value. If this
	 *         type is too small to store the value, it will be truncated.
	 */
	public VectorElement convert(VectorElement value) {
		VectorElement newElem = VectorElement.create(value.size(), this);
		newElem.set(value);
		return newElem;
	}

	/**
	 * Convert a value to this data type.
	 * 
	 * @param value The value to convert.
	 * @return A new scalar element of this type, with the given value. If this
	 *         type is too small to store the value, it will be truncated.
	 */
	public ScalarElement convert(ScalarElement value) {
		ScalarElement newElem = prototype.copy();
		newElem.set(value);
		return newElem;
	}

	public String getName() {
		return name;
	}

	public DataType getStorageType() {
		return storageType;
	}

	public ScalarElement getElement() {
		return prototype;
	}

	public boolean isUnsigned() {
		return unsigned;
	}

	public static Type get(String name) {
		return valueOf(name.toUpperCase());
	}

	public static Type get(DataType dt, boolean unsigned) {
		for (Type t : values()) {
			if (t.isUnsigned() != unsigned)
				continue;
			if (t.getStorageType() != dt)
				continue;
			return t;
		}
		throw new IllegalArgumentException(String.format(
				"Data type %s is not supported.", dt));
	}

	@Override
	public String toString() {
		return name;
	}

}