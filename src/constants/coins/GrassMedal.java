package constants.coins;

import java.io.IOException;
import java.io.RandomAccessFile;

public class GrassMedal extends Coin {
    
    private final int GrassMedalColorsAddress = 0xb82d5;
    
    public GrassMedal(RandomAccessFile f) {
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
        cartFile.writeInt(0x29292a28);
        cartFile.writeByte(0x59);
        cartFile.seek(0xa820e);
        cartFile.writeByte(0x7c);
        
        cartFile.seek(0xa8210);
        cartFile.writeLong(0xc7cfd7cfd7cf544cL);
        cartFile.writeShort(0x9300);
        
        cartFile.seek(0xa8222);
        cartFile.writeByte(0xc8);
        cartFile.seek(0xa8224);
        cartFile.writeInt(0x2474a674);
        
        cartFile.writeLong(0x32fa5e5fbfbfbfbfL);
        cartFile.writeLong(0xa7a7a9a1a6b899beL);
        
        cartFile.writeLong(0xde5f7c8393cfd7cfL);
        cartFile.writeLong(0xd7cfd7cf544c9300L);
        
        cartFile.writeLong(0x7c83f3faf9fdf9fdL);
        cartFile.writeShort(0xc9dd);
        cartFile.writeByte(0x29);
        
        cartFile.seek(0xa8254);
        cartFile.writeInt(0xc93d39fd);
        cartFile.seek(0xa8259);
        cartFile.writeInt(0xfa4f5f6f);
        cartFile.writeShort(0x2f27);
        cartFile.writeByte(0x2f);
        
        cartFile.seek(0xa8261);
        cartFile.writeByte(0x17);
        cartFile.seek(0xa826a);
        cartFile.writeInt(0x93cfd7cf);
        cartFile.writeShort(0xd7cf);
        
        cartFile.writeInt(0xc7ff7efd);
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
        //copy colors from grass medal
        cartFile.seek(GrassMedalColorsAddress);
        byte[] colors = new byte[8];
        cartFile.read(colors, 0 , 8);
        cartFile.seek(CoinPaletteColorsAddress);
        cartFile.write(colors);
    }
}
