package main.java.wse.index.generator;

import main.java.wse.util.CompressionUtil;
import main.java.wse.FilePaths;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvertedIndexGenerator {
    private static Long POINTER = 0L;

    public final List<Long> MetadatasPos = new ArrayList<>();
    private final List<Long> docIDs = new ArrayList<>();
    private final List<Long> freqs = new ArrayList<>();
    private final List<Long> Metadatas = new ArrayList<>();

    private final FilePaths filePaths = new FilePaths();
    private final CompressionUtil compressionUtil = new CompressionUtil();

    public void writeBlockToDisk() {
        long lastDocID = docIDs.get(docIDs.size() - 1);
        compressionUtil.encodeDifference(docIDs);

        List<Long> block = new ArrayList<>();
        block.addAll(docIDs);
        block.addAll(freqs);
        byte[] compressedBlock = compressionUtil.encodeByVarByte(block);

        appendToMetaData(lastDocID, compressedBlock.length);

        writeToDisk(compressedBlock);
        docIDs.clear();
        freqs.clear();
    }

    public void writeMetaDataToDisk() {
        for (Long metadata: Metadatas) {
            System.out.print(metadata + " ");
        }
        byte[] compressedMetadatas = compressionUtil.encodeByVarByte(Metadatas);

        MetadatasPos.clear();
        MetadatasPos.add(POINTER);
        MetadatasPos.add(new Long(compressedMetadatas.length));

        writeToDisk(compressedMetadatas);
        Metadatas.clear();
    }

    public void appendToBlock(int docID, int freq) {
        docIDs.add(new Long(docID));
        freqs.add(new Long(freq));
    }

    private void appendToMetaData(long lastDocID, int blockSize) {
        Metadatas.add(lastDocID);
        Metadatas.add(POINTER);
        Metadatas.add(new Long(blockSize));
    }


    private void writeToDisk(byte[] data) {
        try {
            DataOutputStream output = new DataOutputStream(new FileOutputStream(filePaths.INVERTED_INDEX, true));
            output.write(data);
            POINTER += new Long(data.length);
            output.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
