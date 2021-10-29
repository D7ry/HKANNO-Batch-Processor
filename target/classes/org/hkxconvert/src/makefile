
CPPFLAGS = /nologo /EHsc /MT /I ..\vendor\hk2010_2_0_r1\Source
CONFLAGS = /nologo /LIBPATH:..\vendor\hk2010_2_0_r1\Lib\win32_net_9-0\release_multithreaded

all: hkanno64.exe

clean:
	rm *.obj

hkanno64.exe: anno.obj HctFilterManager.obj
	link.exe $(CONFLAGS) /OUT:hkanno64.exe anno.obj HctFilterManager.obj legacy_stdio_definitions.lib
