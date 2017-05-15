package org.seckill.dao;

import org.junit.Test;
import org.seckill.BaseTest;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by linyp on 2016/12/25.
 */
public class SuccessKilledDaoTest extends BaseTest {
    @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long seckillId = 1000L;
        long phone = 15811112222L;
        int insertCount = successKilledDao.insertSuccessKilled(seckillId, phone);
        System.out.println("insertCount: " + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long seckillId = 1000L;
        long phone = 15811112222L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}