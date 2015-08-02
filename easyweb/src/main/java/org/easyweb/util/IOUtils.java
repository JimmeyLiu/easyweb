package org.easyweb.util;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by jimmey on 15-7-30.
 */
public class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int EOF = -1;

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void unzipTo(byte[] zipBytes, String parent) throws IOException {
        ZipInputStream inputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes));
        File file = new File(parent);

        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(file));
        ZipEntry entry = null;
        while ((entry = inputStream.getNextEntry()) != null) {
            outputStream.putNextEntry(entry);
        }

    }

    public static byte[] unzip(byte[] bytes) throws IOException {
        GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(bytes));
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buf = new byte[8192];

        for (; ; ) {
            int size = in.read(buf);
            if (size == -1) break;
            out.write(buf, 0, size);
        }

        in.close();
        return out.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        String zip = "/Users/jimmey/workspace/platform/easyweb/apps1.tgz";
        ZipInputStream inputStream = new ZipInputStream(new GZIPInputStream(new FileInputStream(zip)));
        ZipEntry entry = null;
        while ((entry = inputStream.getNextEntry()) != null) {
            System.out.println(entry.getName());
        }
    }

}
