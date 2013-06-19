package org.vpac.ndg.query;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.vpac.ndg.query.math.ElementTest;
import org.vpac.ndg.query.math.SwizzleTest;

@RunWith(Suite.class)
@SuiteClasses({
	QueryDefinitionTest.class,
	QueryTest.class,
	ElementTest.class,
	SwizzleTest.class
})
public class AllTests {
	// Empty; tests are defined above.
}
