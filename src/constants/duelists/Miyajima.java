package constants.duelists;

import java.io.IOException;
import java.io.RandomAccessFile;

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
     * @throws java.io.IOException.*/
    @Override
    public void ReplaceCharacterPortrait() throws IOException
    {
            //Row 1 (No changes in block 1 & 6)
            cartFile.seek(0x87d97);
            cartFile.writeByte(0xff);
            cartFile.writeLong(0x00ff00ff00ff03fcL);
            
            cartFile.seek(0x87da3);
            cartFile.writeByte(0xff);
            cartFile.writeInt(0x00ff00ff);
            cartFile.writeShort(0x00ff);
            cartFile.seek(0x87dab);
            cartFile.writeByte(0xff);
            cartFile.writeInt(0xff00ffff);
            
            cartFile.seek(0x87db3);
            cartFile.writeByte(0xff);
            cartFile.writeInt(0x00ff00ff);
            cartFile.writeShort(0x00ff);
            cartFile.seek(0x87dbb);
            cartFile.writeByte(0xff);
            cartFile.writeInt(0xe01ff8e7);
            
            cartFile.seek(0x87dc7);
            cartFile.writeByte(0xff);
            cartFile.writeLong(0x00ff00ff00ff00ffL);
            
            //Row 2
            
            cartFile.seek(0x87dec);
            cartFile.writeShort(0x40bf);
            
            cartFile.seek(0x87df0);
            cartFile.writeLong(0x07fb0ff718ef33dfL);
            cartFile.writeByte(0x64);
            cartFile.seek(0x87dfa);
            cartFile.writeByte(0x66);
            cartFile.seek(0x87dfc);
            cartFile.writeInt(0xe47fe17f);
            
            cartFile.writeLong(0xffffc1ff4cffccffL);
            cartFile.writeByte(0x41);
            cartFile.seek(0x87e0a);
            cartFile.writeShort(0x4cff);
            cartFile.writeInt(0x4cffffff);
            
            cartFile.writeInt(0xfcfbfefd);
            cartFile.writeShort(0xfffe);
            cartFile.writeByte(0xff);
            cartFile.seek(0x87e19);
            cartFile.writeByte(0xff);
            cartFile.writeShort(0xffff);
            cartFile.writeInt(0xffffffff);
            
            cartFile.writeLong(0x00ff00ff00ff807fL);
            cartFile.writeLong(0xc0bfc0bfe0dfe0dfL);
            
            cartFile.seek(0x87e38);
            cartFile.writeInt(0x09f640bf);
            cartFile.seek(0x87e3e);
            cartFile.writeShort(0x807f);
            
            //Row 3
            
            cartFile.writeInt(0x04fb20df);
            cartFile.seek(0x87e48);
            cartFile.writeLong(0x21de4bb5c33d936dL);
            
            cartFile.writeLong(0xff7ffc7cf070fe7eL);
            cartFile.writeLong(0xb1b160606f6f7070L);
            
            cartFile.writeLong(0xffff000000000000L);
            cartFile.writeLong(0x8080c1418100c1c1L);
            
            cartFile.writeLong(0xffff1f1f03037d3dL);
            cartFile.writeLong(0xc3c28100ff7c8782L);
            
            cartFile.writeLong(0xfcf3fefdfffeffffL);
            cartFile.writeLong(0x9f9fefefcfcfcfcfL);
            
            cartFile.writeShort(0x08f7);
            cartFile.seek(0x87e94);
            cartFile.writeInt(0x24db827d);
            cartFile.writeLong(0x827dc1bec1bec9b6L);
            
            //Row 4
            
            cartFile.writeLong(0xa35de31cc13ec837L);
            cartFile.writeLong(0xc837ec13ec13b649L);
            
            cartFile.writeLong(0x7060a0a060602020L);
            cartFile.writeLong(0x20a030a010d008e8L);
            
            cartFile.writeInt(0x000000000);
            cartFile.writeByte(0x26);
            cartFile.seek(0x87ec6);
            cartFile.writeByte(0x27);
            cartFile.seek(0x87ec8);
            cartFile.writeInt(0x1f188080);
            cartFile.writeShort(0xff67);
            cartFile.writeByte(0x00);
            
            cartFile.seek(0x87ed0);
            cartFile.writeShort(0x0300);
            cartFile.writeByte(0x03);
            cartFile.seek(0x87ed4);
            cartFile.writeInt(0x03000300);
            cartFile.writeByte(0x03);
            cartFile.seek(0x87eda);
            cartFile.writeShort(0x4741);
            cartFile.writeInt(0xc7810e02);
            
            cartFile.writeLong(0x8f8f9f9ffffdff9cL);
            cartFile.writeLong(0xff9cbe59bc5339d6L);
            
            cartFile.writeLong(0xc1bee5dae5daf9E6L);
            cartFile.writeLong(0xfb243fc0bf407788L);
            
            //Row 5
            
            cartFile.writeLong(0x3fc01be446b923dcL);
            cartFile.writeLong(0xba455fa0ef107d82L);
            
            cartFile.writeLong(0xbc44ee12b34de11eL);
            cartFile.writeLong(0x7f81de23fd0ff13fL);
            
            cartFile.writeLong(0x12005200f780ffffL);
            cartFile.writeLong(0x8f80C08060605f7fL);
            
            cartFile.writeLong(0x1c0fbc17fc64fc87L);
            cartFile.writeLong(0xfe067b0f3437d4f7L);
            
            cartFile.writeLong(0x17e8cf30fc03e21dL);
            cartFile.writeLong(0x0cf3e11effe01ff8L);
            
            cartFile.writeLong(0xcf301de23bc4f708L);
            cartFile.writeLong(0xde21ff00fb04df20L);
            
            //Row 6
            
            cartFile.writeLong(0xef10ff01fe03fc07L);
            cartFile.writeLong(0xf80ff80ff01ff01fL);
            
            cartFile.writeLong(0xc2fe02fe05fd1afbL);
            cartFile.writeInt(0x64e7989f);
            cartFile.writeShort(0xa0bf);
            cartFile.writeByte(0xc0);
            
            cartFile.seek(0x87f80);
            cartFile.writeShort(0x80ff);
            cartFile.writeByte(0x80);
            cartFile.seek(0x87f84);
            cartFile.writeInt(0x00ff00ff);
            cartFile.writeLong(0x00ff00ff00ff00ffL);
            
            cartFile.writeLong(0x14f70afb0afb05fdL);
            cartFile.writeLong(0x02fe01ff00ff00ffL);
            
            cartFile.writeByte(0x07);
            cartFile.seek(0x87fa2);
            cartFile.writeInt(0x01ff00ff);
            cartFile.writeByte(0x00);
            cartFile.seek(0x87fa8);
            cartFile.writeByte(0xc0);
            cartFile.seek(0x87faa);
            cartFile.writeInt(0x303fd0df);
            cartFile.writeByte(0x30);
            
            cartFile.seek(0x87fb0);
            cartFile.writeLong(0xff00ff00ff807fc0L);
            cartFile.writeLong(0x7fc03fe03fe03fe0L);
            
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
     * @throws java.io.IOException.*/
    @Override
    public void ReplaceOverworldSprite() throws IOException
    {
            cartFile.seek(0x8be92);
            cartFile.writeInt(0x0707181f);
            cartFile.writeShort(0x213f);
            cartFile.writeInt(0x203f303f);
            cartFile.writeShort(0x3f36);
            cartFile.seek(0x8be9f);
            cartFile.writeByte(0x50);
            
            cartFile.seek(0x8bea8);
            cartFile.writeByte(0x7f);
            cartFile.seek(0x8beaa);
            cartFile.writeShort(0x393f);
            cartFile.writeByte(0x16);
            cartFile.seek(0x8beae);
            cartFile.writeShort(0x111f);
            
            cartFile.seek(0x8beb4);
            cartFile.writeInt(0x0707181f);
            cartFile.writeLong(0x213f203f303f3f36L);
            
            cartFile.seek(0x8bec1);
            cartFile.writeByte(0x50);
            cartFile.seek(0x8bec4);
            cartFile.writeInt(0xe0e018f8);
            cartFile.writeLong(0x84fc04fc0cfcfc6cL);
            
            cartFile.seek(0x8bed1);
            cartFile.writeByte(0x0a);
            cartFile.seek(0x8bed9);
            cartFile.writeByte(0x24);
            cartFile.seek(0x8bedb);
            cartFile.writeByte(0x27);
            
            cartFile.seek(0x8bee7);
            cartFile.writeByte(0x0a);
            cartFile.seek(0x8bee9);
            cartFile.writeByte(0x3c);
            cartFile.seek(0x8beeb);
            cartFile.writeShort(0xf868);
            cartFile.seek(0x8beee);
            cartFile.writeShort(0x88f8);
            
            cartFile.seek(0x8bef2);
            cartFile.writeInt(0x0707181f);
            cartFile.writeShort(0x203f);
            cartFile.writeInt(0x213f203f);
            cartFile.writeShort(0x383f);
            cartFile.writeByte(0x7f);
            
            cartFile.seek(0x8bf08);
            cartFile.writeByte(0x73);
            cartFile.seek(0x8bf0a);
            cartFile.writeByte(0x38);
            cartFile.seek(0x8bf0e);
            cartFile.writeShort(0x111f);
            
            cartFile.seek(0x8bf14);
            cartFile.writeInt(0x0707181f);
            cartFile.writeLong(0x203f213f203f383fL);
            
            cartFile.writeByte(0x7f);
            cartFile.seek(0x8bf24);
            cartFile.writeInt(0xe0e018f8);
            cartFile.writeLong(0x04fc84fc04fc1cfcL);
            
            cartFile.writeByte(0xfe);
            cartFile.seek(0x8bf38);
            cartFile.writeByte(0x3f);
            cartFile.seek(0x8bf3a);
            cartFile.writeByte(0x1f);
            cartFile.seek(0x8bf3c);
            cartFile.writeByte(0x14);
            cartFile.seek(0x8bf3e);
            cartFile.writeShort(0x131f);
            
            cartFile.seek(0x8bf4a);
            cartFile.writeByte(0xfe);
            cartFile.seek(0x8bf4c);
            cartFile.writeByte(0x2c);
            
            cartFile.seek(0x8bf52);
            cartFile.writeInt(0x0f0f111f);
            cartFile.writeShort(0x203f);
            cartFile.writeInt(0x203f3f3e);
            cartFile.writeShort(0x3f3e);
            
            cartFile.seek(0x8bf62);
            cartFile.writeInt(0xf0f088f8);
            cartFile.writeShort(0x04fc);
            cartFile.writeInt(0x04fcf868);
            cartFile.writeShort(0xf808);
            
            cartFile.seek(0x8bf8e);
            cartFile.writeShort(0x20e0);
            
            cartFile.seek(0x8bf94);
            cartFile.writeInt(0x0f0f111f);
            cartFile.writeLong(0x203f203f3f3e3f3eL);
            
            cartFile.seek(0x8bfa4);
            cartFile.writeInt(0xf0f088f8);
            cartFile.writeLong(0x04fc04fcf868f808L);
            
            cartFile.seek(0x8bfcc);
            cartFile.writeShort(0x24fc);
            
            //Overworld sprite color is currently shared with portait colors
            
            //Map cursor
            cartFile.seek(0xb7b2d);
            cartFile.writeShort(MiyajimaSkin1);
    }
}
