package cn.mrs47.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author mrs47
 * @param <T>
 */


//属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseData<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ResponseData(int status){
        this.status=status;
    }
    private ResponseData(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ResponseData(int status,T data){
        this.status=status;
        this.data=data;
    }
    private ResponseData(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }
    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS;
    }
    public static <T> ResponseData<T> createBySuccess(){
        return new ResponseData<T>(ResponseCode.SUCCESS);
    }
    public static <T> ResponseData<T> createBySuccessMessage(String msg){
        return new ResponseData<T>(ResponseCode.SUCCESS,msg);
    }
    public static <T> ResponseData<T> createBySuccess(T data){
        return new ResponseData<T>(ResponseCode.SUCCESS,data);
    }
    public static <T> ResponseData<T> createBySuccess(String msg,T data){
        return new ResponseData<T>(ResponseCode.SUCCESS,msg,data);
    }
    public static <T> ResponseData<T> createByError(){
        return new ResponseData<T>(ResponseCode.ERROR);
    }
    public static <T> ResponseData<T> createByErrorMessage(String msg){
        return new ResponseData<T>(ResponseCode.ERROR,msg);
    }
    public static <T> ResponseData<T> createByError(T data){
        return new ResponseData<T>(ResponseCode.ERROR,data);
    }
    public static <T> ResponseData<T> createByError(int status,String msg,T data){
        return new ResponseData<T>(status,msg,data);
    }




}
