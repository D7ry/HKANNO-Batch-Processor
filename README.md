# HKANNO Batch Processor
convenience tool to batch edit hkx files.

prerequisites:
Java runtime Environment https://www.java.com/en/download/manual.jsp
HKANNO64 https://www.nexusmods.com/skyrimspecialedition/mods/54244?tab=description

How to use:

1.download and extract HKBP, put everything into the same folder as your hkanno64.exe

2.put animations you want to edit into "animations" folder that comes with HKBP.

3.double-click HKBP.bat to run the program.

input "Help" to see all available methods and their hotkeys.

Current methods:

1. updating Skysa light attacks to append "attackStart" annotation after "SkySA_AttackWinStart" to allow NPC to chain combos.
2. updating Skysa light attacks to append "atttackPowerStartForward" annotations after "SkySA_AttackWinStart" to allow NPC to transfer into heavy attack.
3. removing specific annotation with custom keyword.
4. replacing specific annotation with custom keyword with custom annotation, keeping time annotation.
5. dumping all .hkx's annotation into .txt files with coresponding names for manual edit. For example skysa_2hm1.hkx's annotation will end up in skysa_2hm1.txt.
6. updating all .txt files annotation to their .hkx counterparts.
