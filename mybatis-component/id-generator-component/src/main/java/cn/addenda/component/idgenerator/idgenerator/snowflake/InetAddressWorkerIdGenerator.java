package cn.addenda.component.idgenerator.idgenerator.snowflake;

import cn.addenda.component.jdk.util.my.MyStringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author addenda
 * @since 2023/6/4 17:41
 */
@Slf4j
public class InetAddressWorkerIdGenerator implements SnowflakeWorkerIdGenerator {

  private final long maxWorkerId;
  private final long maxDatacenterId;

  private InetAddress inetAddress;

  public InetAddressWorkerIdGenerator(InetAddress inetAddress) {
    this.inetAddress = inetAddress;
    this.maxDatacenterId = (1 << SnowflakeIdGenerator.WORKER_ID_BITS / 2) - 1L;
    this.maxWorkerId = (1 << SnowflakeIdGenerator.WORKER_ID_BITS / 2) - 1L;
  }

  public InetAddressWorkerIdGenerator() {
    this(null);
  }

  @Override
  public long workerId() {
    return getMaxWorkerId(getDatacenterId(maxDatacenterId), maxWorkerId);
  }

  /**
   * copy from mybatis-plus project.
   */
  protected long getDatacenterId(long maxDatacenterId) {
    long id = 0L;
    try {
      if (null == this.inetAddress) {
        this.inetAddress = InetAddress.getLocalHost();
      }
      NetworkInterface network = NetworkInterface.getByInetAddress(this.inetAddress);
      if (null == network) {
        id = 1L;
      } else {
        byte[] mac = network.getHardwareAddress();
        if (null != mac) {
          id = ((0x000000FF & (long) mac[mac.length - 2]) | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
          id = id % (maxDatacenterId + 1);
        }
      }
    } catch (Exception e) {
      log.warn(" getDatacenterId: " + e.getMessage());
    }
    return id;
  }

  /**
   * 获取 maxWorkerId。<p/>
   * copy from mybatis-plus project.
   */
  protected long getMaxWorkerId(long datacenterId, long maxWorkerId) {
    StringBuilder mpid = new StringBuilder();
    mpid.append(datacenterId);
    String name = ManagementFactory.getRuntimeMXBean().getName();
    if (MyStringUtils.hasText(name)) {
      /*
       * GET jvmPid
       */
      mpid.append(name.split("@")[0]);
    }
    /*
     * MAC + PID 的 hashcode 获取16个低位
     */
    return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
  }

}
