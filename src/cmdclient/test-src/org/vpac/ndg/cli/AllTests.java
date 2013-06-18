package org.vpac.ndg.cli;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration({"file:resources/spring/beans/CmdClientBean.xml"})
@RunWith(Suite.class)
@SuiteClasses({
	BasicTest.class,
	InvalidInputTests.class,
	CleanUpTest.class,
	QueryTest.class
})
public class AllTests {
	// Intentionally left blank.
}
