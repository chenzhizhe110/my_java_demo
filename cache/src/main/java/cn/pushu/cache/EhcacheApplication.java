package cn.pushu.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> EhcacheApplication<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> TODO <br>
 * <b>date:</b> 2024/4/1815:01
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class EhcacheApplication {
    public static void main(String[] args) {
        {
            SpringApplication.run(EhcacheApplication.class, args);
        }
    }

}
