package constants.duelists;

import java.io.IOException;
import java.io.RandomAccessFile;

/**Represents a character in the game. Allows replacement of the player 
 * character's graphics.*/
public abstract class Duelist {

    protected RandomAccessFile cartFile;
    
    protected final int PlayerPortraitStartAddress = 0x87d7e;
    protected final int PlayerOverworldSpriteStartAddress = 0x8be90;
    
    public Duelist(RandomAccessFile f)
    {
        cartFile = f;
    }
    
    /**Alters text for default name, pronouns, etc.*/
    public abstract void AdjustGameText() throws IOException;
    /**Replaces the character that appears in duels, menus, etc.*/
    public abstract void ReplaceCharacterPortrait() throws IOException;
    /**Replaces the character that appears in overworld*/
    public abstract void ReplaceOverworldSprite() throws IOException;

}