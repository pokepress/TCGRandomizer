package utils;

import java.util.Random;

public class RNG {
	
	private static final Random rnd = new Random();

	public RNG() {}
        
        /** Set the seed value for randomization 
         * @param newSeed new seed value */
        public static void setSeed(long newSeed)
        {
            rnd.setSeed(newSeed);
        }
        
        /** Generates and returns a new seed value 
         * @return new seed value */
        public static long getSeed()
        {
            long seed = rnd.nextLong(); //Generate value for seed field in GUI
            rnd.setSeed(seed);
            return seed;
        }
		
	/** @return a random number between min and max inclusive */
	public static byte randomRange (int min, int max) {
		return (byte) randomInRange(min, max);
	}
        
        /** @return a larger random number between min and max inclusive */
	public static short randomRangeShort (int min, int max) {
		return (short) randomInRange(min, max);
	}
        
        /** Generates actual random number. **/
        private static int randomInRange(int min, int max) {
            if (min > max) {
                int temp = min;
                min = max;
                max = temp;
            }
            return (min + rnd.nextInt(max - min + 1));
        }

	/** @return a random number multiple of delta between min and max inclusive */
	public static byte randomRangeScale (int min, int max, int delta) {
		
		return (byte) (delta * randomRange(min/delta, max/delta));
	}
	
	/** @return random valid weakness and resistance values, making sure they don't overlap */
	public static byte[] randomWR (int minW, int maxW, int minR, int maxR) {
		
		int numW = randomRange(minW, maxW);
		int numR = randomRange(minR, maxR);
		int w = 0, r = 0;
		
		do {
			
			if (numW != 0) {
				do {
					w = rnd.nextInt(64) << 2;
				} while (Integer.bitCount(w) != numW);
			}
		
			if (numR != 0) {
				do {
					r = rnd.nextInt(64) << 2;
				} while (Integer.bitCount(r) != numR);
			}
				
			} while ((w & r) != 0);
		
		byte[] b = {(byte) w, (byte) r};
		
		return b;
	}	

}
