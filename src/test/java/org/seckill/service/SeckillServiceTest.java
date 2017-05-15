package org.seckill.service;

import org.junit.Test;
import org.seckill.BaseTest;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by linyp on 2016/12/25.
 */
public class SeckillServiceTest extends BaseTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        System.out.println(seckillService.getSeckillList());
    }

    @Test
    public void getById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        System.out.println(seckill);
    }

    /**
     * 集成测试：秒杀完整流程，可重复执行
     */
    @Test
    public void testSeckillLogic() {

        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
        if (exposer.isExposed()) {

            long phone = 15821739225L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}",seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }

        } else {
            logger.warn("秒杀未开始：{}",exposer.toString());
        }

    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer.toString());
        //Exposer{exposed=true, md5='be3d9cdd642d64f8ed97eb05e93b9628', seckillId=1000, now=0, start=0, end=0}
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000;
        long phone = 15821739223L;

        String md5 = "be3d9cdd642d64f8ed97eb05e93b9628";

        SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);

        logger.info("seckillExecution={}",seckillExecution);
    }

}