//TODO
//Fix Pokemon names in descriptions
//Maybe randomize types (and type of moves) and other data
//...

package logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import constants.Constants;
import constants.coins.Coin;
import constants.duelists.*;
import gui.GUIController;
import settings.Settings.Options;
import utils.RNG;
import utils.Utils;

public class MainLogic {
	
	private static final GUIController gui = GUIController.getGuiController();

	public static void main () {
		
		long startTime = System.nanoTime();
                
                long seed = gui.getSeed();
                
                int eventCodeFreeSpacePointer = Constants.UNUSED_EFFECT_BEHAVIOR_START;
                
                if(seed != 0)
                {
                    RNG.setSeed(gui.getSeed());
                }
                else
                {
                    //UI should enforce valid seed-failsafe
                    seed = RNG.getSeed();
                    Utils.print("No valid seed value was entered. " + seed + " will be used.");
                }
                
                String outputFileName = utils.Utils.generateOutputFilename(seed, gui.getConfigString(false));
				
		ByteBuffer bbRead = ByteBuffer.allocate(Constants.PKMN_CARD_DATA_LENGTH * Constants.NUM_POKEMON_CARDS);
		ByteBuffer bbWrite = ByteBuffer.allocate(Constants.PKMN_CARD_DATA_LENGTH * Constants.NUM_POKEMON_CARDS);
		
		try (
				RandomAccessFile fin = new RandomAccessFile(Constants.FILE_NAME_IN,  "r" );
				RandomAccessFile fout = new RandomAccessFile(outputFileName,  "rw" );
				FileChannel chin  = fin.getChannel();
				FileChannel chout = fout.getChannel();
		) 	{

			if (ProgramLogic.verifyRom(chin) == false)
				throw new FileNotFoundException();
			
			ProgramLogic.createRomCopy(chin, chout);
                        
			ProgramLogic.readPokemonCardsData(chin, bbRead, bbWrite);
			if (gui.getOption(Options.MATCH.ordinal())) ProgramLogic.matchAttackEnergiesToType(bbRead);
                        if (gui.getOption(Options.REBALANCEATTCOST.ordinal())) ProgramLogic.rebalanceAttackCosts(bbRead);
                        if (gui.getOption(Options.FIXCFF.ordinal())) ProgramLogic.fixCallForFamily(fout,bbRead);
			ProgramLogic.doRandomization(bbRead, bbWrite);
                        
			ProgramLogic.saveChangesToRom(chout, bbWrite);
                        
                        if (gui.getOption(Options.CLUBMASTERREQ.ordinal())) RandomizerLogic.randomizeCMReq(fout);
                        
                        if (gui.getOption(Options.MEDALREQ.ordinal())) RandomizerLogic.randomizeMedalReq(fout);
                        
                        if (gui.getOption(Options.SPEED.ordinal())) ProgramLogic.maxTextSpeedNoAnimations(fout);
                        
                        if (gui.getOption(Options.REMOVETUTORIAL.ordinal()))
                        {
                            ProgramLogic.removePracticeBattle(fout);
                        }
                        else
                        {
                            ProgramLogic.disablePracticeMode(fout);
                        }
                        
                        if (gui.getOption(Options.CPUANTICHEAT.ordinal()))
                        {
                            ProgramLogic.disableBossSetupCheat(fout);
                        }
                        
                        if (gui.getIllusionCardAvailability() == settings.Settings.illusionCardAvailability.treatAsPromo)
                        {
                            ProgramLogic.addIllusionToCup(fout);
                        }
                        
                        if (gui.getOption(Options.FLIPPROB.ordinal()))
                        {
                            ProgramLogic.randomizeFlipProbability(fout);
                        }
                        
                        if (gui.getOption(Options.REBALANCETRAINERS.ordinal()))
                        {
                            eventCodeFreeSpacePointer = ProgramLogic.nerfEnergyRemoval(fout, eventCodeFreeSpacePointer);
                            eventCodeFreeSpacePointer = ProgramLogic.nerfGustOfWind(fout, eventCodeFreeSpacePointer);
                            ProgramLogic.nerfSuperEnergyRemoval(fout);
                            ProgramLogic.makeGamblerSafer(fout);
                            ProgramLogic.nerfProfessorOak(fout);
                        }
                        
                        if(gui.getPlayerCharacter() != settings.Settings.playerCharacter.defaultMark)
                        {
                            Duelist.customizeCharacter(gui.getPlayerCharacter(), fout);
                        }
                        
                        if(gui.getCoin() != settings.Settings.coin.defaultPikachu)
                        {
                            Coin.customizeCoin(gui.getCoin(), fout);
                        }
                        
                        if(gui.getOption(Options.SHOWSEEDCONFIG.ordinal()))
                        {
                            ProgramLogic.addConfigToIntro(fout);
                        }
                        
			ProgramLogic.fixGlobalChecksum(chout);
			
			long endTime = System.nanoTime();
			Utils.print(outputFileName + " has been successfully generated. Took "+ (double) (endTime - startTime)/1000000 + " ms.");
			
		} catch (FileNotFoundException e) {
			Utils.print("One or more needed files are missing.\n\n"
                                        + Constants.FILE_NAME_IN + " should be in the same directory as the jar file "
					+ "and a valid Pokemon TCG ROM.\n"
					+ "Required ROM: "
					+ "Pok\u00e9mon Trading Card Game (U) [C][!].gbc md5: 219b2cc64e5a052003015d4bd4c622cd");
                                                
			
		} catch (IOException e) {
			Utils.print("An unexpected error has occurred. Try again maybe?");
		}			
	}

}
