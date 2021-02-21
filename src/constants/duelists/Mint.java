
package constants.duelists;

import java.io.IOException;
import java.io.RandomAccessFile;

/** Female player character introduced in Japan-only sequel.
 * Used with permission from NikcDC.
 */
public class Mint extends constants.duelists.Duelist {

    public Mint(RandomAccessFile f) {
        super(f);
    }
    
    /**Alters text for default name, pronouns, etc
     * @throws java.io.IOException*/
    @Override
    public void AdjustGameText() throws IOException
    {
        //Adjust default player name
            cartFile.seek(0x128ee);
            cartFile.writeByte(0x38); //I
            cartFile.seek(0x128f0);
            cartFile.writeByte(0x3d); //N
            cartFile.seek(0x128f2);
            cartFile.writeByte(0x43); //T
            
            femaleCharacterGameText();
    }
    
    /**Replaces the character that appears in duels, menus, etc.
     * @throws java.io.IOException.*/
    @Override
    public void ReplaceCharacterPortrait() throws IOException
    {
        cartFile.seek(0x87d97);
        cartFile.writeLong(0xff00fe00fe01fd03L);
        cartFile.writeByte(0xe3);
        cartFile.seek(0x87dA3);
        cartFile.writeLong(0xff00906f6f989c86L);
        cartFile.writeInt(0xe773fcff);
        cartFile.writeByte(0xfe);
        cartFile.seek(0x87db3);
        cartFile.writeLong(0xff000ff0f1deee3dL);
        cartFile.writeInt(0x2310df09);
        cartFile.writeByte(0xfe);
        cartFile.seek(0x87dC7);
        cartFile.writeLong(0xff007f80bf40df60L);
        cartFile.writeByte(0xaf);
        cartFile.seek(0x87de3);
        cartFile.writeLong(0xfe01fd01fd01fd02L);
        cartFile.writeInt(0xfa02fa02);
        cartFile.writeByte(0xfa);
        cartFile.writeLong(0x1f1fffe0ff047f38L);
        cartFile.writeLong(0x7c78f870f160f361L);
        cartFile.writeLong(0x1f1fff07ff07ff0eL);
        cartFile.writeLong(0xff0e7f7efffafff2L);
        cartFile.writeLong(0xc5fefcf3f81ffc1fL);
        cartFile.writeShort(0xfe1f);
        cartFile.seek(0x87e1b);
        cartFile.writeInt(0x1f3f1f7f);
        cartFile.writeByte(0x37);
        cartFile.writeLong(0xd03710f7689b48bbL);
        cartFile.writeShort(0x689b);
        cartFile.writeByte(0x04);
        cartFile.seek(0x87e2c);
        cartFile.writeInt(0xbcc5e6de);
        cartFile.seek(0x87e37);
        cartFile.writeLong(0xfc03fb04f70bcc34L);
        cartFile.writeByte(0x3b);
        cartFile.writeLong(0x03fa07f407f507f5L);
        cartFile.writeLong(0x07f70fef0eee0cedL);
        cartFile.writeLong(0xe7c3efc7ffcfffdfL);
        cartFile.writeInt(0xffdf7f5e);
        cartFile.seek(0x87e5d);
        cartFile.writeShort(0x7f7f);
        cartFile.writeByte(0x7d);
        cartFile.writeLong(0xf7e2e7c6c787c7c7L);
        cartFile.writeLong(0x6727c7c7e6666666L);
        cartFile.writeLong(0xff64e7c4ffbfe784L);
        cartFile.writeLong(0xc7823b3a7f4d4d4dL);
        cartFile.writeLong(0xc5fded77e57f753fL);
        cartFile.writeLong(0x773f3e1bbf199d0dL);
        cartFile.writeInt(0xc8f72fdf);
        cartFile.writeShort(0xf0f0);
        cartFile.seek(0x87e97);
        cartFile.writeShort(0xc780);
        cartFile.seek(0x87e9a);
        cartFile.writeInt(0xee715abd);
        cartFile.writeShort(0xd42f);
        cartFile.writeShort(0x08eb);
        cartFile.seek(0x87ea3);
        cartFile.writeByte(0xe7);
        cartFile.seek(0x87eb0);
        cartFile.writeLong(0x7f7c7f7c7f7c7774L);
        cartFile.writeLong(0x737223aa23aa0797L);
        cartFile.writeShort(0x6464);
        cartFile.seek(0x87ec3);
        cartFile.writeLong(0x600060e0a0d0c090L);
        cartFile.writeInt(0x109800de);
        cartFile.writeByte(0x1e);
        cartFile.writeShort(0x4d0c);
        cartFile.seek(0x87ed3);
        cartFile.writeInt(0x0c000c0c);
        cartFile.seek(0x87ed8);
        cartFile.writeInt(0x1c3c0100);
        cartFile.writeByte(0x01);
        cartFile.seek(0x87ede);
        cartFile.writeShort(0x0301);
        cartFile.writeLong(0xde88df8bfe4afd4cL);
        cartFile.writeLong(0xfdacfeeafeeafefaL);
        cartFile.writeLong(0x1fe820dfa7d8747bL);
        cartFile.writeInt(0x0e8f01f1);
        cartFile.seek(0x87efd);
        cartFile.writeByte(0xfe);
        cartFile.seek(0x87f10);
        cartFile.writeLong(0x07f50feb0ded0bebL);
        cartFile.writeLong(0x03f307e718d830b7L);
        cartFile.writeLong(0xce8ae4c4f0e0fdf0L);
        cartFile.writeLong(0xf7ff030e03fe03feL);
        cartFile.writeLong(0x07031f0d7f31fbc1L);
        cartFile.writeLong(0xe301830103033c3cL);
        cartFile.writeInt(0xfffcfffe);
        cartFile.writeShort(0xfffe);
        cartFile.writeByte(0x7f);
        cartFile.seek(0x87f48);
        cartFile.writeLong(0x9dfd6cfc8ebe1e7eL);
        cartFile.seek(0x87f52);
        cartFile.writeInt(0x007f007f);
        cartFile.seek(0x87f5a);
        cartFile.writeShort(0x80bf);
        cartFile.seek(0x87f5d);
        cartFile.writeByte(0x3f);
        cartFile.seek(0x87f67);
        cartFile.writeByte(0xff);
        cartFile.seek(0x87f69);
        cartFile.writeByte(0xff);
        cartFile.seek(0x87f6b);
        cartFile.writeInt(0xff00ff00);
        cartFile.writeByte(0xfe);
        cartFile.writeLong(0x0ccf02f30dcf31bfL);
        cartFile.writeInt(0x427e417f);
        cartFile.writeShort(0x517f);
        cartFile.writeByte(0x88);
        cartFile.seek(0x87f80);
        cartFile.writeInt(0x03ff0efe);
        cartFile.seek(0x87f85);
        cartFile.writeLong(0x790c7f0efb1ff11fL);
        cartFile.writeShort(0xff92);
        cartFile.writeByte(0xf2);
        cartFile.writeLong(0xc0c3003f00ff03ffL);
        cartFile.writeLong(0x00ff00ff007f01ffL);
        cartFile.writeByte(0x1e);
        cartFile.seek(0x87fa2);
        cartFile.writeInt(0x66fef3ff);
        cartFile.writeByte(0x0c);
        cartFile.seek(0x87fa8);
        cartFile.writeByte(0x03);
        cartFile.seek(0x87faa);
        cartFile.writeByte(0x0c);
        cartFile.seek(0x87fae);
        cartFile.writeByte(0x60);
        cartFile.seek(0x87fb5);
        cartFile.writeLong(0x7f80bfc0df40df20L);
        cartFile.writeShort(0xef20);
        cartFile.writeByte(0xef);
    }
    
    /**Replaces the character that appears in overworld
     * @throws java.io.IOException.*/
    @Override
    public void ReplaceOverworldSprite() throws IOException
    {
        cartFile.seek(0x8be90);
        cartFile.writeByte(0x00);
        cartFile.seek(0x8be9a);
        cartFile.writeInt(0x1b1c1f1f);
        cartFile.writeShort(0x3f3d);
        cartFile.writeInt(0x3f2a7f72);
        cartFile.writeShort(0x7f78);
        cartFile.seek(0x8bea7);
        cartFile.writeByte(0x6f);
        cartFile.seek(0x8beac);
        cartFile.writeInt(0x0f0f0b0d);
        cartFile.writeShort(0x0606);
        cartFile.seek(0x8bebc);
        cartFile.writeInt(0x1b1c1f1f);
        cartFile.writeShort(0x3f2d);
        cartFile.seek(0x8becc);
        cartFile.writeInt(0xd838f8f8);
        cartFile.writeLong(0xfcb43f2a7f727f78L);
        cartFile.seek(0x8bed9);
        cartFile.writeInt(0x371f130c);
        cartFile.writeShort(0x0f07);
        cartFile.writeByte(0x07);
        cartFile.seek(0x8bee3);
        cartFile.writeByte(0x56);
        cartFile.seek(0x8beeb);
        cartFile.writeByte(0xf8);
        cartFile.seek(0x8beee);
        cartFile.writeShort(0xc8b8);
        cartFile.seek(0x8bef2);
        cartFile.writeLong(0x000003030c0f101fL);
        cartFile.writeInt(0x19171e1f);
        cartFile.writeShort(0x333f);
        cartFile.writeInt(0x353f7f7f);
        cartFile.seek(0x8bf05);
        cartFile.writeByte(0x3f);
        cartFile.seek(0x8bf07);
        cartFile.writeShort(0x6f7b);
        cartFile.writeByte(0x4f);
        cartFile.seek(0x8bf0c);
        cartFile.writeInt(0x0f0f0d0b);
        cartFile.writeShort(0x0606);
        cartFile.seek(0x8bf18);
        cartFile.writeShort(0x101f);
        cartFile.seek(0x8bf1c);
        cartFile.writeInt(0x1e1f131f);
        cartFile.writeShort(0x353f);
        cartFile.seek(0x8bf28);
        cartFile.writeShort(0x08f8);
        cartFile.seek(0x8bf2c);
        cartFile.writeInt(0x78f8c8f8);
        cartFile.writeShort(0xacfc);
        cartFile.seek(0x8bf33);
        cartFile.writeByte(0x7f);
        cartFile.seek(0x8bf35);
        cartFile.writeInt(0x7f3f3f1f);
        cartFile.writeShort(0x1f13);
        cartFile.seek(0x8bf3c);
        cartFile.writeInt(0x0f0f0a0d);
        cartFile.writeInt(0x0707fcfc);
        cartFile.seek(0x8bf45);
        cartFile.writeLong(0xfcf8f8f8e8f8c830L);
        cartFile.writeShort(0xf0e0);
        cartFile.writeByte(0xe0);
        cartFile.seek(0x8bf5A);
        cartFile.writeInt(0x6f71bfff);
        cartFile.writeShort(0xffff);
        cartFile.seek(0x8bf61);
        cartFile.writeByte(0x1f);
        cartFile.seek(0x8bf68);
        cartFile.writeByte(0x9e);
        cartFile.seek(0x8bf6a);
        cartFile.writeByte(0xee);
        cartFile.seek(0x8bf6d);
        cartFile.writeByte(0xCA);
        cartFile.seek(0x8bf6f);
        cartFile.writeByte(0xa8);
        cartFile.seek(0x8bf71);
        cartFile.writeLong(0xa83f3d3f3e3f3f1fL);
        cartFile.writeByte(0x1e);
        cartFile.seek(0x8bf88);
        cartFile.writeInt(0xa0e0e0e0);
        cartFile.writeShort(0x40c0);
        cartFile.seek(0x8bf9c);
        cartFile.writeInt(0x6f71beff);
        cartFile.writeShort(0xffff);
        cartFile.seek(0x8bfaa);
        cartFile.writeByte(0xbe);
        cartFile.seek(0x8bfac);
        cartFile.writeByte(0xee);
        cartFile.seek(0x8bfaf);
        cartFile.writeByte(0xCA);
        cartFile.seek(0x8bfb1);
        cartFile.writeByte(0xa8);
        cartFile.seek(0x8bfb3);
        cartFile.writeInt(0x1f3f3d3f);
        cartFile.writeShort(0x3e3f);
        cartFile.writeByte(0x3f);
        cartFile.seek(0x8bfbb);
        cartFile.writeInt(0x1d1f1917);
        cartFile.writeByte(0x1f);
        cartFile.writeShort(0x1e1e);
        cartFile.seek(0x8bfc3);
        cartFile.writeByte(0xa8);
        
        //SGB Palette
        cartFile.seek(0x7349d);
        cartFile.writeShort(0xe665);
        
        //GBC Palette
        cartFile.seek(0xb3ffc);
        cartFile.writeShort(0xe665);
        //Map Cursor
        cartFile.seek(0xb7b2d);
        cartFile.writeShort(0xe665);
    }
}
