;--------------------------------
;Include Modern UI

  !include "MUI2.nsh"

;--------------------------------
;Use same custom icon for both installer and uninstaller

  !define MUI_ICON "..\icon.ico"
  !define MUI_UNICON "..\icon.ico"

;--------------------------------
;General

  Name "JetBrowser"
  OutFile "installer.exe" 
  InstallDir "$PROGRAMFILES64\JetBrowser"
  RequestExecutionLevel admin

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING

  ;Show all languages, despite user's codepage
  !define MUI_LANGDLL_ALLLANGUAGES  

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !insertmacro MUI_UNPAGE_FINISH

;--------------------------------
;Languages

  !insertmacro MUI_LANGUAGE "Czech"
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "Installer"

  SetOutPath "$INSTDIR"
  File "JetBrowser.exe"
 
  ;Create Open with context menu entry
  WriteRegStr HKCR "CompressedFolder\Shell\JetBrowser" "" "Open with JetBrowser"
  WriteRegStr HKCR "CompressedFolder\Shell\JetBrowser" "Icon" "$INSTDIR\JetBrowser.exe"
  WriteRegStr HKCR "CompressedFolder\Shell\JetBrowser\command" "" "$INSTDIR\JetBrowser.exe $\"%1$\""

  ;Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JetBrowser" "DisplayName" "JetBrowser"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JetBrowser" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JetBrowser" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JetBrowser" "NoRepair" 1

  ;Create uninstaller
  WriteUninstaller "$INSTDIR\uninstall.exe"

SectionEnd

;--------------------------------
;Installer Functions

Function .onInit

  !insertmacro MUI_LANGDLL_DISPLAY

FunctionEnd

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ; Remove registry keys
  DeleteRegKey HKCR "CompressedFolder\Shell\JetBrowser"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JetBrowser"

  ; Remove files and uninstaller
  Delete "$INSTDIR\JetBrowser.exe"
  Delete "$INSTDIR\uninstall.exe"

  RMDir "$INSTDIR" # Using RMDir /r $INSTDIR in the uninstaller is not safe.

SectionEnd

;--------------------------------
;Uninstaller Functions

Function un.onInit

  !insertmacro MUI_UNGETLANGUAGE
  
FunctionEnd