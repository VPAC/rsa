package org.vpac.ndg.application;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.storage.dao.JobProgressDao;
import org.vpac.ndg.storage.model.JobProgress;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class JobProgressTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	JobProgressDao jobProgressDao;

	@Before
	public void setUp() {
	}

	@Test
	public void testSearchTasks() {
		String taskType = "Import";
		String taskState = "FINISHED";
		List<JobProgress> list = jobProgressDao.search(
				TaskType.valueOf(taskType), TaskState.valueOf(taskState), 0,
				1000);
		assertNotSame(0, list.size());
	}

	@Test
	public void testSearchTasksNoReturn() {
		String id = "";
		JobProgress jp = jobProgressDao.retrieve(id);
		assertNull(jp);
	}
}
