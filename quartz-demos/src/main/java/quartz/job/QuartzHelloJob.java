package quartz.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Trigger;

import java.net.ContentHandler;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description:
 *
 * @author Michael
 * @date 2020/9/6
 * @time 5:28 下午
 */
@PersistJobDataAfterExecution
public class QuartzHelloJob implements Job {

    private String param;

    private Integer count;

    public void setParam(String param) {
        this.param = param;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println("==== QuartzHelloJob Start ====");

        // 当前时间
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date);
        // 工作内容
        System.out.println("正在进行数据备份任务的定时执行，执行时间：" + dateStr);

        // 通过 JobExecutionContext 对象获取 JobDetail 内容
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        System.out.println("Job Name: " + jobDetail.getKey().getName());
        System.out.println("Job Group: " + jobDetail.getKey().getGroup());
        System.out.println("Job Class: " + jobDetail.getJobClass());
        // JobDataMap 传值
        JobDataMap jobDataMap1 = jobExecutionContext.getJobDetail().getJobDataMap();
        System.out.println("任务数据的参数值：" + jobDataMap1.get("message"));
        System.out.println("任务数据通过 setter 方式传参：" + param);

        // 通过 JobExecutionContext 对象获取 Trigger 内容
        Trigger trigger = jobExecutionContext.getTrigger();
        System.out.println("Trigger Name: " + trigger.getKey().getName());
        System.out.println("Trigger Group: " + trigger.getKey().getGroup());
        System.out.println("Trigger Class: " + trigger.getClass());
        // jobKey
        System.out.println("Trigger jobKey Name: " + trigger.getJobKey().getName());
        System.out.println("Trigger jobKey Group: " + trigger.getJobKey().getGroup());
        System.out.println("Trigger startTime: "+ dateFormat.format(trigger.getStartTime()));
        System.out.println("Trigger endTime: "+ dateFormat.format(trigger.getEndTime()));

        // JobDataMap 传值
        JobDataMap jobDataMap2 = jobExecutionContext.getTrigger().getJobDataMap();
        System.out.println("触发器数据的参数值：" + jobDataMap2.getString("message"));


        // 获取其他内容
        System.out.println("当前任务执行时间：" + dateFormat.format(jobExecutionContext.getFireTime()));
        System.out.println("下次任务执行执行时间：" + dateFormat.format(jobExecutionContext.getNextFireTime()));


        // 输出 count，验证有状态 job 与无状态 job 的区别
        ++count;
        System.out.println("Job 状态 count 数量：" + jobDataMap1.get("count"));
        // 将 count 放到 JobDataMap 中
        jobDataMap1.put("count", count);

        // 加延时主要是想看定时任务是创建出了多个实例，每个实例在独自运行着任务
        // 验证 shutdown 方法
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==== 开始时间：" + dateStr + " 任务 end ====");
    }
}
