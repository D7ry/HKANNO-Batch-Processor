how to use

1. download hkanno64, put it directly into disk D, rename the folder into "hkanno64-001".
so that D:\hkanno64-001 should contain hkanno64.exe

2.create a new folder named "hkxconvert" directly in disk D. Put all hkx you want to process into that folder.

3. download & install java https://www.java.com/download/windows_offline.jsp

4. double click the .bat file to run it. input help to see the way to trigger all the methods.

Current methods:
batch adding "attackStart" anno to skysa light attacks right after SkySA_AttackWinStart, so that the following hkx will be automatically triggered for NPC.
batch adding "atttackPowerStartForward" anno to skysa light attacks right SkySA_AttackWinStart, so that the following hkx will be automatically triggered for NPC.
batch removing annotations with custom keyword; for example, to remove all AMR annotations, first input "R" to start removing, then input "animmotion" so that all AMR annotations with "animomtion" keyword will be removed.
batch replacing annotations with custom keyword;
batch dumping all animations into txt. Instead of being dumped into "anno.txt", txts containing annotations will follow their corresponding .hkx names. So Skysa_mace1.hkx's annotationwill end up in Skysa_mace1.txt.
batch updating all animations based on their corresponding txt. So annotations in Skysa_mace1.txt will be updated into Skysa.mace1.hkx. Skip .hkx that has no corresponding .txt.

