package org.vpac.ndg.cli;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/beans/CmdClientBean.xml"})
public class BasicTest extends ConsoleTest {

	@Test
	public void testUsage() {
		client.execute();
		String err = errput.toString();
		assertTrue(err.contains("Missing category"));
	}

	@Test
	public void testHelp() {
		client.execute("-h");
		String out = output.toString();
		assertTrue(out.contains("Show this help text."));
	}
}
