package constants;

public class WRGroups {
    
    /** Represents weakness and resistance based on what was originally printed 
        on the card.*/
    public enum WRGroupsByComb {
        WeakFireResistNone,
        WeakFireResistFight,
        WeakPsyResistNone,
        WeakPsyResistFight,
        WeakWaterResistNone,
        WeakWaterResistFight,
        WeakNoneResistFight,
        WeakLightResistNone,
        WeakGrassResistNone,
        WeakGrassResistFight,
        WeakFightResistNone,
        WeakGrassResistLight,
        WeakLightResistFight,
        WeakFightResistPsy,
        WeakNoneResistPsy,
    }
    
    /** Indicates total number of weakness/resistance combinations.*/
    public static final int NUM_WR_COMB = WRGroupsByComb.WeakNoneResistPsy.ordinal() + 1;
    
    /** Represents weakness and resistance based on the card's evolutionary
        line in the Gen 1 games.*/
    public enum WRGroupsByLine {
        BulbaIvyVenusaur,
        CatMetaFree,
        WeeKaBee,
        EkArbok,
        NidoFRinaQueen,
        NidoMRinoKing,
        ZuGolbat,
        OddGloomVile,
        ParaParasect,
        NatMoth,
        BellWepVic,
        GrimerMuk,
        Exegg,
        KoffWeez,
        Tangela,
        Scyther,
        Pinsir,
        
        CharmanLeonZard,
        VulpNine,
        GrowlNine,
        PonyDash,
        Magmar,
        Moltres,

        SqirWarBlastoise,
        PsyGolduck,
        PoliwagWhirlWrath,
        TentaCC,
        SeeDewgong,
        ShellCloyster,
        KrabKing,
        HorseaDra,
        GoldKing,
        StaryuMie,
        MagiDos,
        Lapras,
        OmanyteStar,
        Articuno,

        PikaRaichu,
        MagnemiteTon,
        VoltTrode,
        Electabuzz,
        Zapdos,

        ShrewSlash,
        DigTrio,
        MankApe,
        ChopChokeChamp,
        GeoGravGolem,
        Onix,
        CueWak,
        Hitmonlee,
        Hitmonchan, //Gen 1, no Tyrogue
        HornDon,
        KabutoTops,
        Aerodactyl,

        AbKabZam,
        SlopokeBro,
        GasHauntGengar,
        DrowzeeNo,
        MrMime,
        Jynx,
        Mewtwo,
        Mew,

        PidgeyOttoOt,
        RattIcate,
        SpFearow,
        ClefairAble,
        JigglyTuff,
        MeowSian,
        Farfetchd,
        DoduoDrio,
        Lickitung,
        Chansey,
        Kangaskhan,
        Tauros,
        Ditto,
        Eevee, //All Eeveeloutions are grouped together.
        Porygon,
        Snorlax,
        DratAirIte
    }

    /** Indicates total number of evolution line groups. */
    public static final int NUM_WR_LINES = WRGroupsByLine.DratAirIte.ordinal() + 1;
}

