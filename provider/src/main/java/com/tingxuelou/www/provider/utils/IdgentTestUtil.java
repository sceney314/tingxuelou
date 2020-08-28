package com.tingxuelou.www.provider.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Date: 2020-07-31 19:58
 * Copyright (C), 2015-2020
 */
public class IdgentTestUtil {
    private static final long autoBits = (1 << 27) - 1;

    private static final long workerIdShift = 27;

    private static final long wdBits = (1 << 4) - 1;

    private static final long timeShift = 31;

    private static final long timebits =  (1L << 32) - 1;

    private static volatile AtomicLong auto = new AtomicLong(0L);


    public static long nextId(){
        long id = 1 & wdBits;

        long unix = System.currentTimeMillis();
        long tbit = (unix /1000) & timebits;

        long offset = auto.addAndGet(1) & autoBits;

        return  (tbit << timeShift) | (id << workerIdShift) | (offset);
    }
}
