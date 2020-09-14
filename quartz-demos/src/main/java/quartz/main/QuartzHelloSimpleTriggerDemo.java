package quartz.main;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import quartz.job.QuartzHelloJob;

import java.util.Date;

/**
 * description:
 *
 * @author Michael
 * @date 2020/9/6
 * @time 5:34 下午
 */
public class QuartzHelloSimpleTriggerDemo {
    public static void main(String[] args) throws Exception {

        // 设置任务结束时间
        Date endDate = new Date();
        endDate.setTime(endDate.getTime() + 20000);


        // 1. 调度器 Schduler，从工厂中获取调度器实例
        Scheduler stdScheduler = StdSchedulerFactory.getDefaultScheduler();

        // 2. 任务实例 JobDetail
        // 参数 1：任务的名称；参数 2：任务组的名称
        JobDetail jobDetail = JobBuilder.newJob(QuartzHelloJob.class)
                .withIdentity("job1", "jobGroup1")
                .usingJobData("message", "job message")
                .usingJobData("param", "setter 传参")
                .usingJobData("count", 1)
                .build();

        // 放到 Job 类中去输出
        // System.out.println("Job Name:" + jobDetail.getKey().getName());
        // System.out.println("Job Group:" + jobDetail.getKey().getGroup());
        // System.out.println("Job Class:" + jobDetail.getJobClass());

        // 3. 触发器 Trigger
        // 参数 1：触发器的名称；参数 2：触发器组的名称
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "triggerGroup1")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .repeatSecondlyForever(5)
                        .withRepeatCount(2))
                .usingJobData("message", "simple 触发器")
                .startNow()
                .endAt(endDate)
                .build();

        // 4. 通过调度器将触发器与任务实例关联，保证按照触发器定义的条件执行任务
        stdScheduler.scheduleJob(jobDetail, trigger);

        // 5. 启动
        stdScheduler.start();
    }
}
