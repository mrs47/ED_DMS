package cn.mrs47.util;

import java.util.Random;
import java.util.UUID;

/**
 * @author mrs47
 */
public class IdUtil {
    private static final String PRODUCT_KEY_POOL[] = {"0", "1", "2", "3", "4", "5","6", "7", "8", "9"
            , "a", "b", "c", "d", "e", "f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private static final String DEVICE_KEY_POOL[]={"0", "1", "2", "3", "4", "5","6", "7", "8", "9"
            , "a", "b", "c", "d", "e", "f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
            ,"!","@","#","$","%","^","&","*","/","-","+"};
    public static String  getUserId(){
        return makeUserId();
    }

    public static String  getOrderId(){
        return makeOrderId();
    }

    public static String getProductId(){
        return makeProductId();
    }

    public static String getProductKey(){
        return makeProductKey();
    }

    public static String getDeviceKey(){
        return makeDeviceKey();
    }

    public static String getDeviceId(){
        return makeDeviceId();
    }

    public static String getFileId(){
        return makeFileId();
    }

    private static String makeUserId(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return  uuid;
    }


    private static String makeOrderId(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return  uuid;
    }
    public static String getIdCode(){
        return makeIdCode();
    }

    private static String makeProductId(){
        return makeUserId();
    }

    private static String makeFileId(){
        return makeUserId();
    }

    private static String makeProductKey(){
        Random random=new Random(System.currentTimeMillis());
        StringBuffer code=new StringBuffer();
        for (int i=0;i<16;i++){
            int c = (int)(random.nextDouble() * 36);
            code.append(PRODUCT_KEY_POOL[c]);
        }
        return  code.toString();
    }
    private static String makeDeviceKey(){
        Random random=new Random(System.currentTimeMillis());
        StringBuffer code=new StringBuffer();
        for (int i=0;i<16;i++){
            int c = (int)(random.nextDouble() * 47);
            code.append(DEVICE_KEY_POOL[c]);
        }
        return  code.toString();
    }
    private static String makeIdCode(){
        Integer code = Math.abs(UUID.randomUUID().toString().hashCode());
        String idCode = code.toString().substring(0,6);
        return idCode;
    }
    private  static String makeDeviceId(){
        return makeUserId();
    }

}
