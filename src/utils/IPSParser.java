package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Takes data from an IPS patch and applies it to the target file
 */
public class IPSParser {
    /**
    * Takes data from an IPS patch and applies it to the target file
    * @param ipsData IPS file data-read linearly
    * @param cartFile output ROM file
    * @throws java.io.IOException
    */
    public static void applyIPSData(InputStream ipsData, RandomAccessFile cartFile) throws IOException
    {
        //Check header-first 5 characters should be "PATCH"
        byte[] header = new byte[5];
        ipsData.read(header, 0, 5);
        if(header[0] != 0x50 || header[1] != 0x41 || header[2] != 0x54 || header[3] != 0x43 || header[4] != 0x48)
        {
            throw new IOException("Missing/malformed patch header.");
        }
        
        int address = 0;
        while(address != 0x454f46)
        {
            //Get next address to modify
            address = (ipsData.read() * 65536) + (ipsData.read() * 256) + ipsData.read();
            
            //Check for end of file "EOF"
            if(address == 0x454f46)
            {
                break;
            }
            
            cartFile.seek(address);
            
            //Get length of next data segment
            int length = (ipsData.read() * 256) + ipsData.read();
            
            if (length > 0)
            {
                //Copy data
                for (;length >0 ; length--)
                {
                    cartFile.writeByte(ipsData.read());
                }
            }
            else
            {
                //Write repeated data
                int runLength = (ipsData.read() * 256) + ipsData.read();
                int outByte = ipsData.read();
                for (;runLength >0 ; runLength--)
                {
                    cartFile.writeByte(outByte);
                }
            }
        }
    }
}
