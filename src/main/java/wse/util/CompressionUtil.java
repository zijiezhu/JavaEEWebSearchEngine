package main.java.wse.util;

import static java.lang.Math.log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CompressionUtil {

    public byte[] encodeByVarByte(List<Long> numbers) {
        ByteBuffer buf = ByteBuffer.allocate(numbers.size() * (Long.SIZE / Byte.SIZE));
        for (Long number : numbers) {
            buf.put(encodeNumber(number));
        }
        buf.flip();
        byte[] rv = new byte[buf.limit()];
        buf.get(rv);
        buf.clear();
        return rv;
    }


    public List<Long> decodeByVarByte(byte[] byteStream) {
        List<Long> numbers = new ArrayList<>();
        long n = 0L;
        for (byte b : byteStream) {
            //if(b == 0) numbers.add(0L);
            if ((b & 0xff) < 128) {
                n = 128 * n + b;
            } else {
                long num = (128 * n + ((b - 128) & 0xff));
                numbers.add(num);
                n = 0;
            }
        }
        return numbers;
    }

    public void encodeDifference(List<Long> numbers) {
        if (numbers == null) return;
        for (int i = numbers.size() - 1; i > 0; i--) {
            long value = numbers.get(i) - numbers.get(i - 1);
            numbers.set(i, value);
        }
    }

    public void decodeDifference(List<Long> numbers) {
        if (numbers == null) return;

        for (int i = 1; i < numbers.size()/2; i++) {
            long value = numbers.get(i) + numbers.get(i - 1);
            numbers.set(i, value);
        }

    }

    private byte[] encodeNumber(long n) {
        if (n == 0) {
            return new byte[]{0};
        }
        int i = (int) (log(n) / log(128)) + 1;
        byte[] rv = new byte[i];
        int j = i - 1;
        do {
            rv[j--] = (byte) (n % 128);
            n /= 128;
        } while (j >= 0);
        rv[i - 1] += 128;
        return rv;
    }
}
