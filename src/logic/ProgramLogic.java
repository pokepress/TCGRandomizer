package logic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static constants.Cards.*;
import constants.Cards;
import constants.Constants;
import constants.Fields;
import constants.Fields.CardFields;
import constants.Fields.MoveFields;
import constants.WRGroups;
import gui.GUIController;
import settings.EvoTypes;
import settings.Settings;
import settings.Settings.Options;
import settings.Settings.wrRandomType;
import utils.Utils;

class ProgramLogic {
		
	private static final GUIController gui = GUIController.getGuiController();
        private static final int bytesToAddFlip = 11; //Number of bytes needed to add a coin flip to an effect
        private static final int effectFreeSpaceStart = 0x2ff03;

	ProgramLogic() {}
	
	/** Makes sure that tcg.gbc is actually a tcg ROM 
	  * @return true if header data matches, false otherwise */
	static boolean verifyRom (FileChannel chin) throws IOException {
		
		byte[] header = {0x50, 0x4F, 0x4B, 0x45, 0x43, 0x41, 0x52, 0x44, 0x00, 0x00, 0x00, 0x41, 0x58, 0x51, 0x45, 
				(byte) 0x80, 0x30, 0x31, 0x03, 0x1B, 0x05, 0x03, 0x01, 0x33, 0x00, 0x34, 0x26, (byte) 0xA6};
		
		ByteBuffer headerin = ByteBuffer.allocate(header.length);
		chin.read(headerin, 0x134);
		
            return ((ByteBuffer) headerin.rewind()).compareTo(ByteBuffer.wrap(header)) == 0;
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
        
        static void rebalanceAttackCosts (ByteBuffer bb) throws IOException {
            byte[] fight_1 = {0x00, 0x00, 0x10, 0x00};
            byte[] grass_1 = {0x01, 0x00, 0x00, 0x00};
            
            Utils.initTo(bb, Cards.Marowak1.ordinal(), CardFields.MOVE2);
            bb.put(fight_1); /* Marowak 1's Call for Friend */
            
            Utils.initTo(bb, Cards.NidoranF.ordinal(), CardFields.MOVE2);
            bb.put(grass_1); /* Nidoran Female's Call for Family */
            
            Utils.initTo(bb, Cards.Oddish.ordinal(), CardFields.MOVE2);
            bb.put(grass_1); /* Oddish's Sprout */
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
        
        /** Weakens Gust of Wind by requiring the user to flip heads for it to
         work.*/
        static int nerfGustOfWind(RandomAccessFile f, int effectStartAddress) throws IOException
        {
            if (effectStartAddress + bytesToAddFlip - 1 > constants.Constants.UNUSED_EFFECT_BEHAVIOR_END)
            {
                throw new IOException("Unable to edit Gust of Wind due to lack of effect codespace.");
            }
            //Alter card name to clue in player.
            f.seek(0x643c3);
            f.writeBytes("PKMN Catcher");
            //Update card text.
            f.seek(0x643d1);
            f.writeBytes("Flip a coin. If heads, switch your\nopponent's active Pok`mon.");
            f.writeByte(0x00); //Terminate String
            f.seek(0x18f99); //overwrite address for "damage" phase of card execution
            short bank1Address = utils.Utils.cartAddressToBank1Address(effectStartAddress);
            f.writeShort(utils.Utils.swapAddressBytes(bank1Address));
            //Flips a coin, if heads, proceeds to normal "damage" phase at 7e90
            f.seek(effectStartAddress);
            f.writeLong(0x11ef00cd8a40d0cdL);
            f.writeShort(0x907e);
            f.writeByte(0xc9);
            return (int)f.getFilePointer();
        }
        
        /** Weakens Energy Removal by requiring the user to flip heads for it to
         work.*/
        static int nerfEnergyRemoval(RandomAccessFile f, int effectStartAddress) throws IOException
        {
            if (effectStartAddress + bytesToAddFlip - 1 > constants.Constants.UNUSED_EFFECT_BEHAVIOR_END)
            {
                throw new IOException("Unable to edit Energy Removal due to lack of effect codespace.");
            }
            //Alter card name to clue in player.
            f.seek(0x63d28);
            f.writeBytes("Crushing Hamr.");
            f.writeByte(0x00); //Terminate String
            //Update card text.
            f.seek(0x63d38);
            f.writeBytes("Flip. If heads, discard an Energy\ncard from 1 of your opponent's\nPok`mon.");
            f.writeByte(0x00); //Terminate String
            f.seek(0x18e87); //overwrite address for "damage" phase of card execution
            short bank1Address = utils.Utils.cartAddressToBank1Address(effectStartAddress);
            f.writeShort(utils.Utils.swapAddressBytes(bank1Address));
            f.seek(0x2f276);
            //Flips a coin, if heads, proceeds to normal "damage" phase at 7e90
            f.seek(effectStartAddress);
            f.writeLong(0x11ef00cd8a40d0cdL);
            f.writeShort(0x7372);
            f.writeByte(0xc9);
            return (int)f.getFilePointer();
        }
        
        /** Weakens Super Energy Removal by making it a 1-to-1 exchange.*/
        static void nerfSuperEnergyRemoval(RandomAccessFile f) throws IOException
        {
            //Alter card name to clue in player.
            f.seek(0x63d87);
            f.writeBytes("Equal");
            //Update card text.
            f.seek(0x63e09);
            f.writeBytes("1 Energy card attached to it.\nDiscard that Energy card.");
            f.writeByte(0x00); //Terminate String
            //Update UI numbers
            f.seek(0x2fd26);
            f.writeByte(0x01);
            f.seek(0x2fd40);
            f.writeByte(0x01);
            //Alter comparison value so process stops after 1 energy is selected
            f.seek(0x2fd5a);
            f.writeByte(0x04);
            //Prevent multiple energies fron being removed by CPU
            f.seek(0x2fd86);
            f.writeShort(0x0000);
        }
        
        /** Makes Gambler safer by switching between 7 and 4 instead of 8 and 1.*/
        static void makeGamblerSafer(RandomAccessFile f) throws IOException
        {
            //Update flip text
            f.seek(0x37f3b);
            f.writeBytes("7");
            f.seek(0x37f4e);
            f.writeBytes("4.");
            f.writeByte(0x00); //Terminate String
            //Alter card name to clue in player.
            f.seek(0x648ee);
            f.writeBytes("Birch O"); //Based on Birch's Observations
            //Update card text.
            f.seek(0x64935);
            f.writeBytes("7");
            f.seek(0x6494d);
            f.writeBytes("4.");
            f.writeByte(0x00); //Terminate String
            //Change draw quantities
            f.seek(0x2f423);
            f.writeByte(0x07);
            f.seek(0x2f42a);
            f.writeByte(0x04);
        }
        
        /** Makes Professor Oak less powerful by drawing 5 cards instead of 7.*/
        static void nerfProfessorOak(RandomAccessFile f) throws IOException
        {
            //Alter card name to clue in player.
            f.seek(0x6342e);
            //No clever name yet.
            //Update card text.
            f.seek(0x6345a);
            f.writeBytes("5");
            f.seek(0x6494d);
            //Change draw quantities
            f.seek(0x2f3b8);
            f.writeByte(0x05);
            f.seek(0x2f3bd);
            f.writeByte(0x05);
        }
        
        /** Generifies call for family moves by making them work like
         Marowak's "Call For Friend" by looking for a card with a type 
         matching the attacking Pokemon.*/
        static void fixCallForFamily(RandomAccessFile f, ByteBuffer bb) throws IOException
        {
            //Use the search parameter to specify type for basic search
            f.seek(0x2c35a);
            f.writeByte(0xbb);
            f.writeByte(0x00);
            
            //Copy code from Marowak
            f.seek(0x2e110);
            byte[] callBytes = new byte[132];
            f.read(callBytes, 0 , 132);
            
            //Change Krabby
            
            //Change type to water, text pointers
            callBytes[8] = 0x22;
            callBytes[11] = 0x28;
            callBytes[14] = 0x03;
            callBytes[24] = 0x28;
            callBytes[44] = 0x03;
            callBytes[82] = 0x03;
            callBytes[122] = 0x03;
            f.seek(0x2cf6d); //start of player action code
            f.write(callBytes);
            
            //Adjust command ponters
            f.seek(0x188c4); //After selection
            f.writeShort(0x9461);
            f.seek(0x188ca); //AI commands
            f.writeByte(0xd4);
            
            //Adjust text (limited space compared to grass)
            f.seek(0x388ee);
            f.writeByte(0x06);
            f.writeBytes("Basic water Pk ");
            
            f.seek(0x38a02);
            f.writeByte(0x06);
            f.writeBytes("Choose a Basic.");
            f.writeByte(0x00);//terminate string
            
            f.seek(0x5bd89);
            f.writeShort(0x0504); // water symbol
            f.writeByte(0x0a);
            f.writeBytes("Pok`mon and put it onto your Bench.");
            f.writeByte(0x0a);
            f.writeBytes("Shuffle your deck afterward. (You");
            f.writeByte(0x0a);
            f.writeBytes("can't use this attack if your Bench");
            f.writeByte(0x0a);
            f.writeBytes("is full.)");
            f.writeByte(0x00);//terminate string
            
            //Change Marowak
            f.seek(0x2e11e);
            f.writeByte(0x04); //Pass Fighting as parameter
            
            //Change Grass Pokemon (Could be more efficient in terms of space)
            
            byte[] nidoranCffDesc = {0x4e, 0x08};
            
            //Change type to grass, text pointers
            callBytes[8] = 0x2a;
            callBytes[11] = 0x28;
            callBytes[14] = 0x01;
            callBytes[24] = 0x28;
            callBytes[44] = 0x01;
            callBytes[82] = 0x01;
            callBytes[122] = 0x01;
            
            //Oddish
            f.seek(0x2c85a); //start of player action code
            f.write(callBytes);
            
            //Adjust command ponters
            f.seek(0x1878e); //After selection
            f.writeShort(0x9461);
            f.seek(0x18794); //AI commands
            f.writeByte(0xc1);
            
            //Point to shared attack description
            Utils.initTo(bb, Cards.Oddish.ordinal(), MoveFields.DESCRIPTION, 1);
            bb.put(nidoranCffDesc);
            
            //Bellsprout
            f.seek(0x2cc50); //start of player action code
            f.write(callBytes);
            
            //Adjust command ponters
            f.seek(0x18839); //After selection
            f.writeShort(0x9461);
            f.seek(0x1883f); //AI commands
            f.writeByte(0xb7);
            
            //Point to shared attack description
            Utils.initTo(bb, Cards.Bellsprout.ordinal(), MoveFields.DESCRIPTION, 1);
            bb.put(nidoranCffDesc);
            
            //Nidoran (f)
            f.seek(0x2c9eb); //start of player action code
            f.write(callBytes);
            
            //Adjust command ponters
            f.seek(0x187cb); //After selection
            f.writeShort(0x9461);
            f.seek(0x187d1); //AI commands
            f.writeByte(0x52);
            
            //Alter the Nidoran action text and use that
            f.seek(0x38a3b);
            f.writeBytes("basic");
            f.writeByte(0x0a);
            f.writeBytes("grass Pok`mon.");
            f.writeByte(0x00);//terminate string
            
            //Alter attack description
            f.seek(0x58370);
            f.writeShort(0x0502); // grass symbol
            f.writeByte(0x0a);
            f.writeBytes("Pok`mon and put it onto your Bench.");
            f.writeByte(0x0a);
            f.writeBytes("Shuffle your deck afterward. (You");
            f.writeByte(0x0a);
            f.writeBytes("can't use this attack if your Bench");
            f.writeByte(0x0a);
            f.writeBytes("is full.)");
            f.writeByte(0x00);//terminate string
        }
        
        /**Changes coin flip logic to support a randomized probability. Affects
         both players. Ranges from 33% heads to 67% heads.*/
        static void randomizeFlipProbability(RandomAccessFile f) throws IOException
        {
            //Assumes: Register E has a value of 0
            f.seek(0x7246);
            //Add a number to register A instead of performing bitwise rotation
            f.writeByte(0xc6); 
            //The larger the number, the more likely the value is to carry,
            //so larger numbers reduce the heads probability
            f.writeByte(utils.RNG.randomRange(85, 171));
            
            //reposition subsequent operations
            f.writeShort(0x3803); //Jump 3 opcodes if carry flag is set (results in tails)
            f.writeShort(0x1659); //Copy x59 to register D (animation number)
            f.writeByte(0x1c); //Increment register E to 1 (results in heads)
        }
        
        /** Replaces the first part of the intro text with seed and config info.
         I would have preferred to display this values during the title screen
         animation or new game menu, but this was logistically easier and still
         appears before the player sees any cards.*/
        static void addConfigToIntro(RandomAccessFile f) throws IOException
        {
            //Including the minus sign, a 64-bit long should max out at 20 digits
            f.seek(0x3efc4);
            f.writeBytes("S: " + String.format("%1$-21s", gui.getSeed()));
            f.seek(0x3efdd);
            String config=gui.getConfigString(true);
            /*We're curruntly limited by the number of characters used for each 
            line of the intro, so showing the full configuration may not be
            possible in the future without significant rework.*/
            if (config.length() > 27)
            {
                config = config.substring(0, 24) + "...";
            }
            else if(config.length() < 27)
            {
                config = String.format("%1$-27s", config);
            }
            f.writeBytes("C: " + config );
            f.writeByte(0x0a);
            f.writeBytes("1");
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
