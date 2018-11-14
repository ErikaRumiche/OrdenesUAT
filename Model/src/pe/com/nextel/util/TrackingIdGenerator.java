package pe.com.nextel.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by JCURI on 14/07/2017.
 */
public class TrackingIdGenerator {
	private final long sequenceBits = 12;
	private final long datacenterIdBits = 10L;
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private final long datacenterIdShift = sequenceBits;
	private final long timestampLeftShift = sequenceBits + datacenterIdBits;
	private final long twepoch = 1288834974657L;
	private final long datacenterId;
	private final long sequenceMax = 4096;
	private volatile long lastTimestamp = -1L;
	private volatile long sequence = 0L;

	private static TrackingIdGenerator basicEntityIdGenerator;

	public static TrackingIdGenerator getInstance() throws Exception {
		if (basicEntityIdGenerator == null)
			basicEntityIdGenerator = new TrackingIdGenerator();
		return basicEntityIdGenerator;
	}

	private TrackingIdGenerator() throws Exception {
		datacenterId = getDatacenterId();
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new Exception("datacenterId > maxDatacenterId");
		}
	}

	public synchronized String generateLongId() throws Exception {
		long timestamp = System.currentTimeMillis();
		if (timestamp < lastTimestamp) {
			throw new Exception("Hora atrasada.  Error generando id para "
					+ (lastTimestamp - timestamp) + " milliseconds.");
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) % sequenceMax;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		Long id = ((timestamp - twepoch) << timestampLeftShift)
				| (datacenterId << datacenterIdShift) | sequence;
		return id.toString();
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	protected long getDatacenterId() throws Exception {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			long id;
			if (network == null) {
				id = 1;
			} else {
				byte[] mac = network.getHardwareAddress();
				id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
			}
			return id;
		} catch (SocketException e) {
			throw new Exception(e);
		} catch (UnknownHostException e) {
			throw new Exception(e);
		}
	}
}