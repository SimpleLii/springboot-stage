package com.simplelii.app.dao.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;

/**
 * @author SimpleLii
 * @description id 生成器
 * @date 17:45 2020/6/18
 */
public class IdUtil {

    private static final IdUtil INSTANCE = new IdUtil();

    private static final Logger logger = LoggerFactory.getLogger(IdUtil.class);

    private static final long epoch = 1504842098117L;
    private static final long workerIdBits = 10L;
    private static final long tenantIdBits = 3L;
    private static final long maxWorkerId = 1023L;
    private static final long maxTenantId = 7L;
    private static long sequence = 0L;
    private static final long sequenceBits = 10L;
    private static final Random random = new Random();
    private static final long timestampBits = 40L;
    private static final long workerIdShift = 10L;
    private static final long timestampShift = 20L;
    private static final long tenantIdShift = 60L;
    private static final long sequenceMask = 1023L;
    private static long lastTimestamp = -1L;

    public static IdUtil getInstance() {
        return INSTANCE;
    }

    public static synchronized long nextId(long workerId, long tenantCode) throws RuntimeException {
        if ((workerId > maxWorkerId) || (workerId < 0L)) {
            throw new RuntimeException(String.format("worker Id can't be greater than %d or less than 0", sequenceMask));
        }
        if ((tenantCode > maxTenantId) || (tenantCode < 0L)) {
            throw new RuntimeException(String.format("tenant Id can't be greater than %d or less than 0", maxTenantId));
        }
        long timestamp = System.currentTimeMillis();
        if (lastTimestamp == timestamp) {
            sequence = sequence + 1L & 0x3FF;
            if (sequence == 0L) {
                timestamp = nextMillis(lastTimestamp);
            }
        } else {
            sequence = random.nextInt(128);
        }
        if (timestamp < lastTimestamp) {
            logger.error(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));

            throw new RuntimeException(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        lastTimestamp = timestamp;
        return tenantCode << tenantIdShift | timestamp - epoch << timestampShift | workerId << workerIdShift | sequence;
    }

    private static long nextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static Date getDate(long id) {
        long headTenant = 576460752303423487L;
        long idWithTenant = id & headTenant;
        long v1 = idWithTenant >>> 20;

        return new Date(epoch + v1);
    }

    public static long getMachineId(long id) {
        long head = 524287L;
        long id2 = id & head;
        long v1 = id2 >>> 10;

        return v1;
    }

}
