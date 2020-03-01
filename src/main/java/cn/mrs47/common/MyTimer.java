package cn.mrs47.common;

import cn.mrs47.dao.DeviceMapper;
import cn.mrs47.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author mrs47
 */
@Service
public class MyTimer {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    @Autowired
    private DeviceMapper deviceMapper;

    @Scheduled(cron = "0 0/6 * * * ?")
    public void taskCheckDeviceAlive(){
        logger.info("执行定时任务，检查设备存活");
        Jedis jedis = RedisUtil.getJedis();
        Set<String> set=jedis.keys("gpio_*");
        jedis.close();
        List<String> list =new ArrayList<>();
        if (set!=null && !set.isEmpty()){
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                if (next!=null){
                    deviceMapper.setAlive(next.replaceAll("gpio_",""));
                    list.add(next.replaceAll("gpio_",""));
                }
            }
        }
        //避免空集
        list.add("1");
        deviceMapper.setDied(list);
    }
}
