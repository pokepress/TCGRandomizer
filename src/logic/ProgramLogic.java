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
                                RandomizerLogic.changeIllusionToPromo(bbWrite, i);
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
        
        /** Performs code tweaks needed to insert illusion cards into
         Challenge Cup prizes*/
        static void addIllusionToCup (RandomAccessFile f) throws IOException {
		//Increase possible randomized cards from 13 to 15
		f.seek(0xd1bd);
                f.writeByte(0x0f);
                //Remove offset so we can re-use during-game cup promos
                f.seek(0xd1c2);
                f.writeByte(0x00);
                //Overwrtie duplicate Mewtwo Lv 60 with Venusaur Lv 64
                f.seek(0xd1fa);
                f.writeByte(0x0a);
                f.writeShort(0x1408);
                //Overwrtie duplicate Mew Lv 8 with Mew Lv 15
                f.seek(0xd1fd);
                f.writeByte(0xa1);
                f.writeShort(0xca0a);
	}
        
        /** "Boss" opponents (Club Masters, Grand Masters, and Ronald) are 
         * able to manipulate their opening hands to ensure that they have
         * a certain number of basics, energy, etc.. This disables that by
         * returning immediately. */
	static void disableBossSetupCheat(RandomAccessFile f) throws IOException {
		
		f.seek(0x172af);
                f.writeByte(0xc9);
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
