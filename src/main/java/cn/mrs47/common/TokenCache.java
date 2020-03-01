package cn.mrs47.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author mrs47
 */

public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    private static LoadingCache<String,String> loadingCache=CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        loadingCache.put(key,value);
    }
    public static String getKey(String key){
        String value=null;
        try {
            value=loadingCache.get(key);
            if (value=="null"){
                return null;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return value;
    }
}
