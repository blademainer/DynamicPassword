import java.util.Date;
import java.util.Random;

import com.xiongyingqi.util.Base64;
import com.xiongyingqi.util.ByteHelper;
import com.xiongyingqi.util.DateHelper;

public class DynamicPassword {
    public static final int KEY_LENGTH = 16;
    private static final int SECOND = 1000;
    private static final int DEFAULT_REFRESH_TIME = 60;

    public static byte[] generateKey(int length) {
	if (length <= 0) {
	    throw new IllegalArgumentException("长度为：" + length + "， 指定的密钥长度错误！");
	}
	byte[] bts = new byte[length];

	Random random = new Random();
	for (int i = 0; i < length; i++) {
	    bts[i] = (byte) random.nextInt(255);
	}
	return bts;
    }

    public static byte[] getTimeBytes() {
	return getTimeBytes(DEFAULT_REFRESH_TIME);
    }

    public static byte[] getTimeBytes(int seconds) {
	if (seconds <= 0) {
	    throw new IllegalArgumentException("秒数为：" + seconds
		    + "， 指定的密钥更新周期错误！");
	}
	long currentTimeMillis = System.currentTimeMillis();
	// System.out.println(currentTimeMillis);
	currentTimeMillis /= seconds * SECOND;// 对秒数取整

	// long currentTimeMillisRs = currentTimeMillis * seconds *
	// SECOND;//还原时间位数
	// System.out.println(currentTimeMillisRs);
	// Date date = new Date(currentTimeMillisRs);
	// String dateStr = DateHelper.FORMATTER_LONG.format(date);
	// System.out.println(dateStr);

	return ByteHelper.longToBytes(currentTimeMillis);
    }

    public static byte[] generateCode(byte[] key) {
	byte[] bs = getTimeBytes();
	int length = bs.length;
	for (int i = 0; i < length; i++) {
	    byte b = bs[i];
	    for (int j = 0; j < length; j++) {
		bs[i] = (byte) (bs[j] | b);
		bs[i] = (byte) (bs[j] ^ b);
	    }
	}

	int keyLength = key.length;
	byte[] rs = new byte[keyLength];
	System.arraycopy(key, 0, rs, 0, keyLength);
	for (int i = 0; i < keyLength; i++) {
	    byte k = rs[i];
	    for (int j = 0; j < length; j++) {
		rs[i] = (byte) (bs[j] ^ k);
		rs[i] = (byte) (bs[j] | k);
	    }
	}
	// String string = Base64.encodeBytes(rs);
	// System.out.println(string);
	return rs;
    }
    
    public static long bytesToLong(byte[] bts){
	int inputLength = bts.length;
	long data = 0;
	long temp = 0;
	for (int i = 0; i < inputLength; i++) {
	    data <<= 8;
	    byte b = bts[i];
	    temp = b & 0xFF;
	    data |= temp;
	}
	return data;
    }
    
    public static byte[] compressBytes(byte[] bts, int length){
	int inputLength = bts.length;
	if(inputLength < length){
	    throw new ArrayIndexOutOfBoundsException("无法压缩：目标参数length小于bts的长度！");
	}
	
	float multiple = inputLength / length;// 倍数
	int leastLength = inputLength % length;// 剩余字节数
	
	int index = 0;
	
	int i = 0;
	byte[] rs = new byte[length];
	while(i < length){
	    byte bt = 0;
	    for (int j = 0; j < multiple; j++) {
	       bt += bts[index++];
	    }
	    rs[i++] = bt;
	}
	
	for (int j = 0; j < length; j++) {
	    for (int k = 0; k < leastLength; k++) {
		rs[j] ^= bts[index + k];
	    }
	}
	System.out.println(rs.length);
	
	System.out.println("index ========== " + index);
	System.out.println("leastLength ========== " + leastLength);
	System.out.println("multiple ========== " + multiple);
	System.out.println("inputLength ========== " + inputLength);
	System.out.println("length ========== " + length);
	
	return rs;
    }
    
    public static void printBytes(byte[] bts){
	for (int i = 0; i < bts.length; i++) {
	    byte b = bts[i];
	    System.out.println(b);
	}
    }

    public static void main(String[] args) {
//	double a = (1 << 16) / (Math.pow(10, 6) - 1);
//
	byte[] key = generateKey(KEY_LENGTH);
	byte[] rs = generateCode(key);
	int minApproach = minApproach(6);
	int i = minApproach / 8;
	if(minApproach % 8 > 0){
	    i++;
	}
	byte[] data = compressBytes(rs, i);
	printBytes(data);
	String string = Base64.encodeBytes(data);
	System.out.println(string);
	long code = bytesToLong(data);
	long l = (long) (code & 999999);
	System.out.println(l);
//	long s = bytesToLong(new byte[]{ (byte) 255, (byte) 255 });
//	System.out.println(s);
    }

    /**
     * 获取最接近digit位数字的二进制表示的数（一定大于digit位的数字）的大小
     * 例如，如果digit为6，那么表示999999最接近的二进制数字为1048576(2的20次方)，结果返回20
     * <br>2014-1-21 下午5:27:53
     * @param digit
     * @return
     */
    public static int minApproach(int digit) {
	int max = (int) (Math.pow(10, digit) - 1);
//	System.out.println(max);
	int i = 0;
	while (max > 0) {
	    max >>= 1;
	    i++;
	}
	int k = 1 << i;
//	System.out.println(k);
//	System.out.println(i);
	return i;
    }
}
