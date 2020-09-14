package quartz.main;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import quartz.job.QuartzHelloJob;

import java.util.Date;
import java.util.Properties;

/**
 * description:
 *
 * @author Michael
 * @date 2020/9/6
 * @time 5:34 下午
 */
public class QuartzHelloCronTriggerDemo {
    public static void main(String[] args) throws Exception {

        // 设置任务结束时间
        Date endDate = new Date();
        endDate.setTime(endDate.getTime() + 50000);

        // 通过代码方式配置 quartz
        Properties properties = new Properties();
        // 可以用工厂类中的常量
        properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
        // 注意点，这里值是字符串类型
        properties.put("org.quartz.threadPool.threadCount", "10");

        // 1. 调度器 Schduler，从工厂中获取调度器实例
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        stdSchedulerFactory.initialize(properties);
        Scheduler stdScheduler = stdSchedulerFactory.getScheduler();


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
        // 从 0 秒开始，每 5 秒执行
        //
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "triggerGroup1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .usingJobData("message", "simple 触发器")
                .startNow()
                .endAt(endDate)
                .build();

        // 4. 通过调度器将触发器与任务实例关联，保证按照触发器定义的条件执行任务
        stdScheduler.scheduleJob(jobDetail, trigger);

        // 5. 启动
        stdScheduler.start();

        Thread.sleep(2000L);
//        // scheduler 挂起
//        stdScheduler.standby();
//
//        Thread.sleep(2000L);
//
//        // 暂停后重启
//        stdScheduler.start();

        // 关闭
        stdScheduler.shutdown(false);
    }
}
