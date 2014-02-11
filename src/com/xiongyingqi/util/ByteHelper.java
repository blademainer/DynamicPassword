package com.xiongyingqi.util;

/**
 * 
 * @author xiongyingqi <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2014-1-21 上午9:56:22
 */
public class ByteHelper {
    public static byte[] intToBytes(int i) {
	byte[] b = new byte[4];
	b[0] = (byte) (0xff & i);
	b[1] = (byte) ((0xff00 & i) >> 8);
	b[2] = (byte) ((0xff0000 & i) >> 16);
	b[3] = (byte) ((0xff000000 & i) >> 24);
	return b;
    }

    public static int bytesToInt(byte[] bytes) {
	int num = bytes[0] & 0xFF;
	num |= ((bytes[1] << 8) & 0xFF00);
	num |= ((bytes[2] << 16) & 0xFF0000);
	num |= ((bytes[3] << 24) & 0xFF000000);
	return num;
    }

    public static long bytesToLong(byte[] b) {
	long temp = 0;
	long res = 0;
	for (int i = 0; i < 8; i++) {
	    res <<= 8;
	    temp = b[i] & 0xff;
	    res |= temp;
	}
	return res;
    }

    public static byte[] longToBytes(long num) {
	byte[] b = new byte[8];
	for (int i = 0; i < 8; i++) {
	    b[i] = (byte) (num >>> (56 - (i * 8)));
	}
	return b;
    }
}
