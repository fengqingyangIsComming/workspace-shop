package com.taotao.order.job.test;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JobTest {
	/**
	 * spring整合Quartz
	 * 
	 * @throws IOException
	 */
	@Test
	public void testJob() throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-job.xml");
		// System.in.read();
	}

	/**
	 * 独立使用quartz
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQuartzAlone() throws Exception {

		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();

		JobDetail jobDetail = new JobDetailImpl("helloQuartzJob", Scheduler.DEFAULT_GROUP, QuartzAlone.class);

		SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl("simpleTrigger", Scheduler.DEFAULT_GROUP);

		simpleTrigger.setStartTime(new Date(System.currentTimeMillis()));
		simpleTrigger.setRepeatInterval(1000);
		simpleTrigger.setRepeatCount(10);
		scheduler.scheduleJob(jobDetail, simpleTrigger);
		scheduler.start();
		System.in.read();
	}

}
