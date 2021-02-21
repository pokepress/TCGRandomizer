package constants.duelists;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * GR Water Fort Member from TCGGB 2. 
 * Japanese name is "Miyajima"
 * English name "Jacob" is taken from Card GB 2
 * translation by Artemis251 and Jazz:
 * http://artemis251.fobby.net/pkmntcg2/readme.txt
 */
public class Miyajima extends constants.duelists.Duelist {
    
    private final int MiyajimaSkin1 = 0xd315;
    private final int MiyajimaSkin2 = 0xcc08;
    private final int MiyajimaShirt = 0x887c;
    private final int MiyajimaCap = 0x4000;
    
    private final String MiyajimaIPS = "patches/Miyajima.ips";
    
    public Miyajima(RandomAccessFile f) {
        super(f);
    }
    
    /**Alters text for default name, pronouns, etc
     * @throws java.io.IOException*/
    @Override
    public void AdjustGameText() throws IOException
    {
            //Adjust default player name
            cartFile.seek(0x128ec);
            cartFile.writeByte(0x3c); //M
            cartFile.seek(0x128ee);
            cartFile.writeByte(0x38); //I
            cartFile.seek(0x128f0);
            cartFile.writeByte(0x48); //Y
            cartFile.seek(0x128f2);
            cartFile.writeByte(0x30); //A
    }
    
    /**Replaces the character that appears in duels, menus, etc.
     * @throws java.io.IOException*/
    @Override
    public void ReplaceCharacterPortrait() throws IOException
    {
        URL ipsURL = Miyajima.class.getClassLoader().getResource(MiyajimaIPS);
        InputStream ipsFile = ipsURL.openStream();
        utils.IPSParser.applyIPSData(ipsFile, cartFile);
            
        //SGB Portait
        cartFile.seek(0x73499);
        cartFile.writeShort(MiyajimaSkin1);
        cartFile.writeShort(MiyajimaSkin2);
        cartFile.writeShort(MiyajimaShirt);
        cartFile.writeShort(MiyajimaCap);

        //GBC portrait palette
        cartFile.seek(0xb3ff8);
        cartFile.writeShort(MiyajimaSkin1);
        cartFile.writeShort(MiyajimaSkin2);
        cartFile.writeShort(MiyajimaShirt);
        cartFile.writeShort(MiyajimaCap);
    }
    /**Replaces the character that appears in overworld
     * @throws java.io.IOException*/
    @Override
    public void ReplaceOverworldSprite() throws IOException
    {
        //Sprite data already replaced by IPS patch
            
        //Overworld sprite color is currently shared with portait colors

        //Map cursor
        cartFile.seek(0xb7b2d);
        cartFile.writeShort(MiyajimaSkin1);
    }
}
