
package constants.coins;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FireMedal extends Coin {
    
    private final int FireMedalColorsAddress = 0xb82dd;
    
    public FireMedal(RandomAccessFile f) {
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
        cartFile.writeInt(0x2f2f2f2f);
        cartFile.writeShort(0x5f5f);
        cartFile.seek(0xa820e);
        cartFile.writeByte(0x7c);
        
        cartFile.seek(0xa8210);
        cartFile.writeLong(0xefefefefd7cfd7cfL);
        cartFile.writeShort(0xbb87);
        
        cartFile.seek(0xa8222);
        cartFile.writeByte(0xc8);
        cartFile.seek(0xa8225);
        cartFile.writeShort(0xf4e6);
        cartFile.writeByte(0xf4);
        cartFile.writeLong(0xf2fa575fb3b7b5b3L);
        
        cartFile.writeLong(0xb6b1b7b0b7b097b0L);
        cartFile.writeLong(0xd750bb877d036d13L);
        
        cartFile.writeLong(0xd630d730ab68ab68L);
        cartFile.writeInt(0x55ccd3fa);
        cartFile.writeShort(0x99bd);
        cartFile.writeByte(0x59);
        
        cartFile.seek(0xa8250);
        cartFile.writeLong(0xd93dd93dd93dd93dL);
        cartFile.writeLong(0xd33a4b586c2d262fL);
        
        cartFile.seek(0xa8261);
        cartFile.writeByte(0x17);
        cartFile.seek(0xa826a);
        cartFile.writeInt(0x55ccba86);
        cartFile.writeShort(0x82ff);
        
        cartFile.seek(0xa8270);
        cartFile.writeInt(0xffff7efd);
        cartFile.seek(0xa827a);
        cartFile.writeShort(0xb26a);
        cartFile.seek(0xa827d);
        cartFile.writeByte(0xf4);
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
        //copy colors from fire medal
        cartFile.seek(FireMedalColorsAddress);
        byte[] colors = new byte[8];
        cartFile.read(colors, 0 , 8);
        cartFile.seek(CoinPaletteColorsAddress);
        cartFile.write(colors);
    }
}
