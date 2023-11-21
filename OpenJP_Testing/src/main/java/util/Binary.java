package util;

public class Binary {
    
    
	/**
	 * Convert a float to byte array<br><br>
	 * Uses IEEE 754 floating-point "single format"
	 * @param f float to convert
	 * @return 4 byte array representing the float
	 */
	public static byte[] floatToBin(float f) {
		int bits = Float.floatToIntBits(f);
		return new byte[] {
				(byte) (bits >> 24),
				(byte) (bits >> 16),
				(byte) (bits >> 8),
				(byte) (bits)
		};
	}
	
    /**
     * Convert a byte array to a float<br><br>
     * Uses IEEE 754 floating-point "single format"
     * @param bytes 4 byte array representing the float
     * @return Float represented by the bytes
     */
    public static float binToFloat(byte[] bytes) {
        return binToFloat(bytes, 0);
    }
    /**
     * Convert a byte array to a float<br><br>
     * Uses IEEE 754 floating-point "single format"
     * @param bytes 4 byte array representing the float
	 * @param start starting index in array
     * @return Float represented by the bytes
     */
	public static float binToFloat(byte[] bytes, int start) {
		int intBits = bytes[start+0] << 24 | (bytes[start+1] & 0xFF) << 16 | (bytes[start+2] & 0xFF) << 8 | (bytes[start+3] & 0xFF);
		return Float.intBitsToFloat(intBits);
	}
	/**
	 * Convert a bytes to a float.<br><br>
	 * Uses IEEE 754 floating-point "single format"
	 * @param b0 high byte (<code>0xff000000</code>)
	 * @param b1 medium high byte (<code>0x00ff0000</code>)
	 * @param b2 medium low byte (<code>0x0000ff00</code>)
	 * @param b3 low byte (<code>0x000000ff</code>)
	 * @return Float represented by the bytes
	 */
    public static float binToFloat(byte b0, byte b1, byte b2, byte b3) {
        int intBits = b0 << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b3 & 0xFF);
        return Float.intBitsToFloat(intBits);
    }
    
	
    /**
     * Convert a byte array to a double<br><br>
     * Uses IEEE 754 floating-point "double format"
     * @param bytes 8 byte array representing the float
     * @return Double represented by the bytes
     */
    public static double binToDouble(byte[] bytes) {
        return binToDouble(bytes, 0);
    }
    /**
     * Convert a byte array to a double<br><br>
     * Uses IEEE 754 floating-point "double format"
     * @param bytes 8 byte array representing the float
	 * @param start starting index in array
     * @return Double represented by the bytes
     */
	public static double binToDouble(byte[] bytes, int start) {
        return binToDouble(bytes[start + 0], bytes[start + 1], bytes[start + 2], bytes[start + 3], bytes[start + 4], bytes[start + 5], bytes[start + 6], bytes[start + 7]);
	}
	/**
	 * Convert a bytes to a double.<br><br>
	 * Uses IEEE 754 floating-point "double format"
	 * @param b0 high byte (<code>0xff000000_00000000</code>)
	 * @param b1 medium high byte (<code>0x00ff0000_00000000</code>)
	 * @param b2 medium low byte (<code>0x0000ff00_00000000</code>)
	 * @param b3 low byte (<code>0x000000ff_00000000</code>)
	 * @param b4 high byte (<code>0x00000000_ff000000</code>)
	 * @param b5 medium high byte (<code>0x00000000_00ff0000</code>)
	 * @param b6 medium low byte (<code>0x00000000_0000ff00</code>)
	 * @param b7 low byte (<code>0x00000000_000000ff</code>)
	 * @return Double represented by the bytes
	 */
	public static double binToDouble(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
		long longBits = ((b0 & 0xFF) << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b3 & 0xFF)) << 32 | ((b4 & 0xFF) << 24 | (b5 & 0xFF) << 16 | (b6 & 0xFF) << 8 | (b7 & 0xFF));
		return Double.longBitsToDouble(longBits);
	}
    
	/**
	 * Convert an unsigned integer to bytes
	 * @param n unsigned integer to convert
	 * @return 4 byte array representing the integer
	 */
    public static byte[] uIntToBin(int n) {
        return new byte[] {
                (byte) (n >> 24),
                (byte) (n >> 16),
                (byte) (n >> 8),
                (byte) (n)
        };
    }
    
	/**
	 * Convert byte array to integer in Big Endian
	 * @param bytes 4 byte array representing the integer
	 * @return Signed integer represented by the bytes
	 */
	public static int binToInt(byte[] bytes) {
		return binToInt(bytes, 0);
	}
	/**
	 * Convert byte array to integer in Big Endian
	 * @param bytes 4 byte array representing the integer
	 * @param start starting index in array
	 * @return Signed integer represented by the bytes
	 */
	public static int binToInt(byte[] bytes, int start) {
		return binToInt(bytes[start+0], bytes[start+1], bytes[start+2], bytes[start+3]);
	}
	/**
	 * Convert bytes to integer in Big Endian
	 * @param b0 high byte (<code>0xff000000</code>)
	 * @param b1 medium high byte (<code>0x00ff0000</code>)
	 * @param b2 medium low byte (<code>0x0000ff00</code>)
	 * @param b3 low byte (<code>0x000000ff</code>)
	 * @return Signed integer represented by the bytes
	 */
    public static int binToInt(byte b0, byte b1, byte b2, byte b3) {
        return b0 << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b3 & 0xFF);
    }
    
	/**
	 * Convert byte array to integer in Little Endian
	 * @param bytes 4 byte array representing the integer
	 * @return Signed integer represented by the bytes
	 */
	public static int binToIntLE(byte[] bytes) {
		return binToIntLE(bytes, 0);
	}
	/**
	 * Convert byte array to integer in Little Endian
	 * @param bytes 4 byte array representing the integer
	 * @param start starting index in array
	 * @return Signed integer represented by the bytes
	 */
	public static int binToIntLE(byte[] bytes, int start) {
		return binToIntLE(bytes[start+0], bytes[start+1], bytes[start+2], bytes[start+3]);
	}
	/**
	 * Convert bytes to integer in Little Endian
	 * @param b0 low byte (<code>0x000000ff</code>)
	 * @param b1 medium low byte (<code>0x0000ff00</code>)
	 * @param b2 medium high byte (<code>0xff000000</code>)
	 * @param b3 high byte (<code>0xff000000</code>)
	 * @return Signed integer represented by the bytes
	 */
	public static int binToIntLE(byte b0, byte b1, byte b2, byte b3) {
		return b3 << 24 | (b2 & 0xFF) << 16 | (b1 & 0xFF) << 8 | (b0 & 0xFF);
	}
	
	/**
	 * Convert byte array to unsigned integer in Big Endian
	 * @param bytes 4 byte array representing the integer
	 * @return Unsigned integer represented by the bytes
	 */
	public static long binToUInt(byte[] bytes) {
		return binToUInt(bytes, 0);
	}
	/**
	 * Convert byte array to unsigned integer in Big Endian
	 * @param bytes 4 byte array representing the integer
	 * @param start starting index in array
	 * @return Unsigned integer represented by the bytes
	 */
	public static long binToUInt(byte[] bytes, int start) {
		return binToUInt(bytes[start+0], bytes[start+1], bytes[start+2], bytes[start+3]);
	}
	/**
	 * Convert bytes to unsigned integer in Big Endian
	 * @param b0 high byte (<code>0xff000000</code>)
	 * @param b1 medium high byte (<code>0x00ff0000</code>)
	 * @param b2 medium low byte (<code>0x0000ff00</code>)
	 * @param b3 low byte (<code>0x000000ff</code>)
	 * @return Unsigned integer represented by the bytes
	 */
    public static long binToUInt(byte b0, byte b1, byte b2, byte b3) {
        return (((long) b0 & 0xFF) << 24) + (((long) b1 & 0xFF) << 16) + (((long) b2 & 0xFF) << 8)
                + (((long) b3 & 0xFF));
    }
    
	/**
	 * Convert byte array to unsigned integer in Little Endian
	 * @param bytes 4 byte array representing the integer
	 * @return Unsigned integer represented by the bytes
	 */
	public static long binToUIntLE(byte[] bytes) {
		return binToUIntLE(bytes, 0);
	}
	/**
	 * Convert byte array to unsigned integer in Little Endian
	 * @param bytes 4 byte array representing the integer
	 * @param start starting index in array
	 * @return Unsigned integer represented by the bytes
	 */
	public static long binToUIntLE(byte[] bytes, int start) {
		return binToUIntLE(bytes[start+0], bytes[start+1], bytes[start+2], bytes[start+3]);
	}
	/**
	 * Convert bytes to unsigned integer in Little Endian
	 * @param b0 high byte (<code>0xff000000</code>)
	 * @param b1 medium high byte (<code>0x00ff0000</code>)
	 * @param b2 medium low byte (<code>0x0000ff00</code>)
	 * @param b3 low byte (<code>0x000000ff</code>)
	 * @return Unsigned integer represented by the bytes
	 */
    public static long binToUIntLE(byte b0, byte b1, byte b2, byte b3) {
        return (((long) b3 & 0xFF) << 24) + (((long) b2 & 0xFF) << 16) + (((long) b1 & 0xFF) << 8)
                + (((long) b0 & 0xFF));
    }
    
	/**
	 * Convert byte array to signed long in Little Endian
	 * @param bytes 8 byte array representing the integer
	 * @return Signed long represented by the bytes
	 */
	public static long binToLongLE(byte[] bytes) {
		return binToLongLE(bytes, 0);
	}
	/**
	 * Convert byte array to signed long in Little Endian
	 * @param bytes 8 byte array representing the integer
	 * @param start starting index in array
	 * @return Signed long represented by the bytes
	 */
	public static long binToLongLE(byte[] bytes, int start) {
		return binToLongLE(bytes[start+0], bytes[start+1], bytes[start+2], bytes[start+3], bytes[start+4], bytes[start+5], bytes[start+6], bytes[start+7]);
	}
	/**
	 * Convert bytes to signed long in Little Endian
	 * @param b0 high byte (<code>0xff000000_00000000</code>)
	 * @param b1 medium high byte (<code>0x00ff0000_00000000</code>)
	 * @param b2 medium low byte (<code>0x0000ff00_00000000</code>)
	 * @param b3 low byte (<code>0x000000ff_00000000</code>)
	 * @param b4 high byte (<code>0x00000000_ff000000</code>)
	 * @param b5 medium high byte (<code>0x00000000_00ff0000</code>)
	 * @param b6 medium low byte (<code>0x00000000_0000ff00</code>)
	 * @param b7 low byte (<code>0x00000000_000000ff</code>)
	 * @return Signed long represented by the bytes
	 */
    public static long binToLongLE(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
        return ((b0 & 0xFF) << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b3 & 0xFF)) << 32 | ((b4 & 0xFF) << 24 | (b5 & 0xFF) << 16 | (b6 & 0xFF) << 8 | (b7 & 0xFF));
    }
    
    public static String bytesToString(byte[] bytes, int start, int end) {
        String o = "";
        for (int i = start; i < end; i++) {
            o += (char) bytes[i];
        }
        return o;
    }
}
