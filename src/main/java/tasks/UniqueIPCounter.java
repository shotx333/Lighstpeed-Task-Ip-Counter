package tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UniqueIPCounter {
    private static final int BITMAP_SIZE = 1 << 24; // 2^24 bits = 2MB for the bitmap
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java tasks.UniqueIPCounter <filename>");
            return;
        }
        
        String filename = args[0];
        long startTime = System.currentTimeMillis();
        
        try {
            int uniqueCount = countUniqueIPs(filename);
            long endTime = System.currentTimeMillis();
            
            System.out.println("Number of unique IP addresses: " + uniqueCount);
            System.out.println("Time taken: " + (endTime - startTime) + " ms");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private static int countUniqueIPs(String filename) throws IOException {
        byte[] bitmap = new byte[BITMAP_SIZE / 8];
        int uniqueCount = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                long ipValue = ipToLong(line);
                int index = (int) (ipValue / 8);
                int bitOffset = (int) (ipValue % 8);

                if (((bitmap[index] & 0xff) & (1 << bitOffset)) == 0) {
                    bitmap[index] |= (byte) (1 << bitOffset);
                    uniqueCount++;
                }
            }
        }
        
        return uniqueCount;
    }
    
    private static long ipToLong(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) | Integer.parseInt(octets[i]);
        }
        return result;
    }
}
