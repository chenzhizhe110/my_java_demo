package cn.pushu.cache.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> MonitorSystemScheduled<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> TODO <br>
 * <b>date:</b> 2024/4/2210:44
 */
@Service
public class MonitorSystemScheduled {

    private final Logger log= LoggerFactory.getLogger(MonitorSystemScheduled.class);

  //  @Scheduled(initialDelay =3*1000,fixedDelay =5*1000 )
    public void memory(){
        Runtime run = Runtime.getRuntime();
        long max = run.maxMemory()/1024/1024;
        long total = run.totalMemory()/1024/1024;
        long free = run.freeMemory()/1024/1024;
        long usable = max - total + free;

        log.info("最大内存 = " + max+";已分配内存 = " + total+";剩余内存 = " + free+";最大可用内存 = " + usable);
    }


}

