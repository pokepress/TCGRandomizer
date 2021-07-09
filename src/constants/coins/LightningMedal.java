
package constants.coins;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;

public class LightningMedal extends Coin {
    
    private final int LightningMedalColorsAddress = 0xb82ed;
    private final String LightningMedalIPS = "patches/LightningMedal.ips";
    
    public LightningMedal(RandomAccessFile f) {
        super(f);
    }
    
    /**Alters graphics for heads side of coin*/
    public void AlterCoinHeads() throws IOException
    {
        //Also alters other coin positions
        URL ipsURL = LightningMedal.class.getClassLoader().getResource(LightningMedalIPS);
        InputStream ipsFile = ipsURL.openStream();
        utils.IPSParser.applyIPSData(ipsFile, cartFile);
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
        //copy colors from Lightning medal
        cartFile.seek(LightningMedalColorsAddress);
        byte[] colors = new byte[8];
        cartFile.read(colors, 0 , 8);
        cartFile.seek(CoinPaletteColorsAddress);
        cartFile.write(colors);
    }
}