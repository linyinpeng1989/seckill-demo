package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

/**
 * Created by linyp on 2016/12/27.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("list", seckillService.getSeckillList());
        return "list";
    }

    @GetMapping("/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        // 如果秒杀商品ID不存在，则重定向到列表也
        if (seckillId == null) {
            return "redirect:/seckill/list";
            //return "forward:/seckill/list";   //请求转发方式
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "redirect:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 获取秒杀地址
     * @param seckillId
     * @return
     */
    @PostMapping(value = "/{seckillId}/exposer",
            // produces：告诉浏览器数据的content-type，并设置编码
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 秒杀执行方法.
     * @param seckillId 秒杀商品ID
     * @param userPhone 秒杀用户手机
     * @param md5 秒杀Key
     * @return
     */
    @PostMapping(value = "{seckillId}/{md5}/execution", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @CookieValue(value = "userPhone", required = false) Long userPhone,
                                                   @PathVariable("md5") String md5) {
        if (userPhone == null) {
            return new SeckillResult<>(false, "未注册");
        }

        SeckillResult<SeckillExecution> result;
        SeckillExecution seckillExecution;
        try {
            // dao操作
            //seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
            // procedure 操作
            seckillExecution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            result = new SeckillResult<>(true, seckillExecution);
        } catch (SeckillCloseException e) {
            seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            result = new SeckillResult<>(true, seckillExecution);
        } catch (RepeatKillException e) {
            seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            result = new SeckillResult<>(true, seckillExecution);
        } catch (SeckillException e) {
            logger.error(e.getMessage(), e);
            seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            result = new SeckillResult<>(true, seckillExecution);
        }
        return result;
    }

    @GetMapping(value = "/time/now", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> execute(Model model) {
        return new SeckillResult<Long>(true, Calendar.getInstance().getTimeInMillis());
    }

}
