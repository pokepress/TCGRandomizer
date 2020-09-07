package logic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static constants.Cards.*;
import constants.Cards;
import constants.Constants;
import constants.Fields.CardFields;
import constants.WRGroups;
import gui.GUIController;
import settings.EvoTypes;
import settings.Settings;
import settings.Settings.Options;
import settings.Settings.wrRandomType;
import utils.Utils;

class ProgramLogic {
		
	private static final GUIController gui = GUIController.getGuiController();

	ProgramLogic() {}
	
	/** Makes sure that tcg.gbc is actually a tcg ROM 
	  * @return true if header data matches, false otherwise */
	static boolean verifyRom (FileChannel chin) throws IOException {
		
		byte[] header = {0x50, 0x4F, 0x4B, 0x45, 0x43, 0x41, 0x52, 0x44, 0x00, 0x00, 0x00, 0x41, 0x58, 0x51, 0x45, 
				(byte) 0x80, 0x30, 0x31, 0x03, 0x1B, 0x05, 0x03, 0x01, 0x33, 0x00, 0x34, 0x26, (byte) 0xA6};
		
		ByteBuffer headerin = ByteBuffer.allocate(header.length);
		chin.read(headerin, 0x134);
		
		if (((ByteBuffer) headerin.rewind()).compareTo(ByteBuffer.wrap(header)) == 0)
			return true;
		else
			return false;
	}
	
	/** Creates a copy of tcg.gbc named tcgrandomized.gbc */
	static void createRomCopy (FileChannel chin, FileChannel chout) throws IOException {
		
		chin.transferTo(0, chin.size(), chout);
	}
	
	/** Copies data of all Pokemon cards to two byte buffers */
	static void readPokemonCardsData (FileChannel ch, ByteBuffer bbRead, ByteBuffer bbWrite) throws IOException {
		
		Utils.init(ch);
		ch.read(bbRead);
		Utils.init(ch);
		ch.read(bbWrite);
	}
	
	static void matchAttackEnergiesToType (ByteBuffer bb) throws IOException {

		byte[] grass_1 = {0x01, 0x00, 0x00, 0x00};
		byte[] water_1 = {0x00, 0x01, 0x00, 0x00};
		byte[] light_2 = {0x00, 0x20, 0x00, 0x00};
		
		Utils.initTo(bb, Cards.Exeggcute.ordinal(), CardFields.MOVE1);
		bb.put(grass_1); /* Exeggcute's Hypnosis */
		
		Utils.initTo(bb, Cards.Exeggutor.ordinal(), CardFields.MOVE1);
		bb.put(grass_1); /* Exeggutor's Teleport */
		
		Utils.initTo(bb, Cards.Psyduck.ordinal(), CardFields.MOVE1);
		bb.put(water_1); /* Psyduck's Headache */
		
		Utils.initTo(bb, Cards.Golduck.ordinal(), CardFields.MOVE1);
		bb.put(water_1); /* Golduck's Psyshock */
		
		Utils.initTo(bb, Cards.SurfingPikachu1.ordinal(), CardFields.MOVE1);
		bb.put(light_2); /* Surfing Pikachu 1's Surf */
		
		Utils.initTo(bb, Cards.SurfingPikachu2.ordinal(), CardFields.MOVE1);
		bb.put(light_2); /* Surfing Pikachu 2's Surf */
	}
	
	/** Applies the randomization in the second byte buffer */
	static void doRandomization (ByteBuffer bbRead, ByteBuffer bbWrite) throws IOException {
		
                byte[] existingW, existingR;
                if (gui.getOption(Options.WR.ordinal()))
                {
                    int numSlots = 1;

                    if (gui.getWRRandomType() == wrRandomType.ByWRCombination)
                    {
                        //Randomize cards with the same original WR combination 
                        //to the same combination
                        numSlots = WRGroups.NUM_WR_COMB;
                    }
                    else if (gui.getWRRandomType() == wrRandomType.ByLine)
                    {
                        //Randomize cards from the same gen 1 main game 
                        //evolution line identically
                        numSlots = WRGroups.NUM_WR_LINES;
                    }

                    existingW = new byte[numSlots];
                    existingR = new byte[numSlots];
                    for (int idx = 0 ; idx < numSlots; idx++)
                    {
                        existingW[idx] = -1;
                        existingR[idx] = -1;
                    }
                }
                else
                {
                    existingW = new byte[1];
                    existingR = new byte[1];
                }
                
		for (int i = 0; i < Constants.NUM_POKEMON_CARDS; i++) {
			
			EvoTypes et = EvoTypes.values()[Cards.values()[i].getEvoType()];
			
			if (
                                gui.getOption(Options.HP.ordinal()) &&
                                !RandomizerLogic.isHPException(i)
                            ) RandomizerLogic.randomizeHP(bbWrite, i, et);          /* HP */
			if (gui.getOption(Options.WR.ordinal())) RandomizerLogic.randomizeWR(bbWrite, i, gui.getWRRandomType(), existingW, existingR);              /* Weakness & Resistance */
			if (gui.getOption(Options.RC.ordinal())) RandomizerLogic.randomizeRetreatCost(bbWrite, i, et); /* Retreat Cost          */
                        if (Cards.isIllusionCard(i) && 
                                gui.getIllusionCardAvailability() != Settings.illusionCardAvailability.cardPopOnly)
                        {
                            if (gui.getIllusionCardAvailability() == Settings.illusionCardAvailability.randomToSet)
                            {
                                RandomizerLogic.randomizeSet(bbRead, bbWrite, i);
                            }
                            else
                            {
                                RandomizerLogic.changeToPromo(bbWrite, i);
                            }
                        }
		}
		
		/* Moves */
		if (gui.getOption(Options.MOVES.ordinal())) {
			
			int[] grassArray     = RandomizerLogic.shuffleMoveArray(RandomizerLogic.getMovesAsIndexArray(bbRead, Bulbasaur,  Pinsir));
			int[] fireArray      = RandomizerLogic.shuffleMoveArray(RandomizerLogic.getMovesAsIndexArray(bbRead, Charmander, Moltres2));
			int[] waterArray     = RandomizerLogic.shuffleMoveArray(RandomizerLogic.getMovesAsIndexArray(bbRead, Squirtle,   Articuno2));
			int[] lightingArray  = RandomizerLogic.shuffleMoveArray(RandomizerLogic.getMovesAsIndexArray(bbRead, Pikachu1,   Zapdos3));
			int[] fightingArray  = RandomizerLogic.shuffleMoveArray(RandomizerLogic.getMovesAsIndexArray(bbRead, Sandshrew,  Aerodactyl));
			int[] psychicArray   = RandomizerLogic.shuffleMoveArray(RandomizerLogic.getMovesAsIndexArray(bbRead, Abra,       Mew3));
			int[] colorlessArray = RandomizerLogic.shuffleMoveArray(RandomizerLogic.getMovesAsIndexArray(bbRead, Pidgey,     Dragonite2));
		
			RandomizerLogic.applyMoveArrayOrder (bbRead, bbWrite, grassArray,     Bulbasaur);
			RandomizerLogic.applyMoveArrayOrder (bbRead, bbWrite, fireArray,      Charmander);
			RandomizerLogic.applyMoveArrayOrder (bbRead, bbWrite, waterArray,     Squirtle);
			RandomizerLogic.applyMoveArrayOrder (bbRead, bbWrite, lightingArray,  Pikachu1);
			RandomizerLogic.applyMoveArrayOrder (bbRead, bbWrite, fightingArray,  Sandshrew);
			RandomizerLogic.applyMoveArrayOrder (bbRead, bbWrite, psychicArray,   Abra);
			RandomizerLogic.applyMoveArrayOrder (bbRead, bbWrite, colorlessArray, Pidgey);
		}
	}
	
	/** Saves all changes to tcgrandomized.gbc */
	static void saveChangesToRom (FileChannel ch, ByteBuffer bbWrite) throws IOException {
		
		Utils.init(ch);
		bbWrite.rewind();
		ch.write(bbWrite);
	}

        /** Maximizes the text speed by setting the delay to 0 (5) in options.
            Also disables attack animations. */
	static void maxTextSpeedNoAnimations (RandomAccessFile f) throws IOException {
		
                //Jump to instructions for new save file generation
		f.seek(0x0199c0);
                 //set register A to 0
		f.writeByte(0xaf);
                //Set text delay of 0 to save file
                f.writeByte(0xea);
                f.writeByte(0x03);
                f.writeByte(0xa0); 
                //Set text delay of 0 to RAM
                f.writeByte(0xea);
                f.writeByte(0x47);
                f.writeByte(0xce); 
                //Increment register A to 1
                f.writeByte(0x3c); 
                //Set animation flag 1 to disable (1)
                f.writeByte(0xea);
                f.writeByte(0x07);
                f.writeByte(0xa0); 
                //Set animation flag 2 to disable (1)
                f.writeByte(0xea);
                f.writeByte(0x09);
                f.writeByte(0xa0); 
                //Decrement register A to 0 for later settings
                f.writeByte(0x3d); 
	}
        
        /** Changes the character to Mint from the Japan-only sequel. 
         * Used with permission from NikcDC.*/
        static void playAsMint (RandomAccessFile f) throws IOException {
            
            //Adjust default player name
            f.seek(0x128ee);
            f.writeByte(0x38); //I
            f.seek(0x128f0);
            f.writeByte(0x3d); //N
            f.seek(0x128f2);
            f.writeByte(0x43); //T
            
            //Game Text
            f.seek(0x4c9c7);
            f.writeBytes("er");
            f.seek(0x4cea1);
            f.writeBytes("er");
            f.seek(0x4f095);
            f.writeBytes("er");
            f.seek(0x51af4);
            f.writeBytes("You'll be the first girl I beat\non my new winning streak!");
            f.seek(0x526e3);
            f.writeBytes(". Sorry!        ");
            f.seek(0x52890);
            f.writeBytes("Boys don't take me seriously when we\nduel...  ");
            f.seek(0x529ac);
            f.writeBytes("!\nYou duel like a boy, hehe!        ");
            
            f.seek(0x7349d);
            f.writeShort(0xe665);
            
            //Graphics Data
            
            //Player Portrait
            f.seek(0x87d97);
            f.writeLong(0xff00fe00fe01fd03L);
            f.writeByte(0xe3);
            f.seek(0x87dA3);
            f.writeLong(0xff00906f6f989c86L);
            f.writeInt(0xe773fcff);
            f.writeByte(0xfe);
            f.seek(0x87db3);
            f.writeLong(0xff000ff0f1deee3dL);
            f.writeInt(0x2310df09);
            f.writeByte(0xfe);
            f.seek(0x87dC7);
            f.writeLong(0xff007f80bf40df60L);
            f.writeByte(0xaf);
            f.seek(0x87de3);
            f.writeLong(0xfe01fd01fd01fd02L);
            f.writeInt(0xfa02fa02);
            f.writeByte(0xfa);
            f.writeLong(0x1f1fffe0ff047f38L);
            f.writeLong(0x7c78f870f160f361L);
            f.writeLong(0x1f1fff07ff07ff0eL);
            f.writeLong(0xff0e7f7efffafff2L);
            f.writeLong(0xc5fefcf3f81ffc1fL);
            f.writeShort(0xfe1f);
            f.seek(0x87e1b);
            f.writeInt(0x1f3f1f7f);
            f.writeByte(0x37);
            f.writeLong(0xd03710f7689b48bbL);
            f.writeShort(0x689b);
            f.writeByte(0x04);
            f.seek(0x87e2c);
            f.writeInt(0xbcc5e6de);
            f.seek(0x87e37);
            f.writeLong(0xfc03fb04f70bcc34L);
            f.writeByte(0x3b);
            f.writeLong(0x03fa07f407f507f5L);
            f.writeLong(0x07f70fef0eee0cedL);
            f.writeLong(0xe7c3efc7ffcfffdfL);
            f.writeInt(0xffdf7f5e);
            f.seek(0x87e5d);
            f.writeShort(0x7f7f);
            f.writeByte(0x7d);
            f.writeLong(0xf7e2e7c6c787c7c7L);
            f.writeLong(0x6727c7c7e6666666L);
            f.writeLong(0xff64e7c4ffbfe784L);
            f.writeLong(0xc7823b3a7f4d4d4dL);
            f.writeLong(0xc5fded77e57f753fL);
            f.writeLong(0x773f3e1bbf199d0dL);
            f.writeInt(0xc8f72fdf);
            f.writeShort(0xf0f0);
            f.seek(0x87e97);
            f.writeShort(0xc780);
            f.seek(0x87e9a);
            f.writeInt(0xee715abd);
            f.writeShort(0xd42f);
            f.writeShort(0x08eb);
            f.seek(0x87ea3);
            f.writeByte(0xe7);
            f.seek(0x87eb0);
            f.writeLong(0x7f7c7f7c7f7c7774L);
            f.writeLong(0x737223aa23aa0797L);
            f.writeShort(0x6464);
            f.seek(0x87ec3);
            f.writeLong(0x600060e0a0d0c090L);
            f.writeInt(0x109800de);
            f.writeByte(0x1e);
            f.writeShort(0x4d0c);
            f.seek(0x87ed3);
            f.writeInt(0x0c000c0c);
            f.seek(0x87ed8);
            f.writeInt(0x1c3c0100);
            f.writeByte(0x01);
            f.seek(0x87ede);
            f.writeShort(0x0301);
            f.writeLong(0xde88df8bfe4afd4cL);
            f.writeLong(0xfdacfeeafeeafefaL);
            f.writeLong(0x1fe820dfa7d8747bL);
            f.writeInt(0x0e8f01f1);
            f.seek(0x87efd);
            f.writeByte(0xfe);
            f.seek(0x87f10);
            f.writeLong(0x07f50feb0ded0bebL);
            f.writeLong(0x03f307e718d830b7L);
            f.writeLong(0xce8ae4c4f0e0fdf0L);
            f.writeLong(0xf7ff030e03fe03feL);
            f.writeLong(0x07031f0d7f31fbc1L);
            f.writeLong(0xe301830103033c3cL);
            f.writeInt(0xfffcfffe);
            f.writeShort(0xfffe);
            f.writeByte(0x7f);
            f.seek(0x87f48);
            f.writeLong(0x9dfd6cfc8ebe1e7eL);
            f.seek(0x87f52);
            f.writeInt(0x007f007f);
            f.seek(0x87f5a);
            f.writeShort(0x80bf);
            f.seek(0x87f5d);
            f.writeByte(0x3f);
            f.seek(0x87f67);
            f.writeByte(0xff);
            f.seek(0x87f69);
            f.writeByte(0xff);
            f.seek(0x87f6b);
            f.writeInt(0xff00ff00);
            f.writeByte(0xfe);
            f.writeLong(0x0ccf02f30dcf31bfL);
            f.writeInt(0x427e417f);
            f.writeShort(0x517f);
            f.writeByte(0x88);
            f.seek(0x87f80);
            f.writeInt(0x03ff0efe);
            f.seek(0x87f85);
            f.writeLong(0x790c7f0efb1ff11fL);
            f.writeShort(0xff92);
            f.writeByte(0xf2);
            f.writeLong(0xc0c3003f00ff03ffL);
            f.writeLong(0x00ff00ff007f01ffL);
            f.writeByte(0x1e);
            f.seek(0x87fa2);
            f.writeInt(0x66fef3ff);
            f.writeByte(0x0c);
            f.seek(0x87fa8);
            f.writeByte(0x03);
            f.seek(0x87faa);
            f.writeByte(0x0c);
            f.seek(0x87fae);
            f.writeByte(0x60);
            f.seek(0x87fb5);
            f.writeLong(0x7f80bfc0df40df20L);
            f.writeShort(0xef20);
            f.writeByte(0xef);
            
            //Overworld Sprites
            f.seek(0x8be90);
            f.writeByte(0x00);
            f.seek(0x8be9a);
            f.writeInt(0x1b1c1f1f);
            f.writeShort(0x3f3d);
            f.writeInt(0x3f2a7f72);
            f.writeShort(0x7f78);
            f.seek(0x8bea7);
            f.writeByte(0x6f);
            f.seek(0x8beac);
            f.writeInt(0x0f0f0b0d);
            f.writeShort(0x0606);
            f.seek(0x8bebc);
            f.writeInt(0x1b1c1f1f);
            f.writeShort(0x3f2d);
            f.seek(0x8becc);
            f.writeInt(0xd838f8f8);
            f.writeLong(0xfcb43f2a7f727f78L);
            f.seek(0x8bed9);
            f.writeInt(0x371f130c);
            f.writeShort(0x0f07);
            f.writeByte(0x07);
            f.seek(0x8bee3);
            f.writeByte(0x56);
            f.seek(0x8beeb);
            f.writeByte(0xf8);
            f.seek(0x8beee);
            f.writeShort(0xc8b8);
            f.seek(0x8bef2);
            f.writeLong(0x000003030c0f101fL);
            f.writeInt(0x19171e1f);
            f.writeShort(0x333f);
            f.writeInt(0x353f7f7f);
            f.seek(0x8bf05);
            f.writeByte(0x3f);
            f.seek(0x8bf07);
            f.writeShort(0x6f7b);
            f.writeByte(0x4f);
            f.seek(0x8bf0c);
            f.writeInt(0x0f0f0d0b);
            f.writeShort(0x0606);
            f.seek(0x8bf18);
            f.writeShort(0x101f);
            f.seek(0x8bf1c);
            f.writeInt(0x1e1f131f);
            f.writeShort(0x353f);
            f.seek(0x8bf28);
            f.writeShort(0x08f8);
            f.seek(0x8bf2c);
            f.writeInt(0x78f8c8f8);
            f.writeShort(0xacfc);
            f.seek(0x8bf33);
            f.writeByte(0x7f);
            f.seek(0x8bf35);
            f.writeInt(0x7f3f3f1f);
            f.writeShort(0x1f13);
            f.seek(0x8bf3c);
            f.writeInt(0x0f0f0a0d);
            f.writeInt(0x0707fcfc);
            f.seek(0x8bf45);
            f.writeLong(0xfcf8f8f8e8f8c830L);
            f.writeShort(0xf0e0);
            f.writeByte(0xe0);
            f.seek(0x8bf5A);
            f.writeInt(0x6f71bfff);
            f.writeShort(0xffff);
            f.seek(0x8bf61);
            f.writeByte(0x1f);
            f.seek(0x8bf68);
            f.writeByte(0x9e);
            f.seek(0x8bf6a);
            f.writeByte(0xee);
            f.seek(0x8bf6d);
            f.writeByte(0xCA);
            f.seek(0x8bf6f);
            f.writeByte(0xa8);
            f.seek(0x8bf71);
            f.writeLong(0xa83f3d3f3e3f3f1fL);
            f.writeByte(0x1e);
            f.seek(0x8bf88);
            f.writeInt(0xa0e0e0e0);
            f.writeShort(0x40c0);
            f.seek(0x8bf9c);
            f.writeInt(0x6f71beff);
            f.writeShort(0xffff);
            f.seek(0x8bfaa);
            f.writeByte(0xbe);
            f.seek(0x8bfac);
            f.writeByte(0xee);
            f.seek(0x8bfaf);
            f.writeByte(0xCA);
            f.seek(0x8bfb1);
            f.writeByte(0xa8);
            f.seek(0x8bfb3);
            f.writeInt(0x1f3f3d3f);
            f.writeShort(0x3e3f);
            f.writeByte(0x3f);
            f.seek(0x8bfbb);
            f.writeInt(0x1d1f1917);
            f.writeByte(0x1f);
            f.writeShort(0x1e1e);
            f.seek(0x8bfc3);
            f.writeByte(0xa8);
            
            //Palettes (I think)
            f.seek(0x900e0);
            f.writeShort(0x0000);
            f.seek(0x901e0);
            f.writeShort(0x0605);
            f.seek(0x901f0);
            f.writeShort(0x10f0);
            f.seek(0xb3ffc);
            f.writeShort(0xe665);
            f.seek(0xb7b2d);
            f.writeShort(0xe665);
        }
	
	/** Turns the tutorial into a regular duel to prevent the player from possibly getting stuck */
	static void disablePracticeMode (RandomAccessFile f) throws IOException {
		
		f.seek(0x2b86);
		f.writeByte(0xaf);
	}
        
        /** Removes the tutorial battle entirely. Based on Sanqui's solution.
            Replaces cutscene instructions that would move NPCs, show options
            that explain terms, starts the practice battle, and other tasks
            with a command that says "do nothing". Used with permission. */
	static void removePracticeBattle (RandomAccessFile f) throws IOException {
		
		f.seek(0xd76f);
                for (int i = 0 ; i < 229 ; i++)
                {
                    f.writeByte(0x43);
                }
	}
	
	/** Fixes the global checksum */
	static void fixGlobalChecksum (FileChannel ch) throws IOException {
		
		ch.position(0);
		ByteBuffer rom = ByteBuffer.allocate(0x100000);
		ch.read(rom);
		
		int checksum = 0;
		for (byte b : rom.array())
			checksum += ((int) b) & 0xff;
		checksum -= 0xcc;
		
		ch.position(0x14e);
		byte[] cs = new byte[2];
		cs[0] = (byte) ((checksum >> 8) & 0xff);
		cs[1] = (byte) (checksum & 0xff);
		ch.write(ByteBuffer.wrap(cs));
	}
	
}
