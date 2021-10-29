package org.hkxconvert;

public class Const {
    public static String PATH = "D:\\hkxconvert";
    public static String DUMP_COMMAND_TEMPLATE = "cmd /c D: & cd hkanno64-001 & hkanno64.exe dump -o \"%s\" \"%s\"";
    public static String UPDATE_COMMAND_TEMPLATE = "cmd /c D: & cd hkanno64-001 & hkanno64.exe update -i \"%s\" \"%s\"";
    public static String ATTACK_STOP = "%s attackStop";
    public static String FIX_TEMPLATE = "%s SkySA_AttackWinEnd";
    public static String SKYSA_COMBO_ANNO = "%s attackStart";
    public static String SKYSA_HVY_COMBO_ANNO = "%s atttackPowerStartForward";
}
