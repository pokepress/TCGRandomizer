package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.MainLogic;
import settings.EvoTypes;
import settings.Settings;
import utils.RNG;

//Actual UI logic
public class GUIController implements Initializable {
	
	private static final GUIController guiController = new GUIController();
	private final boolean[] options = new boolean[Settings.NUM_OPTIONS];
	private List<Integer> HPList = new ArrayList<Integer>();
	private List<Integer> RCList = new ArrayList<Integer>();
        private List<String> WRRndTypeList = new ArrayList<String>();
        private List<String> PlayerCharList = new ArrayList<String>();
        private List<String> IllusAvailList = new ArrayList<String>();
        
        private static final String WRRandomFull = "Full-Randomize each card individually";
        private static final String WRRandomCombo = "Identically randomize within each original weakness/resistance combination";
        private static final String WRRandomLine = "Identically randomize within each gen 1 main game evolution line";
	
        private static final String PlayerDefaultMark = "Default (Mark)";
        private static final String PlayerMint = "Mint (Card GB 2)";
        
        private static final String CardPopOnly = "Default (Card Pop! Only)";
        private static final String AddToSets = "Add randomly to in-game sets";
        private static final String TreatAsPromo = "Treat as promo cards (Challenge Cup)";
        
	/* Options to randomize HP, weaknesses/resistances, retreat cost, 
           shuffle moves, default gsme speed, and skip tutorial */
	@FXML private CheckBox optionHP, optionWR, optionRC, optionMoves;
	@FXML private CheckBox optionFillEmpty, optionMatchEnergies, optionSpeed;
        @FXML private CheckBox optionTutorial;
	
	/* Minimum and maximum HP and retreat cost values for each of the 6 evolution types */
	@FXML private Label minHPLbl, maxHPLbl, minRCLbl, maxRCLbl;
        @FXML private Label lbl11, lbl12, lbl22, lbl13, lbl23, lbl33;
        @FXML private ChoiceBox<Integer> minHP1, minHP2, minHP3, minHP4, minHP5, minHP6;
	@FXML private ChoiceBox<Integer> maxHP1, maxHP2, maxHP3, maxHP4, maxHP5, maxHP6;
	@FXML private ChoiceBox<Integer> minRC1, minRC2, minRC3, minRC4, minRC5, minRC6;
	@FXML private ChoiceBox<Integer> maxRC1, maxRC2, maxRC3, maxRC4, maxRC5, maxRC6;
	
	/* Minimum and maximum number of weaknesses and resistances, randomization type */
	@FXML private Label wrTypeLbl, wrNumWeakLbl, wrNumResLbl, wrMinLbl, wrMaxLbl;
        @FXML private ChoiceBox<Integer> minW, maxW, minR, maxR;
        @FXML private ChoiceBox<String> wrRndType;
        
        /* Miscellaneous options*/
        @FXML private Label playerCharLbl, illusLbl;
        @FXML private ChoiceBox<String> playerChar;
        @FXML private ChoiceBox<String> illusAvail;
        
        /* Randomizer seed*/
        @FXML private Label seedLbl;
        @FXML private TextField seedVal;

	public static GUIController getGuiController() {
		return guiController;
	}
	
	@Override
	public void initialize (URL location, ResourceBundle resources) {
		
		setAllMainOptions();
		initChoiceBoxes();
		addListeners();
                populateSeedField();
                doLayout();
	}
	
        /** Returns the current status of the specified option.*/
	public boolean getOption (int which) {
		return options[which];
	}

        /** Toggles the status of the specified option.*/
	private void setOption (int which) {
		getGuiController().options[which] ^= true;
	}
	
	/** Used during initialization to mark all main options as true */
	public void setAllMainOptions() {
		handleHPOption();
		handleWROption();
		handleRCOption();
		handleMovesOption();
                handleSpeedOption();
                handleTutorialOption();
	}

        /** Updates whether HP is randomized. */
	private void handleHPOption() {
		setOption (Settings.Options.HP.ordinal());
	}

        /** Updates whether weakness and resistance are randomized. */
	private void handleWROption() {
		setOption (Settings.Options.WR.ordinal());
	}

        /** Updates whether retreat cost is randomized. */
	private void handleRCOption() {
		setOption (Settings.Options.RC.ordinal());
	}

        /** Updates whether moves are randomized. */
	private void handleMovesOption() {
		setOption (Settings.Options.MOVES.ordinal());
	}

        /** Updates whether text speed is maximized and animation are disabled.*/
        private void handleSpeedOption() {
                setOption (Settings.Options.SPEED.ordinal());
        }
        
        /** Updates whether the tutorial game is skipped instead of just
         * becoming a normal duel. */
        private void handleTutorialOption() {
                setOption (Settings.Options.REMOVETUTORIAL.ordinal());
        }
        
        /** Returns the selected weakness/resistance randomization setting.*/
        public Settings.wrRandomType getWRRandomType () {
		return Settings.settings.getWRRandomizationType();
	}
        
        /** Returns the selected player character setting.*/
        public Settings.playerCharacter getPlayerCharacter () {
		return Settings.settings.getPlayerChar();
	}
        
        /** Returns the selected illusion card setting.*/
        public Settings.illusionCardAvailability getIllusionCardAvailability () {
		return Settings.settings.getIllusionCardAvailability();
	}
        
        /** Returns the selected randomization seed.*/
        public long getSeed () {
            String seed = Settings.settings.getCustomSeed();
            if (seed.length() == 0)
            {
                return 0;
            }
            
            try
            {
                return Long.parseLong(seed);
            }
            catch (NumberFormatException e)
            {
                
            }
            
            return 0;
	}
	
	@FXML
	private void beginProgram() {
                if(this.getSeed() == 0)
                {
                    utils.Utils.print("A nonzero numeric seed must be supplied.");
                    return;
                }
		MainLogic.main();
	}
	
	@FXML
	private void handleHPOptionClick() {
		handleHPOption();
		minHP1.setDisable(minHP1.isDisable()^true);
		maxHP1.setDisable(maxHP1.isDisable()^true);
		minHP2.setDisable(minHP2.isDisable()^true);
		maxHP2.setDisable(maxHP2.isDisable()^true);
		minHP3.setDisable(minHP3.isDisable()^true);
		maxHP3.setDisable(maxHP3.isDisable()^true);
		minHP4.setDisable(minHP4.isDisable()^true);
		maxHP4.setDisable(maxHP4.isDisable()^true);
		minHP5.setDisable(minHP5.isDisable()^true);
		maxHP5.setDisable(maxHP5.isDisable()^true);
		minHP6.setDisable(minHP6.isDisable()^true);
		maxHP6.setDisable(maxHP6.isDisable()^true);
	}
	
	@FXML
	private void handleWROptionClick() {
		handleWROption();
		minW.setDisable(minW.isDisable()^true);
		maxW.setDisable(maxW.isDisable()^true);
		minR.setDisable(minR.isDisable()^true);
		maxR.setDisable(maxR.isDisable()^true);
                wrRndType.setDisable(wrRndType.isDisable()^true);
	}
	
	@FXML
	private void handleRCOptionClick() {	
		handleRCOption();
		minRC1.setDisable(minRC1.isDisable()^true);
		maxRC1.setDisable(maxRC1.isDisable()^true);
		minRC2.setDisable(minRC2.isDisable()^true);
		maxRC2.setDisable(maxRC2.isDisable()^true);
		minRC3.setDisable(minRC3.isDisable()^true);
		maxRC3.setDisable(maxRC3.isDisable()^true);
		minRC4.setDisable(minRC4.isDisable()^true);
		maxRC4.setDisable(maxRC4.isDisable()^true);
		minRC5.setDisable(minRC5.isDisable()^true);
		maxRC5.setDisable(maxRC5.isDisable()^true);
		minRC6.setDisable(minRC6.isDisable()^true);
		maxRC6.setDisable(maxRC6.isDisable()^true);
	}
	
	@FXML
	private void handleMovesOptionClick() {
		handleMovesOption();
		optionFillEmpty.setDisable(optionFillEmpty.isDisable()^true);
		optionMatchEnergies.setDisable(optionMatchEnergies.isDisable()^true);
	}
	
	@FXML
	private void handleFillEmptyOptionClick() {	
		setOption (Settings.Options.FILL.ordinal());
	}
	
	@FXML
	private void handleMatchEnergiesOptionClick() {	
		setOption (Settings.Options.MATCH.ordinal());
	}	
        
        @FXML
	private void handleSpeedOptionClick() {	
		setOption (Settings.Options.SPEED.ordinal());
	}
        
        @FXML
	private void handleTutorialOptionClick() {	
		setOption (Settings.Options.REMOVETUTORIAL.ordinal());
	}
	
	/** Initializes all choice boxes to their default values */
	private void initChoiceBoxes() {
		
		HPList.addAll(Arrays.asList(30, 40, 50, 60, 70, 80, 90, 100, 110, 120));
		RCList.addAll(Arrays.asList(0, 1, 2, 3));
                
                WRRndTypeList.add(WRRandomFull);
                WRRndTypeList.add(WRRandomCombo);
                WRRndTypeList.add(WRRandomLine);
                
                PlayerCharList.add(PlayerDefaultMark);
                PlayerCharList.add(PlayerMint);
                
                IllusAvailList.add(CardPopOnly);
                IllusAvailList.add(AddToSets);
                IllusAvailList.add(TreatAsPromo);

		minHP1.setValue(EvoTypes.EVO1OF1.getMinHP());
		maxHP1.setValue(EvoTypes.EVO1OF1.getMaxHP());
		minRC1.setValue(EvoTypes.EVO1OF1.getMinRC());
		maxRC1.setValue(EvoTypes.EVO1OF1.getMaxRC());
		minHP1.getItems().addAll(HPList);
		maxHP1.getItems().addAll(HPList);
		minRC1.getItems().addAll(RCList);
		maxRC1.getItems().addAll(RCList);
		
		minHP2.setValue(EvoTypes.EVO1OF2.getMinHP());
		maxHP2.setValue(EvoTypes.EVO1OF2.getMaxHP());
		minRC2.setValue(EvoTypes.EVO1OF2.getMinRC());
		maxRC2.setValue(EvoTypes.EVO1OF2.getMaxRC());
		minHP2.getItems().addAll(HPList);
		maxHP2.getItems().addAll(HPList);
		minRC2.getItems().addAll(RCList);
		maxRC2.getItems().addAll(RCList);
		
		minHP3.setValue(EvoTypes.EVO2OF2.getMinHP());
		maxHP3.setValue(EvoTypes.EVO2OF2.getMaxHP());
		minRC3.setValue(EvoTypes.EVO2OF2.getMinRC());
		maxRC3.setValue(EvoTypes.EVO2OF2.getMaxRC());
		minHP3.getItems().addAll(HPList);
		maxHP3.getItems().addAll(HPList);
		minRC3.getItems().addAll(RCList);
		maxRC3.getItems().addAll(RCList);
		
		minHP4.setValue(EvoTypes.EVO1OF3.getMinHP());
		maxHP4.setValue(EvoTypes.EVO1OF3.getMaxHP());
		minRC4.setValue(EvoTypes.EVO1OF3.getMinRC());
		maxRC4.setValue(EvoTypes.EVO1OF3.getMaxRC());
		minHP4.getItems().addAll(HPList);
		maxHP4.getItems().addAll(HPList);
		minRC4.getItems().addAll(RCList);
		maxRC4.getItems().addAll(RCList);
		
		minHP5.setValue(EvoTypes.EVO2OF3.getMinHP());
		maxHP5.setValue(EvoTypes.EVO2OF3.getMaxHP());
		minRC5.setValue(EvoTypes.EVO2OF3.getMinRC());
		maxRC5.setValue(EvoTypes.EVO2OF3.getMaxRC());
		minHP5.getItems().addAll(HPList);
		maxHP5.getItems().addAll(HPList);
		minRC5.getItems().addAll(RCList);
		maxRC5.getItems().addAll(RCList);
		
		minHP6.setValue(EvoTypes.EVO3OF3.getMinHP());
		maxHP6.setValue(EvoTypes.EVO3OF3.getMaxHP());
		minRC6.setValue(EvoTypes.EVO3OF3.getMinRC());
		maxRC6.setValue(EvoTypes.EVO3OF3.getMaxRC());
		minHP6.getItems().addAll(HPList);
		maxHP6.getItems().addAll(HPList);
		minRC6.getItems().addAll(RCList);
		maxRC6.getItems().addAll(RCList);
		
		/* RCList doubles as WRList */
		minW.setValue(Settings.settings.getMinWeaknesses());
		maxW.setValue(Settings.settings.getMaxWeaknesses());
		minR.setValue(Settings.settings.getMinResistances());
		maxR.setValue(Settings.settings.getMaxResistances());
		minW.getItems().addAll(RCList);
		maxW.getItems().addAll(RCList);
		minR.getItems().addAll(RCList);
		maxR.getItems().addAll(RCList);
                wrRndType.getItems().addAll(WRRndTypeList);
                switch(Settings.settings.getWRRandomizationType())
                {
                    case ByWRCombination:
                        wrRndType.setValue(WRRandomCombo);
                        break;
                    case ByLine:
                        wrRndType.setValue(WRRandomLine);
                        break;
                    default:
                        wrRndType.setValue(WRRandomFull);
                        break;
                }
                
                playerChar.getItems().addAll(PlayerCharList);
                switch(Settings.settings.getPlayerChar())
                {
                    case defaultMark:
                        playerChar.setValue(PlayerDefaultMark);
                        break;
                    case mint:
                        playerChar.setValue(PlayerMint);
                        break;
                    default:
                        playerChar.setValue(PlayerDefaultMark);
                        break;
                }
                
                illusAvail.getItems().addAll(IllusAvailList);
                switch(Settings.settings.getIllusionCardAvailability())
                {
                    case cardPopOnly:
                        illusAvail.setValue(CardPopOnly);
                        break;
                    case randomToSet:
                        illusAvail.setValue(AddToSets);
                        break;
                    case treatAsPromo:
                        illusAvail.setValue(TreatAsPromo);
                        break;
                    default:
                        illusAvail.setValue(CardPopOnly);
                        break;
                }
	}
	
	/** Listens to value of controls changing */
	private void addListeners() {
		
		minHP1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF1.setMinHP(HPList.get(newValue.intValue()));
			}
		});
		
		maxHP1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF1.setMaxHP(HPList.get(newValue.intValue()));
			}
		});
		
		minRC1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF1.setMinRC(RCList.get(newValue.intValue()));
			}
		});
		
		maxRC1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF1.setMaxRC(RCList.get(newValue.intValue()));
			}
		});
		
		minHP2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF2.setMinHP(HPList.get(newValue.intValue()));
			}
		});
		
		maxHP2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF2.setMaxHP(HPList.get(newValue.intValue()));
			}
		});
		
		minRC2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF2.setMinRC(RCList.get(newValue.intValue()));
			}
		});
		
		maxRC2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF2.setMaxRC(RCList.get(newValue.intValue()));
			}
		});
		
		minHP3.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO2OF2.setMinHP(HPList.get(newValue.intValue()));
			}
		});
		
		maxHP3.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO2OF2.setMaxHP(HPList.get(newValue.intValue()));
			}
		});
		
		minRC3.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO2OF2.setMinRC(RCList.get(newValue.intValue()));
			}
		});
		
		maxRC3.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO2OF2.setMaxRC(RCList.get(newValue.intValue()));
			}
		});
		
		minHP4.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF3.setMinHP(HPList.get(newValue.intValue()));
			}
		});
		
		maxHP4.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF3.setMaxHP(HPList.get(newValue.intValue()));
			}
		});
		
		minRC4.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF3.setMinRC(RCList.get(newValue.intValue()));
			}
		});
		
		maxRC4.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO1OF3.setMaxRC(RCList.get(newValue.intValue()));
			}
		});
		
		minHP5.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO2OF3.setMinHP(HPList.get(newValue.intValue()));
			}
		});
		
		maxHP5.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO2OF3.setMaxHP(HPList.get(newValue.intValue()));
			}
		});
		
		minRC5.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO2OF3.setMinRC(RCList.get(newValue.intValue()));
			}
		});
		
		maxRC5.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO2OF3.setMaxRC(RCList.get(newValue.intValue()));
			}
		});
		
		minHP6.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO3OF3.setMinHP(HPList.get(newValue.intValue()));
			}
		});
		
		maxHP6.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO3OF3.setMaxHP(HPList.get(newValue.intValue()));
			}
		});
		
		minRC6.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO3OF3.setMinRC(RCList.get(newValue.intValue()));
			}
		});
		
		maxRC6.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				EvoTypes.EVO3OF3.setMaxRC(RCList.get(newValue.intValue()));
			}
		});
                
                wrRndType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                                if (newValue.equals(WRRandomFull))
                                {
                                    Settings.settings.setWRRandomizationType(Settings.wrRandomType.Full);
                                }
                                else if (newValue.equals(WRRandomCombo))
                                {
                                    Settings.settings.setWRRandomizationType(Settings.wrRandomType.ByWRCombination);
                                }
                                else if (newValue.equals(WRRandomLine))
                                {
                                    Settings.settings.setWRRandomizationType(Settings.wrRandomType.ByLine);
                                }
			}
		});
		
		minW.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				Settings.settings.setMinWeaknesses(RCList.get(newValue.intValue()));
			}
		});
		
		maxW.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				Settings.settings.setMaxWeaknesses(RCList.get(newValue.intValue()));
			}
		});
		
		minR.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				Settings.settings.setMinResistances(RCList.get(newValue.intValue()));
			}
		});
		
		maxR.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				Settings.settings.setMaxResistances(RCList.get(newValue.intValue()));
			}
		});
                
                playerChar.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                                if (newValue.equals(PlayerDefaultMark))
                                {
                                    Settings.settings.setPlayerChar(Settings.playerCharacter.defaultMark);
                                }
                                else if (newValue.equals(PlayerMint))
                                {
                                    Settings.settings.setPlayerChar(Settings.playerCharacter.mint);
                                }
			}
		});
                
                illusAvail.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                                if (newValue.equals(CardPopOnly))
                                {
                                    Settings.settings.setIllusionCardAvailability(Settings.illusionCardAvailability.cardPopOnly);
                                }
                                else if (newValue.equals(AddToSets))
                                {
                                    Settings.settings.setIllusionCardAvailability(Settings.illusionCardAvailability.randomToSet);
                                }
                                else if (newValue.equals(TreatAsPromo))
                                {
                                    Settings.settings.setIllusionCardAvailability(Settings.illusionCardAvailability.treatAsPromo);
                                }
			}
		});
                
                seedVal.textProperty().addListener(new ChangeListener<String>() {
            
                    	@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                                Settings.settings.setCustomSeed(newValue);
			}
                });
	}
        
        /** Arranges controls across vertical space.*/
        private void doLayout()
        {
            double nextElementY = 21;
            nextElementY = doCheckBoxSectionLayout(nextElementY);
            nextElementY = doHPRCSectionLayout(nextElementY);
            nextElementY = doWRSectionLayout(nextElementY);
            nextElementY = doMiscSectionLayout(nextElementY);
            
            nextElementY += 10;
            seedLbl.setLayoutY(nextElementY + 1);
            seedVal.setLayoutY(nextElementY);
        }
        
        private double doCheckBoxSectionLayout(double yPos)
        {
            double checkYIncrement = 25;
            optionHP.setLayoutY(yPos);
            yPos += checkYIncrement;
            optionWR.setLayoutY(yPos);
            yPos += checkYIncrement;
            optionRC.setLayoutY(yPos);
            yPos += checkYIncrement;
            optionMoves.setLayoutY(yPos);
            optionFillEmpty.setLayoutY(yPos);
            optionMatchEnergies.setLayoutY(yPos);
            yPos += checkYIncrement;
            optionSpeed.setLayoutY(yPos);
            yPos += checkYIncrement;
            optionTutorial.setLayoutY(yPos);
            yPos += checkYIncrement;
            return yPos;
        }
        
        private double doHPRCSectionLayout(double yPos)
        {
            double hpRcYIncrement = 32;
            minHPLbl.setLayoutY(yPos + 6);
            maxHPLbl.setLayoutY(yPos + 6);
            minRCLbl.setLayoutY(yPos + 6);
            maxRCLbl.setLayoutY(yPos + 6);
            yPos += hpRcYIncrement;
            minHP1.setLayoutY(yPos);
            maxHP1.setLayoutY(yPos);
            minRC1.setLayoutY(yPos);
            maxRC1.setLayoutY(yPos);
            lbl11.setLayoutY(yPos+1);
            yPos += hpRcYIncrement;
            minHP2.setLayoutY(yPos);
            maxHP2.setLayoutY(yPos);
            minRC2.setLayoutY(yPos);
            maxRC2.setLayoutY(yPos);
            lbl12.setLayoutY(yPos+1);
            yPos += hpRcYIncrement;
            minHP3.setLayoutY(yPos);
            maxHP3.setLayoutY(yPos);
            minRC3.setLayoutY(yPos);
            maxRC3.setLayoutY(yPos);
            lbl22.setLayoutY(yPos+1);
            yPos += hpRcYIncrement;
            minHP4.setLayoutY(yPos);
            maxHP4.setLayoutY(yPos);
            minRC4.setLayoutY(yPos);
            maxRC4.setLayoutY(yPos);
            lbl13.setLayoutY(yPos+1);
            yPos += hpRcYIncrement;
            minHP5.setLayoutY(yPos);
            maxHP5.setLayoutY(yPos);
            minRC5.setLayoutY(yPos);
            maxRC5.setLayoutY(yPos);
            lbl23.setLayoutY(yPos+1);
            yPos += hpRcYIncrement;
            minHP6.setLayoutY(yPos);
            maxHP6.setLayoutY(yPos);
            minRC6.setLayoutY(yPos);
            maxRC6.setLayoutY(yPos);
            lbl33.setLayoutY(yPos + 1);
            yPos += hpRcYIncrement;
            return yPos;
        }
        
        private double doWRSectionLayout(double yPos)
        {
            double wrIncrement = 32;
            yPos += wrIncrement - 22;
            wrRndType.setLayoutY(yPos);
            wrTypeLbl.setLayoutY(yPos + 1);
            yPos += wrIncrement - 10;
            wrMinLbl.setLayoutY(yPos + 6);
            wrMaxLbl.setLayoutY(yPos + 6);
            yPos += wrIncrement;
            minW.setLayoutY(yPos);
            maxW.setLayoutY(yPos);
            wrNumWeakLbl.setLayoutY(yPos + 1);
            yPos += wrIncrement;
            minR.setLayoutY(yPos);
            maxR.setLayoutY(yPos);
            wrNumResLbl.setLayoutY(yPos + 1);
            yPos += wrIncrement;
            return yPos;
        }
        
        private double doMiscSectionLayout(double yPos)
        {
            double miscIncrement = 32;
            yPos += 10;
            playerCharLbl.setLayoutY(yPos + 1);
            playerChar.setLayoutY(yPos);
            yPos += miscIncrement;
            illusLbl.setLayoutY(yPos + 1);
            illusAvail.setLayoutY(yPos);
            yPos += miscIncrement;
            return yPos;
        }
        
        private void populateSeedField()
        {
            seedVal.setText(String.valueOf(RNG.getSeed()));
        }
        
        /** Returns a string representing some of the configuration settings
            used to generate the ROM. Intended for use in output filename.*/
        public String getConfigString()
        {
            String config = "";
            for (int opt = 0 ; opt < Settings.NUM_OPTIONS ; opt++)
            {
                config += options[opt] ? "1" : "0";
            }
            
            //If Randomizing Weakness/Resistance, specify type
            if(options[Settings.Options.WR.ordinal()])
            {
                config += "WR" + Settings.settings.getWRRandomizationType().ordinal();
            }
            
            //Status of Illusion cards
            config += "IL" + Settings.settings.getIllusionCardAvailability().ordinal();
            
            return config;
        }

}
