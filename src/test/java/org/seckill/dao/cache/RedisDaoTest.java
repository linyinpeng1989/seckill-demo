package org.seckill.dao.cache;

import org.junit.Test;
import org.seckill.BaseTest;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * Created by linyp on 2017/1/2.
 */
public class RedisDaoTest extends BaseTest {
    @Autowired
    private RedisDao redisDao;

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() throws Exception {
        long seckillId = 1000L;
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null ){
            seckill = seckillDao.queryById(seckillId);
            if(seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
            }
            seckill = redisDao.getSeckill(seckillId);
            System.out.println(seckill);
        }
    }

    /*@Test
    public void putSeckill() throws Exception {

    }*/

}