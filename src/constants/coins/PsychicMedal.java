
package constants.coins;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;

public class PsychicMedal extends Coin {
    
    private final int PsychicMedalColorsAddress = 0xb82fd;
    private final String PsychicMedalIPS = "patches/PsychicMedal.ips";
    
    public PsychicMedal(RandomAccessFile f) {
        super(f);
    }
    
    /**Alters graphics for heads side of coin*/
    public void AlterCoinHeads() throws IOException
    {
        //Also alters other coin positions
        URL ipsURL = PsychicMedal.class.getClassLoader().getResource(PsychicMedalIPS);
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
        //copy colors from psychic medal
        cartFile.seek(PsychicMedalColorsAddress);
        byte[] colors = new byte[8];
        cartFile.read(colors, 0 , 8);
        cartFile.seek(CoinPaletteColorsAddress);
        cartFile.write(colors);
    }
}