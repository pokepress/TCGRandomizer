package constants;

public class Fields {

        /** Each card has a certain number of one-byte fields that represent 
          various attributes and behaviors.*/
	public enum CardFields {
	
		START         (0),
		TYPE          (0),
		GFX           (1),
		NAME          (3),
		RARITY        (5),
                /** Set uses the upper 4 bits to represent the in-game set and 
                   the lower four bits to represent the real-world set:
                    Low Bits: 0-Base 1-Jungle 2-Fossil 7-GB
                    High Bits: 0-Colosseum 1-Evolution 2-Mystery 3-Laboratory
                    Grand Master Birds/Illusion Cards: 0x47
                    Promos: 0x48 (Rarity 0xff) */
		SET           (6), 
		ID            (7),
                //Multiple of 10 between 10 and 120
		HP            (8),
		STAGE         (9),
		PRE_EVO_NAME (10),
		MOVE1        (12),
		MOVE2        (31),
		RETREAT_COST (50),
		WEAKNESS     (51),
		RESISTANCE   (52),
		KIND         (53),
		POKEDEX      (55),
		DUMMY        (56),
		LEVEL        (57),
		LENGTH       (58),
		WEIGHT       (60),
		DESCRIPTION  (62),
		UNKNOWN      (64),
		END          (65);
	
		private final int offset;
	
		CardFields (int offset) {
			this.offset = offset;
		}
                
                /**Returns the byte offset for a particular card field.*/
		public int getOffset() {
			return offset;
		}

	}
	
        /** Each move has a certain number of one-byte fields that represent 
          various attributes and behaviors.*/
	public enum MoveFields {
		
		START         (0),
		ENERGY        (0),
		NAME          (4),
		DESCRIPTION   (6),
		DAMAGE       (10),
		CATEGORY     (11),
		EFFECT_CMDS  (12),
		FLAGS1       (14),
		FLAGS2       (15),
		FLAGS3       (16),
		UNKNOWN1     (17),
		UNKNOWN2     (18),
		END          (19);
	
		private final int offset;
	
		MoveFields (int offset) {
			this.offset = offset;
		}

                /**Returns the byte offset for a particular move field.*/
		public int getOffset() {
			return offset;
		}

	}	

}
