package org.vpac.ndg.query.sampling;


public interface HasPrototype {
	/**
	 * @return A prototypical element for this object. This can be used by
	 * algorithms that don't need to know what type of data they are dealing
	 * with.
	 */
	Prototype getPrototype();
}
