
package constants.coins;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;

/** Derived from small image from Card GB 2.*/
public class PsyduckCoin extends Coin {
    
    private final String PsyduckCoinIPS = "patches/PsyduckCoin.ips";
    
    public PsyduckCoin(RandomAccessFile f) {
        super(f);
    }
    
    /**Alters graphics for heads side of coin*/
    public void AlterCoinHeads() throws IOException
    {
        //Also alters other coin positions
        URL ipsURL = PsyduckCoin.class.getClassLoader().getResource(PsyduckCoinIPS);
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
        //No need to change first or fourth color
        cartFile.seek(CoinPaletteColorsAddress + 0x2);
        cartFile.writeInt(0x6c7fc645);
    }
}