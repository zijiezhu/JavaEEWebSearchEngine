package main.java.wse.query.parser;

import main.java.wse.FilePaths;
import main.java.wse.util.CompressionUtil;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvertedIndexParser {

    private final CompressionUtil compressionUtil = new CompressionUtil();

    public List readIndexBlock(long start, int len) {
        List<Long> list = readDisk(start, len);
        compressionUtil.decodeDifference(list);
        return convertLongtoInt(list);
    }

    public List readDisk(long start, int len) {
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(FilePaths.INVERTED_INDEX));
            byte[] bytes = new byte[len];
            if (start > Integer.MAX_VALUE) {
                input.skipBytes(Integer.MAX_VALUE);
                long value = start - Integer.MAX_VALUE;
                input.skipBytes((int) value);
            } else {
                input.skipBytes((int) start);
            }
            input.readFully(bytes);
            List<Long> list = compressionUtil.decodeByVarByte(bytes);
            input.close();
            return list;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Integer> convertLongtoInt(List<Long> list) {
        if (list == null) return null;
        List<Integer> result = new ArrayList<>();
        for (long l: list) {
            result.add((int) l);
        }
        return result;
    }
}
