package org.seckill.exception;

/**
 * 秒杀关闭异常：库存已经消耗完毕或者秒杀活动已经结束
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }

}
