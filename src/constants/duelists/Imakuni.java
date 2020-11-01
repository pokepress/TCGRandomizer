package constants.duelists;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Japanese celebrity that did promotional work for Pokemon during the early
 * years.
 */
public class Imakuni extends constants.duelists.Duelist {
    
    private final int ImakuniPortraitAddress = 0x9c242;
    private final int ImakuniSpriteAddress = 0xa1a18;
    private final int ImakuniLight = 0x1481;
    private final int ImakuniDark = 0x9c92;
    private final int ImakuniDarkest = 0x0480;
    
    public Imakuni(RandomAccessFile f) {
        super(f);
    }
    
        /**Alters text for default name, pronouns, etc
     * @throws java.io.IOException*/
    @Override
    public void AdjustGameText() throws IOException
    {
            //Adjust default player name
            cartFile.seek(0x128ec);
            cartFile.writeByte(0x38); //I
            cartFile.seek(0x128ee);
            cartFile.writeByte(0x3c); //M
            cartFile.seek(0x128f0);
            cartFile.writeByte(0x30); //A
            cartFile.seek(0x128f2);
            cartFile.writeByte(0x3a); //K
            cartFile.writeShort(0x0344); //U
            cartFile.writeShort(0x03bb); //?
    }
    
        /**Replaces the character that appears in duels, menus, etc.
     * @throws java.io.IOException.*/
    @Override
    public void ReplaceCharacterPortrait() throws IOException
    {
            byte [] portraitData = new byte[PortaitBytes];
            cartFile.seek(ImakuniPortraitAddress);
            cartFile.read(portraitData, 0, PortaitBytes);
            cartFile.seek(PlayerPortraitStartAddress);
            cartFile.write(portraitData);
            
            //SGB Portait (?) Needs more testing 
            cartFile.seek(0x7349b);
            cartFile.writeShort(ImakuniDark);
            cartFile.writeShort(ImakuniLight);
            cartFile.writeShort(ImakuniDarkest);
            
            //GBC portrait palette
            cartFile.seek(0xb3ffa);
            cartFile.writeShort(ImakuniDark);
            cartFile.writeShort(ImakuniLight);
            cartFile.writeShort(ImakuniDarkest);
    }
        /**Replaces the character that appears in overworld
     * @throws java.io.IOException.*/
    @Override
    public void ReplaceOverworldSprite() throws IOException
    {
            byte [] portraitData = new byte[SpriteBytes];
            cartFile.seek(ImakuniSpriteAddress);
            cartFile.read(portraitData, 0, SpriteBytes);
            cartFile.seek(PlayerOverworldSpriteStartAddress);
            cartFile.write(portraitData);
            
            //Map cursor
            cartFile.seek(0xb7b2d);
            /*Technically, "light" would be the equivalent color, but I think
            this looks better*/
            cartFile.writeShort(ImakuniDark);
    }
}
