package logic;

import java.io.IOException;
import java.nio.ByteBuffer;

import constants.Cards;
import constants.Constants;
import constants.Fields.CardFields;
import constants.Fields.MoveFields;
import gui.GUIController;
import java.io.RandomAccessFile;
import settings.EvoTypes;
import settings.Settings;
import settings.Settings.Options;
import settings.Settings.wrRandomType;
import utils.RNG;
import utils.Utils;

class RandomizerLogic {
	
	private static final GUIController gui = GUIController.getGuiController();

	RandomizerLogic() {}
	
	/** Default values:<br>
	 * 	Evolution 1 of 1 --> Between 50 and  80 HP<br>
	 *  Evolution 1 of 2 --> Between 30 and  60 HP<br>
	 *  Evolution 2 of 2 --> Between 60 and  90 HP<br>
	 *  Evolution 1 of 3 --> Between 30 and  50 HP<br>
	 *  Evolution 2 of 3 --> Between 50 and  70 HP<br>
	 *  Evolution 3 of 3 --> Between 70 and 120 HP<br>  */
	static void randomizeHP (ByteBuffer bbWrite, int i, EvoTypes et) throws IOException {
		
		Utils.initTo(bbWrite, i, CardFields.HP);
		bbWrite.put(RNG.randomRangeScale(et.getMinHP(), et.getMaxHP(), 10));
	}
        
        /** Returns true if the card is exempted from HP randomization.*/
        static boolean isHPException(int cardIndex)
        {
            if(cardIndex == constants.Cards.MrMime.ordinal())
            {
                //Mr. Mime's Invisible Wall ability makes it not be able to take
                //more than 20 damage per turn under normal circumstances. To
                //avoid a severely annoying card, we won't randomize its HP. 
                return true;
            }
            return false;
        }
	
	/** Randomizes weakness and resistance based on the settings the user 
         * chose. Default is 1 weakness, and 0 or 1 resistances (50% each). */
	static void randomizeWR (ByteBuffer bbWrite, int i, Settings.wrRandomType randomType, byte[] existingW, byte[] existingR) throws IOException {
		Utils.initTo(bbWrite, i, CardFields.WEAKNESS);

                switch(randomType)
                {
                    case Full: //Randomize each card from scratch
                        bbWrite.put(RNG.randomWR(
                                        Settings.settings.getMinWeaknesses(), Settings.settings.getMaxWeaknesses(),
                                        Settings.settings.getMinResistances(), Settings.settings.getMaxResistances())
                                        );
                        break;
                    case ByWRCombination:
                    case ByLine:
                        int rwIdx = 0;

                        if (randomType == wrRandomType.ByWRCombination)
                        {
                            //Randomize cards with the same original WR 
                            //combination to the same combination
                            rwIdx = Cards.values()[i].getWRComb();
                        }
                        else if (randomType == wrRandomType.ByLine)
                        {
                            //Randomize cards from the same gen 1 main game 
                            //evolution line identically
                            rwIdx = Cards.values()[i].getWRLine();
                        }

                        if(existingW[rwIdx] == -1 || existingR[rwIdx] == -1)
                        {
                            //Haven't seen this combination yet. Create new value.
                             byte[] newWR = RNG.randomWR(
                                        Settings.settings.getMinWeaknesses(), Settings.settings.getMaxWeaknesses(),
                                        Settings.settings.getMinResistances(), Settings.settings.getMaxResistances());
                             existingW[rwIdx] = newWR[0];
                             existingR[rwIdx] = newWR[1];
                             bbWrite.put(newWR);
                        }
                        else
                        {
                            //Already has been determined. Re-use prior value.
                            byte[] newWR= new byte[2];
                            newWR[0] = existingW[rwIdx];
                            newWR[1] = existingR[rwIdx];
                            bbWrite.put(newWR);
                        }
                        break;
                    case None:
                    default:
                        byte[] newWR= new byte[2];
                        bbWrite.put(newWR);
                        break;
        	}
        }
	
	/** Default values:<br>
	 *  Evolution 1 of 1 --> Between 1 and 3 retreat cost<br>
	 *  Evolution 1 of 2 --> Between 0 and 1 retreat cost<br>
	 *  Evolution 2 of 2 --> Between 1 and 3 retreat cost<br>
	 *  Evolution 1 of 3 --> Between 0 and 1 retreat cost<br>
	 *  Evolution 2 of 3 --> Between 1 and 2 retreat cost<br>
	 *  Evolution 3 of 3 --> Between 2 and 3 retreat cost<br>  */
	static void randomizeRetreatCost (ByteBuffer bbWrite, int i, EvoTypes et) throws IOException {
		
		Utils.initTo(bbWrite, i, CardFields.RETREAT_COST);	
		bbWrite.put(RNG.randomRange(et.getMinRC(), et.getMaxRC()));
	}
	
	/** @return the number of energies required to use move in position mn of Pokemon card i starting from typeOffset,
	 *  or -1 if said move is empty. */
	static int howManyEnergies (ByteBuffer bbRead, int i, int mn) throws IOException {
		
		Utils.initTo(bbRead, i, MoveFields.ENERGY, mn);		
		return Utils.addNybbles(bbRead.getInt()) + ((bbRead.getInt() != 0) ? 0 : -1);
	}
	
	/** Maps the moves from Pokemon cards between first and last into an integer array:<br>
	 *  -->  0 if move is a Pokemon power, or if it's empty and "fill empty moveslots" is not selected<br>
	 *  --> -1 if move is empty, and "fill empty moveslots" is selected<br>
	 *  Else:<br>
	 *  --> Bits 0-7: Move index<br>
	 *  --> Bits 8-9: Number of energies */
	static int[] getMovesAsIndexArray (ByteBuffer bbRead, Cards first, Cards last) throws IOException {
		
		Utils.initTo(bbRead, first.ordinal(), CardFields.START);
		int[] indexArray = new int[2 * (last.ordinal() + 1 - first.ordinal())];
		
		for (int pos = 0, curCard = 0, moveNumber = 0 ; pos < indexArray.length ; pos ++) {
			
			indexArray[pos] = pos;
			
			switch (howManyEnergies (bbRead, first.ordinal() + curCard, moveNumber)) {
			
			case -1: /* Ignore (return 0) if "Fill empty moveslots" isn't selected */
				indexArray[pos] = (gui.getOption(Options.FILL.ordinal()) ? -1 : 0);
				break;
			case 0:
				indexArray[pos] = 0;
				break;
			case 1:
				indexArray[pos] |= 1 << 8;
				break;
			case 2:
				indexArray[pos] |= 2 << 8;
				break;
			default: /* Moves that require 3 and 4 energies are treated equally */
				indexArray[pos] |= 3 << 8;
			}
			
			curCard += moveNumber;
			moveNumber ^= 1;
		}
		
		return indexArray;
	}
	
	/** Shuffles the array of move indexes across same type Pokemon cards accounting for energy requirements.<br>
	 *  Fills indexes corresponding to empty moveslots if "fill empty moveslots" is selected. */
	static int[] shuffleMoveArray (int[] indexArray) {
		
		/* Shuffle move array */
		for (int curIndex = 0, randomIndex = 0, temp = 0 ; curIndex < indexArray.length ; 
				randomIndex = RNG.randomRange(0, indexArray.length - 1)) {
			
			/* Ignore if Pokemon Power or empty moveslot */
			if (indexArray[curIndex] == 0 || indexArray[curIndex] == -1) {
				curIndex ++;
				
			} else {
			
				if ((indexArray[curIndex] & 0xf00) == (indexArray[randomIndex] & 0xf00)) /* Compare energies */ {			
					if (curIndex != randomIndex) {
			
						temp = indexArray[curIndex];
						indexArray[curIndex] = indexArray[randomIndex];
						indexArray[randomIndex] = temp;
						curIndex ++;
					}
				}
			}		
		}
		
		/* Fill empty moveslots with a move that requires 1 or 2 energy */
		for (int curIndex = 0, randomIndex = 0 ; curIndex < indexArray.length ; 
				randomIndex = RNG.randomRange(0, indexArray.length - 1)) {
			
			if ((indexArray[randomIndex] & 0xf00) == 0x100 || (indexArray[randomIndex] & 0xf00) == 0x200) {
				if (randomIndex != curIndex - 1) /* Make sure it's not a move it already knows */ {
				
					if (indexArray[curIndex] == -1) {
						indexArray[curIndex] = indexArray[randomIndex];
					}
					curIndex ++;
				}
			}
			
		}
		return indexArray;
	}
	
	/** Applies the result of ShuffleMoveArray to the actual Pokemon card move data in the ByteBuffer */
	static void applyMoveArrayOrder (ByteBuffer bbRead, ByteBuffer bbWrite, int[] indexArray, Cards start) throws IOException {
		
		for (int i = 0 ; i < indexArray.length ; i ++) {
			
			if (indexArray[i] != 0) {
			
				indexArray[i] = indexArray[i] & 0xff;
				CardFields moveField;
			
				/* Init position of destination buffer for current iteration */
				if ((i & 1) == 0)
					moveField = CardFields.MOVE1;
				else
					moveField = CardFields.MOVE2;			
				Utils.initTo(bbWrite, start.ordinal() + i/2, moveField);
			
				/* Init position of origin buffer for current iteration */
				if ((indexArray[i] & 1) == 0)
					moveField = CardFields.MOVE1;
				else
					moveField = CardFields.MOVE2;			
				Utils.initTo(bbRead, start.ordinal() + indexArray[i]/2, moveField);
			
				/* Apply change */
				bbWrite.put(bbRead.array(), bbRead.position(), Constants.PKMN_MOVE_DATA_LENGTH);
			}
		}
	}
        
        /** Generates a random set number. Upper nybble controls in-game set,
            lower nybble controls real-world set icon.*/
        static void randomizeSet (ByteBuffer bbRead, ByteBuffer bbWrite, int i) throws IOException {
                Utils.initTo(bbRead, i, CardFields.SET);
                byte cardSet = bbRead.get(); 
                cardSet = (byte) (cardSet & 0x0f); //keep lower nybble (real-world set)
                cardSet += (byte) RNG.randomRangeScale(0, 3, 16); //Randomize upper nybble (in-game set)
		Utils.initTo(bbWrite, i, CardFields.SET);
		bbWrite.put(cardSet);
	}
        
        /** Turns the card into a promo card. See ProgramLogic.addIllusionToCup
         for how we actually make them available.*/
        static void changeIllusionToPromo (ByteBuffer bbWrite, int i) throws IOException {
		Utils.initTo(bbWrite, i, CardFields.RARITY);
		bbWrite.put((byte) 0xff); //Dedicated Promo Rarity (no icon)
                Utils.initTo(bbWrite, i, CardFields.SET);
                bbWrite.put((byte) 0x48); //Promo Set (used for challenge cups)
	}
        
        /**Randomizes requirements for Club Masters.**/
        static void randomizeCMReq (RandomAccessFile f) throws IOException {
		
                /*Isaac and Brandon's scripts check events x25, x26, x27*/
                int isaacReq =  RNG.randomRange(0, 7);
                
                if (isaacReq % 2 == 1)
                {
                    //Remove event x25 (Jennifer) check
                    f.seek(0xe4ae);
                    f.writeInt(0x43434343);
                    f.seek(0xe457);
                    f.writeInt(0x43434343);
                }
                if ((isaacReq & 2) == 2)
                {
                    //Remove event x26 (Nicholas) check
                    f.seek(0xe4b2);
                    f.writeInt(0x43434343);
                    f.seek(0xe45b);
                    f.writeInt(0x43434343);
                }
                if ((isaacReq & 4) == 4)
                {
                    //Remove event x27 (Brandon) check
                    f.seek(0xe4b6);
                    f.writeInt(0x43434343);
                    f.seek(0xe45f);
                    f.writeInt(0x43434343);
                }
                
                int joshuaReq =  RNG.randomRange(0, 3);
                if (joshuaReq % 2 == 1)
                {
                    //Remove event x15 (Sara) check
                    f.seek(0xe221);
                    f.writeInt(0x43434343);
                    
                    if ((joshuaReq & 2) == 0)
                    {
                        //Still need to battle Amanda
                        f.seek(0x42485);
                        f.writeBytes(String.format("%1$-22s","Amanda first."));
                        f.seek(0x4251c);
                        f.writeBytes(String.format("%1$-16s","Amanda?"));
                        f.seek(0x42556);
                        f.writeBytes(String.format("%1$-18s","Amanda..."));
                    }
                }
                if ((joshuaReq & 2) == 2)
                {
                    //Remove event x16 (Amanda) check
                    f.seek(0xe21d);
                    f.writeInt(0x43434343);
                    
                    if (joshuaReq % 2 == 0)
                    {
                        //Still need to battle Sara
                        f.seek(0x42485);
                        f.writeBytes(String.format("%1$-22s","Sara first."));
                        f.seek(0x4251c);
                        f.writeBytes(String.format("%1$-16s","Sara?"));
                        f.seek(0x42556);
                        f.writeBytes(String.format("%1$-18s","Sara..."));
                    }
                }
                if(joshuaReq == 3)
                {
                    /*No required battles for Joshua-remove references to Sara
                    and Amanda*/
                    f.seek(0x4247b);
                    f.writeBytes(String.format("%1$-32s","Then..."));
                    f.seek(0xe239);
                    f.writeShort(0x4343);
                    f.writeByte(0x43);
                    f.seek(0xe241);
                    f.writeByte(0x02);
                    f.writeShort(0x3e04);
                    f.writeShort(0x4343);
                }
            
                byte murrayBadges = (byte) RNG.randomRange(0, 7);
		f.seek(0xeae5); //text comparison value
                f.writeByte(murrayBadges);
                f.seek(0xead8); //sprite position comparison value
                f.writeByte(murrayBadges);
                
                if (RNG.randomRange(0, 1) == 1)
                {
                    /*To allow direct access to Rick, we need to move Joshua.*/
                    f.seek(0xecc4); //Joshua's loading code
                    f.writeLong(0x3e1b0e01cd924a00L); //set event x1b when we enter room
                }
                
                short kenCards = (short) RNG.randomRangeShort(0, 500);
                f.seek(0xef2a);
                f.writeShort(Utils.swapAddressBytes(kenCards));
	}
        
        /**Randomizes medal requirement for beating the game.**/
        static void randomizeMedalReq (RandomAccessFile f) throws IOException {
            byte medalReq =  (byte) RNG.randomRange(6, 8);
            if(medalReq == 8)
            {
                return;
            }
            
            //Programmatic changes
            f.seek(0xf6b1); //Change script instruction to allow medal counts above the requirement
            f.writeByte(0x5d);
            f.seek(0xf6b3); //requirement for Pokemon Dome doors
            f.writeByte(medalReq);
            f.seek(0xf654); //count where ronald stops appearing when reading plaque
            f.writeByte(medalReq-1);
            
            //Text changes
            f.seek(0x40bef); //Science Club letter from Dr. Mason
            f.writeBytes("the ");
            f.writeByte(medalReq | 0x30); //Convert to character
            f.seek(0x47392); //Plaque in Pokemon Dome
            f.writeByte(medalReq | 0x30); //Convert to character
            f.writeBytes(" TCG");
            f.seek(0x473ad);
            f.writeBytes("ir");
            
            f.seek(0x48dcb); //Dome Door: Fail
            f.writeByte(medalReq | 0x30); //Convert to character
            f.writeBytes(" / 8");
            f.seek(0x48e0c);
            f.writeBytes("enough ");
            
            f.seek(0x48e4f); //Dome Door: Proceed
            f.writeByte(medalReq | 0x30); //Convert to character
            f.writeBytes(" / 8");
            f.seek(0x48e8c);
            f.writeByte(medalReq | 0x30); //Convert to character
            f.writeBytes(" / 8");
            
            f.seek(0x491fc); //Dueling stage message
            f.writeBytes("those");
            
            f.seek(0x4e9fe); //Ronald's introduction
            f.writeByte(medalReq | 0x30); //Convert to character
            f.writeBytes(" / 8");
            f.seek(0x4ea1c);
            f.writeBytes("ir");
        }

}
