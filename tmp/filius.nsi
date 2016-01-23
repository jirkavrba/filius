; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "Filius"
!define PRODUCT_VERSION "1.6.1"
!define PRODUCT_PUBLISHER "lernsoftware-filius.de"
!define PRODUCT_WEB_SITE "http://www.lernsoftware-filius.de/"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\Filius.exe"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"

; MUI 1.67 compatible ------
!include "MUI.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Language Selection Dialog Settings
!define MUI_LANGDLL_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_LANGDLL_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_LANGDLL_REGISTRY_VALUENAME "NSIS:Language"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
; License page
!insertmacro MUI_PAGE_LICENSE "GPLv3.txt"
!insertmacro MUI_PAGE_DIRECTORY
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "German"

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
;OutFile "Filius-Setup-${PRODUCT_VERSION}.exe"
InstallDir "$PROGRAMFILES\Filius"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show

; http://nsis.sourceforge.net/Refresh_shell_icons
!define SHCNE_ASSOCCHANGED 0x08000000
!define SHCNF_IDLIST 0
Function RefreshShellIcons
  ; By jerome tremblay - april 2003
  System::Call 'shell32.dll::SHChangeNotify(i, i, i, i) v \
  (${SHCNE_ASSOCCHANGED}, ${SHCNF_IDLIST}, 0, 0)'
FunctionEnd

Function .onInit
  !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

Section "Filius" SEC01
  AddSize 6700
  SetShellVarContext all

  SetOutPath "$INSTDIR"
  SetOverwrite try
  File "Changelog.txt"
  SetOutPath "$INSTDIR\config"
  File "config\Desktop.txt"
  File "config\Desktop_en_GB.txt"
  File "config\filetypes.txt"
  File "config\filius.ini"
  SetOutPath "$INSTDIR"
  File "Einfuehrung_Filius.pdf"
  CreateDirectory "$SMPROGRAMS\Filius"
  CreateShortCut "$SMPROGRAMS\Filius\Filius.pdf.lnk" "$INSTDIR\Einfuehrung_Filius.pdf"
  File "Filius.exe"
  CreateShortCut "$SMPROGRAMS\Filius\Filius.lnk" "$INSTDIR\Filius.exe"
  File "filius.jar"
  File "filius.nsi"
  File "Filius.sh"
  File "Filius_MacOSX.command"
  File "GPLv2.txt"
  File "GPLv3.txt"
  SetOutPath "$INSTDIR\hilfe"
  File "hilfe\design_mode.html"
  File "hilfe\entwurfsansichtDetail.html"
  File "hilfe\entwurfsmodus.html"
  SetOutPath "$INSTDIR\hilfe\gfx"
  File "hilfe\gfx\aktionsmodus.png"
  File "hilfe\gfx\button_wizard.png"
  File "hilfe\gfx\draganddrop.gif"
  File "hilfe\gfx\draganddrop.png"
  File "hilfe\gfx\eigenschaften.png"
  File "hilfe\gfx\entwurfsansicht.png"
  File "hilfe\gfx\entwurfsmodus.png"
  File "hilfe\gfx\icon_softwareinstallation.png"
  File "hilfe\gfx\netzwek_aus.png"
  File "hilfe\gfx\tutorial1.png"
  File "hilfe\gfx\tutorial2.png"
  File "hilfe\gfx\verkabeln.png"
  SetOutPath "$INSTDIR\hilfe"
  File "hilfe\simulationsmodus.html"
  File "hilfe\simulation_mode.html"
  SetOutPath "$INSTDIR\img"
  File "img\email_icon.png"
  SetOutPath "$INSTDIR\lib"
  File "lib\commons-io-2.4.jar"
  File "lib\commons-lang3-3.3.2.jar"
  File "lib\htmlparser-1.6.jar"
  File "lib\jna-3.0.9.jar"
  SetOutPath "$INSTDIR\tmpl"
  File "tmpl\firewall_konfig_webseite_de_DE.txt"
  File "tmpl\firewall_konfig_webseite_en_GB.txt"
  File "tmpl\http_fehler.txt"
  SetOutPath "$INSTDIR\tmpl\quelltext_vorlagen"
  File "tmpl\quelltext_vorlagen\client.txt"
  File "tmpl\quelltext_vorlagen\client_gui.txt"
  File "tmpl\quelltext_vorlagen\server.txt"
  File "tmpl\quelltext_vorlagen\server_gui.txt"
  File "tmpl\quelltext_vorlagen\server_mitarbeiter.txt"
  SetOutPath "$INSTDIR\tmpl"
  File "tmpl\routing_de_DE.html"
  File "tmpl\routing_en_GB.html"
  File "tmpl\vermittlung_index_de_DE.txt"
  File "tmpl\vermittlung_index_en_GB.txt"
  File "tmpl\webserver_index.txt"
  ; Dateityp '.fls' zuordnen
  WriteRegStr HKLM "SOFTWARE\Classes\.fls\shell\open\command" "" '"$INSTDIR\Filius.exe" "%1"'
  WriteRegStr HKLM "SOFTWARE\Classes\.fls\DefaultIcon" "" "$INSTDIR\Filius.exe"
  Call RefreshShellIcons
SectionEnd

Section -AdditionalIcons
  SetOutPath $INSTDIR
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateShortCut "$SMPROGRAMS\Filius\Website.lnk" "$INSTDIR\${PRODUCT_NAME}.url"
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\Filius.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\Filius.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd


Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) wurde erfolgreich deinstalliert."
FunctionEnd

Function un.onInit
!insertmacro MUI_UNGETLANGUAGE
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "M�chten Sie $(^Name) und alle seinen Komponenten deinstallieren?" IDYES +2
  Abort
FunctionEnd

Section Uninstall
  SetShellVarContext all
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"
  Delete "$INSTDIR\tmpl\webserver_index.txt"
  Delete "$INSTDIR\tmpl\vermittlung_index_en_GB.txt"
  Delete "$INSTDIR\tmpl\vermittlung_index_de_DE.txt"
  Delete "$INSTDIR\tmpl\routing_en_GB.html"
  Delete "$INSTDIR\tmpl\routing_de_DE.html"
  Delete "$INSTDIR\tmpl\quelltext_vorlagen\server_mitarbeiter.txt"
  Delete "$INSTDIR\tmpl\quelltext_vorlagen\server_gui.txt"
  Delete "$INSTDIR\tmpl\quelltext_vorlagen\server.txt"
  Delete "$INSTDIR\tmpl\quelltext_vorlagen\client_gui.txt"
  Delete "$INSTDIR\tmpl\quelltext_vorlagen\client.txt"
  Delete "$INSTDIR\tmpl\http_fehler.txt"
  Delete "$INSTDIR\tmpl\firewall_konfig_webseite_en_GB.txt"
  Delete "$INSTDIR\tmpl\firewall_konfig_webseite_de_DE.txt"
  Delete "$INSTDIR\lib\jna-3.0.9.jar"
  Delete "$INSTDIR\lib\htmlparser-1.6.jar"
  Delete "$INSTDIR\lib\commons-lang3-3.3.2.jar"
  Delete "$INSTDIR\lib\commons-io-2.4.jar"
  Delete "$INSTDIR\img\email_icon.png"
  Delete "$INSTDIR\hilfe\simulation_mode.html"
  Delete "$INSTDIR\hilfe\simulationsmodus.html"
  Delete "$INSTDIR\hilfe\gfx\verkabeln.png"
  Delete "$INSTDIR\hilfe\gfx\tutorial2.png"
  Delete "$INSTDIR\hilfe\gfx\tutorial1.png"
  Delete "$INSTDIR\hilfe\gfx\netzwek_aus.png"
  Delete "$INSTDIR\hilfe\gfx\icon_softwareinstallation.png"
  Delete "$INSTDIR\hilfe\gfx\entwurfsmodus.png"
  Delete "$INSTDIR\hilfe\gfx\entwurfsansicht.png"
  Delete "$INSTDIR\hilfe\gfx\eigenschaften.png"
  Delete "$INSTDIR\hilfe\gfx\draganddrop.png"
  Delete "$INSTDIR\hilfe\gfx\draganddrop.gif"
  Delete "$INSTDIR\hilfe\gfx\button_wizard.png"
  Delete "$INSTDIR\hilfe\gfx\aktionsmodus.png"
  Delete "$INSTDIR\hilfe\entwurfsmodus.html"
  Delete "$INSTDIR\hilfe\entwurfsansichtDetail.html"
  Delete "$INSTDIR\hilfe\design_mode.html"
  Delete "$INSTDIR\GPLv3.txt"
  Delete "$INSTDIR\GPLv2.txt"
  Delete "$INSTDIR\Filius_mitLog.bat"
  Delete "$INSTDIR\Filius_MacOSX.command"
  Delete "$INSTDIR\Filius.sh"
  Delete "$INSTDIR\filius.nsi"
  Delete "$INSTDIR\filius.jar"
  Delete "$INSTDIR\Filius.exe"
  Delete "$INSTDIR\Einfuehrung_Filius.pdf"
  Delete "$INSTDIR\config\filius.ini"
  Delete "$INSTDIR\config\filetypes.txt"
  Delete "$INSTDIR\config\Desktop_en_GB.txt"
  Delete "$INSTDIR\config\Desktop.txt"
  Delete "$INSTDIR\Changelog.txt"

  Delete "$SMPROGRAMS\Filius\Website.lnk"
  Delete "$SMPROGRAMS\Filius\Filius.lnk"
  Delete "$SMPROGRAMS\Filius\Filius.pdf.lnk"

  RMDir "$SMPROGRAMS\Filius"
  RMDir "$INSTDIR\tmpl\quelltext_vorlagen"
  RMDir "$INSTDIR\tmpl"
  RMDir "$INSTDIR\lib"
  RMDir "$INSTDIR\img"
  RMDir "$INSTDIR\hilfe\gfx"
  RMDir "$INSTDIR\hilfe"
  RMDir "$INSTDIR\config"
  RMDir "$INSTDIR"
  RMDir ""

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  DeleteRegKey HKLM "${PRODUCT_DIR_REGKEY}"
  ; Dateitypzuordnung entfernen
  DeleteRegKey HKLM "SOFTWARE\Classes\.fls"
  SetAutoClose true
SectionEnd