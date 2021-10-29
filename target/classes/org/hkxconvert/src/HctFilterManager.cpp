#include <windows.h>
#include <stdio.h>
#include <strsafe.h>

#pragma comment(lib, "Advapi32.lib") // RegOpenKeyExA RegQueryValueExA

LONG GetHctFilterPath(DWORD cbData, char *lpData)
{
	HKEY hKey;

	LONG lResult;
	lResult = RegOpenKeyExA(HKEY_CURRENT_USER, "Software\\Havok\\hkFilters_x64", 0, KEY_QUERY_VALUE, &hKey);

	if (lResult != ERROR_SUCCESS)
	{
		if (lResult == ERROR_FILE_NOT_FOUND)
		{
			printf("Key not found.\n");
		}
		else
		{
			printf("Error opening key.\n");
		}
		return lResult;
	}

	{
		//printf("DEBUG cbData: %d\n", cbData);

		DWORD dwRet;
		dwRet = RegQueryValueExA(hKey, "FilterPath", NULL, NULL, (unsigned char *) lpData, &cbData);

		if (dwRet != ERROR_SUCCESS) {
			if (lResult == ERROR_FILE_NOT_FOUND)
			{
				printf("Value not found.\n");
			}
			else
			{
				printf("Error query value.\n");
			}
		}
		else
		{
			//printf("DEBUG cbData: %d\n", cbData);
			//printf("DEBUG lpData: %s\n", lpData);
		}
	}

	RegCloseKey (hKey);

	return lResult;
}

int ExecuteHctFilterManager(char *ifile)
{
	char lpData[260] = "";
	LONG lResult;
	lResult = GetHctFilterPath(sizeof(lpData), lpData);
	if (lResult != ERROR_SUCCESS)
	{
		return 301;
	}

	char commandLine[260] = "";
	StringCchCatA(commandLine, 260, lpData); 
	StringCchCatA(commandLine, 260, "\\hctStandAloneFilterManager.exe ");
	StringCchCatA(commandLine, 260, "-s win32.hko ");
	// EscapeFileName
	StringCchCatA(commandLine, 260, "\"");
	StringCchCatA(commandLine, 260, ifile);
	StringCchCatA(commandLine, 260, "\"");

	//printf("DEBUG commandLine: %s\n", commandLine);

	STARTUPINFO startupInfo;
	PROCESS_INFORMATION processInformation;

	ZeroMemory( &startupInfo, sizeof(startupInfo) );
	startupInfo.cb = sizeof(startupInfo);
	ZeroMemory( &processInformation, sizeof(processInformation) );

	// Start the child process. 
	if ( !CreateProcess( NULL,	// No module name (use command line)
		commandLine,		// Command line
		NULL,			// Process handle not inheritable
		NULL,			// Thread handle not inheritable
		FALSE,			// Set handle inheritance to FALSE
		0,				// No creation flags
		NULL,			// Use parent's environment block
		NULL,			// Use parent's starting directory 
		&startupInfo,			 // Pointer to STARTUPINFO structure
		&processInformation )			// Pointer to PROCESS_INFORMATION structure
	) 
	{
		printf( "CreateProcess failed (%d).\n", GetLastError() );
		return 302;
	}

	// Wait until child process exits.
	WaitForSingleObject( processInformation.hProcess, INFINITE );

	// Close process and thread handles. 
	CloseHandle( processInformation.hProcess );
	CloseHandle( processInformation.hThread );

	return 0;
}
