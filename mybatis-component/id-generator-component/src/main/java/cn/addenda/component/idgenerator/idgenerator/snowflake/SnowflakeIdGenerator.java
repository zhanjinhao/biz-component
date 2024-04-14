/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.addenda.component.idgenerator.idgenerator.snowflake;

import cn.addenda.component.idgenerator.idgenerator.IdGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Snowflake distributed primary key generator.
 *
 * <p>
 * Use snowflake algorithm. Length is 64 bit.
 * </p>
 *
 * <pre>
 * 1bit sign bit.
 * 41bits timestamp offset from 2016.11.01(ShardingSphere distributed primary key published data) to now.
 * 10bits worker process id.
 * 12bits auto increment offset in one mills
 * </pre>
 *
 * <p>
 * Call @{@code SnowflakeShardingKeyGenerator.setWorkerId} to set worker id, default value is 0.
 * </p>
 *
 * <p>
 * Call @{@code SnowflakeShardingKeyGenerator.setMaxTolerateTimeDifferenceMilliseconds} to set max tolerate time difference milliseconds, default value is 0.
 * </p>
 */
public final class SnowflakeIdGenerator implements IdGenerator {

  @Getter
  @Setter
  private Properties properties = new Properties();

  public static final long EPOCH;

  public static final long SEQUENCE_BITS = 12L;

  public static final long WORKER_ID_BITS = 10L;

  public static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1L;

  public static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

  public static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

  public static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

  static {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2016, Calendar.NOVEMBER, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    EPOCH = calendar.getTimeInMillis();
  }

  private final SnowflakeWorkerIdGenerator snowflakeWorkerIdGenerator;

  private final long workerId;

  public SnowflakeIdGenerator(SnowflakeWorkerIdGenerator snowflakeWorkerIdGenerator) {
    this.snowflakeWorkerIdGenerator = snowflakeWorkerIdGenerator;
    this.workerId = snowflakeWorkerIdGenerator.workerId();
    if (!(workerId >= 0L && workerId < WORKER_ID_MAX_VALUE)) {
      throw new IllegalArgumentException();
    }
  }

  private final Map<String, SingleTableSnowflakeIdGenerator> map = new ConcurrentHashMap<>();

  @Override
  public synchronized Long nextId(String scopeName) {
    return map.computeIfAbsent(scopeName, s -> new SingleTableSnowflakeIdGenerator()).nextId();
  }

  private class SingleTableSnowflakeIdGenerator {

    public static final int DEFAULT_VIBRATION_VALUE = 1;

    public static final int MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 10;

    private int sequenceOffset = -1;

    private long sequence;

    private long lastMilliseconds;

    public synchronized long nextId() {
      long currentMilliseconds = System.currentTimeMillis();
      if (waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
        currentMilliseconds = System.currentTimeMillis();
      }
      if (lastMilliseconds == currentMilliseconds) {
        if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
          currentMilliseconds = waitUntilNextTime(currentMilliseconds);
        }
      } else {
        vibrateSequenceOffset();
        sequence = sequenceOffset;
      }
      lastMilliseconds = currentMilliseconds;
      return ((currentMilliseconds - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    @SneakyThrows
    private boolean waitTolerateTimeDifferenceIfNeed(final long currentMilliseconds) {
      if (lastMilliseconds <= currentMilliseconds) {
        return false;
      }
      long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
      if (timeDifferenceMilliseconds >= getMaxTolerateTimeDifferenceMilliseconds()) {
        throw new IllegalArgumentException(String.format("Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastMilliseconds, currentMilliseconds));
      }
      return true;
    }

    private int getMaxVibrationOffset() {
      int result = Integer.parseInt(properties.getProperty("max.vibration.offset", String.valueOf(DEFAULT_VIBRATION_VALUE)));
      if (!(result >= 0 && result <= SEQUENCE_MASK)) {
        throw new IllegalArgumentException("Illegal max vibration offset");
      }
      return result;
    }

    private int getMaxTolerateTimeDifferenceMilliseconds() {
      return Integer.parseInt(properties.getProperty("max.tolerate.time.difference.milliseconds", String.valueOf(MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS)));
    }

    private long waitUntilNextTime(final long lastTime) {
      long result = System.currentTimeMillis();
      while (result <= lastTime) {
        result = System.currentTimeMillis();
      }
      return result;
    }

    private void vibrateSequenceOffset() {
      sequenceOffset = sequenceOffset >= getMaxVibrationOffset() ? 0 : sequenceOffset + 1;
    }

  }

}
