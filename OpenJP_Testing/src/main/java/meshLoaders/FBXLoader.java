package meshLoaders;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import util.BinMesh;
import util.Binary;
import util.MeshData;

public class FBXLoader {

    private static final String FBX_BINARY_HEADER = "Kaydara FBX Binary  ";

    private MeshDataBuilder builder;
    private byte[] bytes;

    private FBXLoader(byte[] bytes) throws Exception {
        builder = new MeshDataBuilder();
        this.bytes = bytes;

        parse();
    }

    private void parse() throws Exception {
        if (!Binary.bytesToString(bytes, 0, 0x14).equals(FBX_BINARY_HEADER)) {
            System.out.println("Unknown file: \"" + Binary.bytesToString(bytes, 0, 20) + "\"");
            return;
        }
        long version = Binary.binToUIntLE(bytes, 0x17);
        System.out.println("Version " + version);

        int pointer = 0x1B;
        pointer = parseProperty(pointer);


    }
    
    private int parseProperty(int sI) throws Exception {
        return parseProperty(sI, 0);
    }

    private int parseProperty(int pointer, int level) throws Exception {
        String sString = "";
        for (int i = 0; i < level; i++) {
            sString += "|";
        }
        System.out.print(sString+"Property @" + pointer + ": ");
        long endOffset = Binary.binToUIntLE(bytes, pointer);
        System.out.print("eO=" + endOffset);
        if(endOffset > 0xffffff) {
            throw new Exception("Property too big");
        }
        int endIndex = pointer + (int)endOffset;
        pointer += 0x4;

        long numProperties = Binary.binToUIntLE(bytes, pointer);
        pointer += 0x4;
        System.out.print(", nP=" + numProperties);
        long propertyListLen = Binary.binToUIntLE(bytes, pointer);
        pointer += 0x4;
        System.out.print(", pLL=" + propertyListLen);
        int nameLen = bytes[pointer] & 0xFF; 
        pointer += 0x1;
        System.out.print(", nL=" + nameLen);
        String name = Binary.bytesToString(bytes, pointer, pointer + nameLen);
        pointer += nameLen;
        System.out.println(", name=\"" + name + "\"");

        for (int i = 0; i < numProperties; i++) {
            pointer = parsePropertyRecord(pointer, level);
        }

        if (level > 250) {
            // System.err.println("Recursion limit");
            // return pointer + (int) endOffset;
            throw new Exception("Recursion limit");
        }
        
        while (pointer + 13 < endIndex) { // We have nested records
            boolean zt = true;
            for (int i = 0; i < 13; i++) {
                zt = zt && (bytes[pointer + i] == 0x00);
            }
            if (zt) {
                pointer += 13;
                System.out.println(sString + "End of nested records");
                return pointer;
            }
            // System.out.println("Nested records");
            pointer = parseProperty(pointer, level + 1);
        }

        return endIndex;
    }
    
    private int parsePropertyRecord(int pointer, int level) throws Exception {
        String sString = "";
        for (int i = 0; i < level; i++) {
            sString += "|";
        }

        char type = (char) bytes[pointer];
        System.out.print(sString + type + ": ");
        pointer += 0x1;
        if (type == 'Y') { // 2 byte signed int
            pointer += 0x2;
        } else if (type == 'c') { // 1 byte boolean in LSB
            boolean bVal = bytes[pointer] == 1;
            System.out.print(bVal);
            pointer += 0x1;
        } else if (type == 'I') { // 4 byte signed integer
            int iVal = Binary.binToIntLE(bytes, pointer);
            System.out.print(iVal);
            pointer += 0x4;
        } else if (type == 'F') { // 4 byte single-precision iEEE 754 number
            float fVal = Binary.binToFloat(bytes, pointer);
            System.out.print(fVal);
            pointer += 0x4;
        } else if (type == 'D') { // 8 byte double-precision iEEE 754 number
            double dVal = Binary.binToFloat(bytes, pointer);
            System.out.print(dVal);
            pointer += 0x8;
        } else if (type == 'L') { // 8 byte signed integer
            long lVal = Binary.binToLongLE(bytes, pointer);
            System.out.print(lVal);
            pointer += 0x8;
        } else if (type == 'f') { // array of 4 byte single-precision iEEE 754 numbers
            // long lVal = Binary.binToLongLE(bytes, pointer);
            long arrLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long encoding = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long compLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            if (encoding == 1) {
                System.out.println("Zip encoding not yet supported");
                return pointer + (int) compLength;
            }

            float[] arr = new float[(int) arrLength];
            for (int i = 0; i < arrLength; i++) {
                arr[i] = Binary.binToFloat(bytes, pointer);
                pointer += 0x4;
            }
        } else if (type == 'd') { // array of 8 byte double-precision iEEE 754 numbers
            // long lVal = Binary.binToLongLE(bytes, pointer);
            long arrLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long encoding = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long compLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            if (encoding == 1) {
                // System.out.println("Zip encoding not yet supported");
                byte[] data = uncompress(pointer, compLength);
                System.out.println("[" + data + "]");

                return pointer + (int) compLength;
            }

            double[] arr = new double[(int) arrLength];
            for (int i = 0; i < arrLength; i++) {
                arr[i] = Binary.binToDouble(bytes, pointer);
                pointer += 0x8;
            }
        } else if (type == 'i') { // array of 4 byte signed integers
            // long lVal = Binary.binToLongLE(bytes, pointer);
            long arrLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long encoding = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long compLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            if (encoding == 1) {
                // System.out.println("Zip encoding not yet supported");
                byte[] data = uncompress(pointer, compLength);
                System.out.println("[" + data + "]");

                return pointer + (int) compLength;
            }

            int[] arr = new int[(int) arrLength];
            for (int i = 0; i < arrLength; i++) {
                arr[i] = Binary.binToIntLE(bytes, pointer);
                pointer += 0x4;
            }
        } else if (type == 'l') { // array of 4 byte signed integers
            // long lVal = Binary.binToLongLE(bytes, pointer);
            long arrLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long encoding = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long compLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            if (encoding == 1) {
                // System.out.println("Zip encoding not yet supported");
                byte[] data = uncompress(pointer, compLength);
                System.out.println("[" + data + "]");

                return pointer + (int) compLength;
                // return pointer;
            }

            long[] arr = new long[(int) arrLength];
            for (int i = 0; i < arrLength; i++) {
                arr[i] = Binary.binToLongLE(bytes, pointer);
                pointer += 0x8;
            }
        } else if (type == 'b') { // array of 4 byte signed integers
            // long lVal = Binary.binToLongLE(bytes, pointer);
            long arrLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long encoding = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            long compLength = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            if (encoding == 1) {
                // System.out.println("Zip encoding not yet supported");
                byte[] data = uncompress(pointer, compLength);
                System.out.println("[" + data + "]");

                return pointer + (int) compLength;
            }

            boolean[] arr = new boolean[(int) arrLength];
            for (int i = 0; i < arrLength; i++) {
                arr[i] = bytes[pointer] == 1;
                pointer += 0x1;
            }
        } else if (type == 'S') {
            long length = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            String str = Binary.bytesToString(bytes, pointer, pointer + (int) length);
            System.out.print("\"" + str + "\"");
            pointer += length;
        } else if (type == 'R') {
            long length = Binary.binToUIntLE(bytes, pointer);
            pointer += 0x4;
            byte[] data = new byte[(int) length];
            for (int i = 0; i < length; i++) {
                data[i] = bytes[pointer];
                pointer++;
            }
        } else {
            System.out.print("Unknown type");
            throw new Exception("Unknown type '" + type + "'");
        }
        System.out.println();
        return pointer;
    }

    private byte[] uncompress(int pointer, long length) throws IOException {
        return uncompress(pointer, (int) length);
    }

    private byte[] uncompress(int pointer, int length) throws IOException {
        // InputStream bIS = new ByteArrayInputStream(new byte[(int) compLength]);
        byte[] iData = new byte[length];
        for (int i = 0; i < length; i++) {
            iData[i] = bytes[pointer + i];
        }
        InputStream bIS = new ByteArrayInputStream(iData);
        // for(int i = 0; i < compLength; i++) {
        //     bIS.write(bytes[pointer]);
        //     pointer++;
        // }
        InflaterInputStream inf = new InflaterInputStream(bIS);
        // System.out.println(inf.available()); 
        byte[] data = inf.readAllBytes();
        inf.close();
        return data;
    }

    private MeshData build() {
        return builder.build("MESH");
    }
   
    
    public static MeshData parseFile(BufferedReader fileReader) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int b = fileReader.read();
        while (b != -1) {
            bos.write(b);
            b = fileReader.read();
        }
        fileReader.close();
        byte[] bytes = bos.toByteArray();

        try {
            return new FBXLoader(bytes).build();
        } catch (Exception e) {
            // e.printStackTrace();
            System.err.println();
            System.err.println(e.getClass()+": "+e.getMessage());
            return null;
        }
    }
}
