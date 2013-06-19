package org.vpac.ndg.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;

/**
 * Test case to test if CmdClient is working properly.
 * @author hsumanto
 * @author Alex Fraser
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/beans/CmdClientBean.xml"})
public class CleanUpTest extends ConsoleTest {

	@Test
	public void testImportTimeSlice() throws TaskInitialisationException, TaskException {
		client.execute("data", "cleanup");
		assertEquals("Failed to perform clean-up.", 0, errcode);
	}

}
