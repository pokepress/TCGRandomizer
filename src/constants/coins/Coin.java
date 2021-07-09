package constants.coins;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class Coin {
    
    protected RandomAccessFile cartFile;
    
    protected final int HeadsStartAddress = 0xa81fa;
    protected final int TailsStartAddress = 0xa828a;
    protected final int FlippingAStartAddress = 0xa82ca;
    protected final int FlippingBStartAddress = 0xa830a;
    protected final int SideStartAddress = 0xa834a;
    protected final int CoinPaletteAddress = 0xb7e21;
    protected final int CoinPaletteColorsAddress = CoinPaletteAddress + 0x3;
    
    protected final int HeadsTiles = 9;
    protected final int TailsAndFlipTiles = 4;
    protected final int SideTiles = 2;
    protected final int BytesPerTile = 16;
    
    public Coin(RandomAccessFile f)
    {
        cartFile = f;
    }
    
    /**Alters graphics for heads side of coin*/
    public abstract void AlterCoinHeads() throws IOException;
    /**Alters graphics for tails side of coin*/
    public abstract void AlterCoinTails() throws IOException;
    /**Alters graphics for flipping graphic a of coin*/
    public abstract void AlterCoinFlippingA() throws IOException;
    /**Alters graphics for flipping graphic b of coin*/
    public abstract void AlterCoinFlippingB() throws IOException;
    /**Alters graphics for side view of coin*/
    public abstract void AlterCoinSideView() throws IOException;
    /**Alters palette for coin*/
    public abstract void AlterCoinPalette() throws IOException;
    
    /** Given a coin and an output file, performs customizations.*/
    public static void customizeCoin(settings.Settings.coin coin, RandomAccessFile f ) throws IOException
    {
        Coin playerCoin;
        switch(coin)
        {
            case defaultPikachu:
                return;
            case grassMedal:
                playerCoin = new GrassMedal(f);
                break;
            case fireMedal:
                playerCoin = new FireMedal(f);
                break;
            case waterMedal:
                playerCoin = new WaterMedal(f);
                break;
            case lightningMedal:
                playerCoin = new LightningMedal(f);
                break;
            case psychicMedal:
                playerCoin = new PsychicMedal(f);
                break;
            case chansey:
                playerCoin = new ChanseyCoin(f);
                break;
            case psyduck:
                playerCoin = new PsyduckCoin(f);
                break;
            default:
                return;
        }
        
        if (playerCoin != null)
        {
            playerCoin.AlterCoinHeads();
            playerCoin.AlterCoinTails();
            playerCoin.AlterCoinFlippingA();
            playerCoin.AlterCoinFlippingB();
            playerCoin.AlterCoinSideView();
            playerCoin.AlterCoinPalette();
        }
    }
}
