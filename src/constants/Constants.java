package constants;

import constants.Fields.CardFields;
import constants.Fields.MoveFields;

public class Constants {

	public static final String FILE_NAME_IN  = "tcg.gbc";
	public static final String FILE_NAME_OUT = "tcgrandomized_";
        public static final String FILE_NAME_OUT_SUFFIX = "_.gbc";
	
        //ROM location of first Pokemon card following energy cards
	public static final int POKEMON_CARDS = 0x30e28;
        
        //Each card in the gsme is assigned a unique one-byte ID
	public static final int ENERGY_FIRST_ID  = 0x01;
	public static final int POKEMON_FIRST_ID = 0x08;
	public static final int TRAINER_FIRST_ID = 0xc3;
        
        //There are 228 unique cards, counting the two "illusion" cards
	public static final int NUM_CARDS         = 0xe4;
	public static final int NUM_POKEMON_CARDS = TRAINER_FIRST_ID - POKEMON_FIRST_ID;
	
        /*Each card type has a certain number of one-byte fields that represent 
          various attributes and behaviors. Likewise for individual moves.*/
	public static final int PKMN_CARD_DATA_LENGTH   = CardFields.END.getOffset() - CardFields.START.getOffset();
	public static final int PKMN_MOVE_DATA_LENGTH   = MoveFields.END.getOffset() - MoveFields.START.getOffset();
	public static final int TRN_CARD_DATA_LENGTH    = 0x0e;
	public static final int ENERGY_CARD_DATA_LENGTH = 0x0e;
        public static final int UNUSED_EFFECT_BEHAVIOR_START = 0x2ff03; //253 Bytes of extra effect code space
        public static final int UNUSED_EFFECT_BEHAVIOR_END = 0x2ffff;
        
}
