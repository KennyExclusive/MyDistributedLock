
package com.kenny.demo.distrilock.data;


/**
 * 返回结果信息码类
 * @author kenny
 * @date 2019/12/04
 */
public enum MyResultCode {
    
    Success(201,"成功"),
    SuccessStock(202,"减库存成功"),
    
    PARAM_IS_BLANK(102,"参数为空"),
    PROCESS_FAULT(103,"下单失败，请稍后重试"),
    PROCESS_EMPTY(104,"下单失败，商品已卖光"),
    
    PROCESS_ERROR(501,"系统出现异常");
    
    private Integer code;
    
    private String message;

    private MyResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
