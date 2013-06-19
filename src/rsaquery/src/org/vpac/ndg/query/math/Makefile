SCALAR_CLASSES := ElementByte.java ElementShort.java ElementInt.java ElementLong.java ElementFloat.java ElementDouble.java
SPECIAL_VECTORS := VectorInt.java VectorReal.java
CORE_GEN_FILES := Element.java ScalarElement.java
GENERATED_FILES := $(CORE_GEN_FILES) VectorElement.java $(SCALAR_CLASSES) $(SPECIAL_VECTORS) Swizzle.java

all: $(GENERATED_FILES)

$(CORE_GEN_FILES): Element_types.py Element_gen.py
	python2 Element_gen.py

$(SCALAR_CLASSES): Element_types.py ElementX_gen.py
	python2 ElementX_gen.py

VectorElement.java: Element_types.py VectorElement_gen.py
	python2 VectorElement_gen.py

$(SPECIAL_VECTORS): VectorX_gen.py
	python2 VectorX_gen.py

Swizzle.java: Swizzle_gen.py Element_types.py
	python2 Swizzle_gen.py

clean:
	rm -f $(GENERATED_FILES)
