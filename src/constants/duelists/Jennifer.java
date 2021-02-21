package constants.duelists;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Pikachu fan from Lightning Club
 */
public class Jennifer extends constants.duelists.Duelist {
    
    private final int JenniferPortraitAddress = 0xa0fce;
    private final int JenniferSpriteAddress = 0xa38b8;
    private final int JenniferSkin = 0x9c33;
    private final int JenniferBackground = 0x0811;
    private final int JenniferCheeks = 0x1822;
    private final int JenniferHair = 0x2204;
    
    public Jennifer(RandomAccessFile f) {
        super(f);
    }
    
        /**Alters text for default name, pronouns, etc
     * @throws java.io.IOException*/
    @Override
    public void AdjustGameText() throws IOException
    {
            //Adjust default player name
            cartFile.seek(0x128ec);
            cartFile.writeByte(0x39); //J
            cartFile.seek(0x128ee);
            cartFile.writeByte(0x34); //E
            cartFile.seek(0x128f0);
            cartFile.writeByte(0x3d); //N
            cartFile.seek(0x128f2);
            cartFile.writeByte(0x3d); //N
            cartFile.writeShort(0x0348); //Y
            
            femaleCharacterGameText();
    }
    
        /**Replaces the character that appears in duels, menus, etc.
     * @throws java.io.IOException.*/
    @Override
    public void ReplaceCharacterPortrait() throws IOException
    {
            byte [] portraitData = new byte[PortaitBytes];
            cartFile.seek(JenniferPortraitAddress);
            cartFile.read(portraitData, 0, PortaitBytes);
            cartFile.seek(PlayerPortraitStartAddress);
            cartFile.write(portraitData);
            
            //SGB Portait
            cartFile.seek(0x73499);
            cartFile.writeShort(JenniferSkin);
            cartFile.writeShort(JenniferCheeks);
            cartFile.writeShort(JenniferBackground);
            cartFile.writeShort(JenniferHair);
            
            //GBC portrait palette
            cartFile.seek(0xb3ff8);
            cartFile.writeShort(JenniferSkin);
            cartFile.writeShort(JenniferCheeks);
            cartFile.writeShort(JenniferBackground);
            cartFile.writeShort(JenniferHair);
    }
        /**Replaces the character that appears in overworld
     * @throws java.io.IOException.*/
    @Override
    public void ReplaceOverworldSprite() throws IOException
    {
            byte [] spriteData = new byte[SpriteBytes];
            cartFile.seek(JenniferSpriteAddress);
            cartFile.read(spriteData, 0, SpriteBytes);
            cartFile.seek(PlayerOverworldSpriteStartAddress);
            cartFile.write(spriteData);
            
            //Overworld sprite color is currently shared with portait colors
            
            //Map cursor
            cartFile.seek(0xb7b2d);
            cartFile.writeShort(JenniferSkin);
    }
}
