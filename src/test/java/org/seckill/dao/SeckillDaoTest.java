package org.seckill.dao;

import org.junit.Test;
import org.seckill.BaseTest;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by linyp on 2016/12/25.
 */
public class SeckillDaoTest extends BaseTest {
    // 注入Dao实现类依赖
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        //Java没有保存形参的记录:QueryAll(int offset,int limit) -> QueryAll(arg0,arg1);
        //因为java形参的问题,多个基本类型参数的时候需要用@Param("seckillId")注解区分开来
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount:  " + updateCount);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.toString());
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 10);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
    }

}