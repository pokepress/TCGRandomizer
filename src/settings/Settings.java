package settings;

public class Settings {

	public static final int NUM_OPTIONS = 10;
	
	public enum Options {
		HP, WR, RC, MOVES, FILL, MATCH, SPEED, REMOVETUTORIAL, CPUANTICHEAT,
                REBALANCETRAINERS;
	}
        
        /** List of possible methods for randomizing weakness and resistance. */
        public enum wrRandomType {
            Full,               //Each card is randomized individually
            ByWRCombination,    //Each original WR combination is randomized to the same value
            ByLine,             //Each gen 1 main game evolution line is randomized individually
            None                //No cards have weakness or resistance
        }
        
        /** List of possible player characters. */
        public enum playerCharacter {
            defaultMark,               //default character
            mint,                      //female character from gb2
            imakuni                    //Japanese celebrity
            //In theory, any character with a portrait and overworld sprite could be swapped.
        }
        
        /** List of possible coin graphics. */
        public enum coin {
            defaultPikachu,               //default coin
            grassMedal,
            fireMedal,
            waterMedal,
        }
        
        /** Controls availability of Mew and Venusaur, normally only available 
         * via Card Pop!.*/
        public enum illusionCardAvailability {
            cardPopOnly,                //Leave as Card Pop exclusive
            randomToSet,                //Randomly assign to one of four in-game sets
            treatAsPromo                //Change to promo
        }
	
	public static final Settings settings = new Settings(1, 1, 0, 1);
	
	public Settings (int minWeaknesses, int maxWeaknesses, int minResistances, int maxResistances) {
		this.minWeaknesses = minWeaknesses;
		this.maxWeaknesses = maxWeaknesses;
		this.minResistances = minResistances;
		this.maxResistances = maxResistances;
                this.wrRandomizationType = wrRandomType.ByWRCombination;
                this.customSeed = "";
                this.playerChar = playerCharacter.defaultMark;
                this.playerCoin = coin.defaultPikachu;
                this.illusionCardAvail = illusionCardAvailability.cardPopOnly;
	}
	
	private int minWeaknesses;
	private int maxWeaknesses;
	private int minResistances;
	private int maxResistances;
        private wrRandomType wrRandomizationType;
        private playerCharacter playerChar;
        private coin playerCoin;
        private illusionCardAvailability illusionCardAvail;
        
        private String customSeed;
	
	public int getMinWeaknesses() {
		return minWeaknesses;
	}
	
	public void setMinWeaknesses(int minWeaknesses) {
		this.minWeaknesses = minWeaknesses;
	}
	
	public int getMaxWeaknesses() {
		return maxWeaknesses;
	}
	
	public void setMaxWeaknesses(int maxWeaknesses) {
		this.maxWeaknesses = maxWeaknesses;
	}
	
	public int getMinResistances() {
		return minResistances;
	}
	
	public void setMinResistances(int minResistances) {
		this.minResistances = minResistances;
	}
	
	public int getMaxResistances() {
		return maxResistances;
	}
	
	public void setMaxResistances(int maxResistances) {
		this.maxResistances = maxResistances;
	}
        
        public wrRandomType getWRRandomizationType() {
		return wrRandomizationType;
	}
        
        public void setWRRandomizationType(wrRandomType wrRandomizationType) {
		this.wrRandomizationType = wrRandomizationType;
	}
        
        public void setCustomSeed(String customSeed) {
		this.customSeed = customSeed;
	}
        
        public String getCustomSeed() {
		return customSeed;
	}
        
        public playerCharacter getPlayerChar() {
		return playerChar;
	}
        
        public void setPlayerChar(playerCharacter character) {
		this.playerChar = character;
	}
        
        public coin getCoin() {
		return playerCoin;
	}
        
        public void setCoin(coin coin) {
		this.playerCoin = coin;
	}
        
        public illusionCardAvailability getIllusionCardAvailability() {
		return illusionCardAvail;
	}
        
        public void setIllusionCardAvailability(illusionCardAvailability availability) {
		this.illusionCardAvail = availability;
	}
        
}
