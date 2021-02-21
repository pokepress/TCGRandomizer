package constants.duelists;

import java.io.IOException;
import java.io.RandomAccessFile;

/**Represents a character in the game. Allows replacement of the player 
 * character's graphics.*/
public abstract class Duelist {

    protected RandomAccessFile cartFile;
    
    protected final int PlayerPortraitStartAddress = 0x87d7e;
    protected final int PortaitBytes = 578; //two-byte header plus 4 pixels per byte = 2304 pixels
    protected final int PlayerOverworldSpriteStartAddress = 0x8be90;
    protected final int SpriteBytes = 322; //two-byte header plus 4 pixels per byte = 1280 pixels
    
    
    public Duelist(RandomAccessFile f)
    {
        cartFile = f;
    }
    
    /**Alters text for default name, pronouns, etc
     * @throws java.io.IOException*/
    public abstract void AdjustGameText() throws IOException;
    /**Replaces the character that appears in duels, menus, etc
     * @throws java.io.IOException*/
    public abstract void ReplaceCharacterPortrait() throws IOException;
    /**Replaces the character that appears in overworld
     * @throws java.io.IOException*/
    public abstract void ReplaceOverworldSprite() throws IOException;

    /** Given a character and an output file, performs customizations
     * @param character character being used by player
     * @param cartFile output ROM file
     * @throws java.io.IOException*/
    public static void customizeCharacter(settings.Settings.playerCharacter character, RandomAccessFile cartFile ) throws IOException
    {
        Duelist player;
        switch(character)
        {
            case defaultMark:
                return;
            case mint:
                player = new Mint(cartFile);
                break;
            case imakuni:
                player = new Imakuni(cartFile);
                break;
            case jennifer:
                player = new Jennifer(cartFile);
                break;
            case miyajima:
                player = new Miyajima(cartFile);
                break;
            default:
                return;
        }
        
        if (player != null)
        {
            player.AdjustGameText();
            player.ReplaceCharacterPortrait();
            player.ReplaceOverworldSprite();
        }
    }
    
    /** Derived from "Mint" patch.Used with permission from NikcDC.
     * @throws java.io.IOException
     */
    protected final void femaleCharacterGameText() throws IOException
    {
            //Game Text
            cartFile.seek(0x4c9c7);
            cartFile.writeBytes("er");
            cartFile.seek(0x4cea1);
            cartFile.writeBytes("er");
            cartFile.seek(0x4f095);
            cartFile.writeBytes("er");
            cartFile.seek(0x51af4);
            cartFile.writeBytes("You'll be the first girl I beat\non my new winning streak!");
            cartFile.seek(0x526e3);
            cartFile.writeBytes(". Sorry!        ");
            cartFile.seek(0x52890);
            cartFile.writeBytes("Boys don't take me seriously when we\nduel...  ");
            cartFile.seek(0x529ac);
            cartFile.writeBytes("!\nYou duel like a boy, hehe!        ");
    }
}

