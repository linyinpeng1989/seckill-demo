-- 存储过程
DELIMITER $$ -- console ; 转换为$$

-- 定义存储过程
-- 参数：in - 输入参数
--      out - 输出参数
-- ROW_COUNT() 返回上一条修改类型的SQL(INSERT、UPDATE、DELETE)的影响行数
-- ROW_COUNT(): 0 - 未修改数据； >0 - 表示修改的行数； <0 - 表示SQL错误或者未执行SQL
CREATE PROCEDURE seckill.execute_seckill(IN v_seckill_id BIGINT, IN v_phone BIGINT,
                IN v_kill_time TIMESTAMP, OUT r_result INT )
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    -- 开启事务
    START TRANSACTION;
    INSERT ignore INTO success_killed (seckill_id, user_phone, state, create_time)
    VALUES (v_seckill_id, v_phone, 0, v_kill_time);

    -- 根据是否插入成功，执行下面的逻辑
    SELECT ROW_COUNT() INTO insert_count;
    IF(insert_count = 0) THEN
      ROLLBACK;
      SET  r_result = -1; -- 根据业务逻辑定义输出参数
    ELSEIF(insert_count < 0) THEN
      ROLLBACK;
      SET  r_result = -2;
    -- 插入成功后，执行减库存的操作
    ELSE
      UPDATE seckill SET number = number - 1 WHERE seckill_id = v_seckill_id AND end_time > v_kill_time
      AND start_time < v_kill_time AND number > 0;

      -- 根据减库存操作是否成功，执行下面的逻辑
      SELECT ROW_COUNT() INTO insert_count;
      IF (insert_count = 0) THEN
        ROLLBACK;
        SET r_result = 0;
      ELSEIF (insert_count < 0) THEN
        ROLLBACK;
        SET r_result = -2;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
  END;
$$
-- $$代表存储过程定义结束
DELIMITER ; -- console $$ 转换为;



-- 在MySQL的console窗口中执行存储过程
--SET @r_result = -3; -- 定义输出参数
--CALL execute_seckill(1003, 15868457814, now(), @r_result);
--SELECT @r_result; -- 获取结果