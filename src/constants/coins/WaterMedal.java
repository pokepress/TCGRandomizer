package constants.coins;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WaterMedal extends Coin{
    
    private final int WaterMedalColorsAddress = 0xb82e5;
    
    public WaterMedal(RandomAccessFile f)
    {
        super(f);
    }
    
    /**Alters graphics for heads side of coin*/
    public void AlterCoinHeads() throws IOException
    {
        /*Although they use the same number of tiles, the medal graphic uses
          slightly less space than the defualt coin. As such, we have to
          amalgamate the images a bit.*/
        cartFile.seek(0xa8200);
        cartFile.writeByte(0x09);
        cartFile.seek(0xa8202);
        cartFile.writeByte(0x17);
        cartFile.seek(0xa8204);
        cartFile.writeInt(0x2c2d2a29);
        cartFile.writeShort(0x5b58);
        cartFile.seek(0xa820e);
        cartFile.writeByte(0x7c);
        
        cartFile.seek(0xa8210);
        cartFile.writeByte(0xff);
        cartFile.seek(0xa8213);
        cartFile.writeInt(0x00bb87bd);
        cartFile.writeByte(0x83);
        cartFile.writeShort(0x5dc3);
        
        cartFile.seek(0xa8222);
        cartFile.writeByte(0xc8);
        cartFile.seek(0xa8225);
        cartFile.writeShort(0xf4e6);
        cartFile.writeByte(0xf4);
        cartFile.writeLong(0xf2fa5750b6b6aea6L);
        
        cartFile.writeLong(0xa9a0afa0b7b097b0L);
        cartFile.writeLong(0xdb585ec1ae61af60L);
        
        cartFile.writeLong(0xaf60af60ae615ec1L);
        cartFile.writeLong(0x5dc3f3fae9fd59cdL);
        
        cartFile.writeLong(0xf90d59ede9fdf9f5L);
        cartFile.seek(0xa8259);
        cartFile.writeInt(0xfa4a596c);
        cartFile.writeShort(0x2d27);
        cartFile.writeByte(0x2f);
        
        cartFile.seek(0xa8261);
        cartFile.writeByte(0x17);
        cartFile.seek(0xa826a);
        cartFile.writeInt(0xbd83bb87);
        cartFile.writeShort(0xfe01);
        
        cartFile.writeInt(0xffff7efd);
        cartFile.seek(0xa827a);
        cartFile.writeInt(0xf2eae6f4);
        cartFile.seek(0xa827f);
        cartFile.writeByte(0xd4);
        
    }
    /**Alters graphics for tails side of coin*/
    public void AlterCoinTails() throws IOException
    {
        
    }
    /**Alters graphics for flipping graphic a of coin*/
    public void AlterCoinFlippingA() throws IOException
    {
        
    }
    /**Alters graphics for flipping graphic b of coin*/
    public void AlterCoinFlippingB() throws IOException
    {
        
    }
    /**Alters graphics for side view of coin*/
    public void AlterCoinSideView() throws IOException
    {
        
    }
    
    /**Alters palette for coin*/
    public void AlterCoinPalette() throws IOException
    {
        //copy colors from water medal
        cartFile.seek(WaterMedalColorsAddress);
        byte[] colors = new byte[8];
        cartFile.read(colors, 0 , 8);
        cartFile.seek(CoinPaletteColorsAddress);
        cartFile.write(colors);
    }
        
}
