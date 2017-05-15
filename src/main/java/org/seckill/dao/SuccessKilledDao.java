package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by linyp on 2016/12/25.
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复（联合唯一主键）
     * @param seckillId
     * @param userPhone
     * @return 表示插入的行数，0表示插入失败
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据ID查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
