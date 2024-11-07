#include <Constants.au3>
#include <MsgBoxConstants.au3>
Global $scriptName = "Zenyte Rune Miner"
Global $clientName = "Zenyte - HC Allza"

Global $lc = "left"
Global $rc = "right"

Global $Clientpos = WinGetPos("Zenyte - HC Allza")

Global $CutColor = 0x42351B

;Hotkeys
Global $Paused
HotKeySet("`", "Stop") ;Stops when ` is pressed
HotKeySet("~", "Stop") ;Stops when ~ is pressed
;HotKeySet("`", "TogglePause")

MsgBox(0, $scriptName, "Starting up... Press the ~ key to stop.")
$Timer = TimerInit()
WinActivate($clientName)
WinWaitActive($clientName)


Func Mine()
   $MinePos = PixelSearch($CutColor)
   MouseMove($MinePos[0], $MinePos[1])
   ToolTip("Clicking Tree")
   MouseClick($lc)
   Sleep(99)

EndFunc   ;==>CutTree


Func Stop()
	Local $RunTime = TimerDiff($Timer)
	;Convert miliseconds to seconds
	$RunTime = Round($RunTime/1000,2)
	;if over 3600 seconds convert to hours
	If $RunTime > 3600  Then
		$RunTime = Round($RunTime/3600,2)
			MsgBox($MB_SYSTEMMODAL, $scriptName, "Script Runtime: "&$RunTime&" Hours")
	ElseIf $RunTime > 60 Then
			$RunTime = Round($RunTime/60,2)
			MsgBox($MB_SYSTEMMODAL, $scriptName, "Script Runtime: "&$RunTime&" Minutes")
	Else
			MsgBox($MB_SYSTEMMODAL, $scriptName, "Script Runtime: "&$RunTime&" Seconds")
	EndIf
	Exit
EndFunc   ;==>Stop

Func TogglePause()
	$Paused = Not $Paused
	While $Paused
		MsgBox(0, $scriptName, $scriptName + "Script paused")
		Sleep(100)
	WEnd
EndFunc   ;==>TogglePause

;;;;;;;;;; SCRIPT ;;;;;;;;;;
While True
	Mine()
WEnd
;TestScreen()
;TestItems()
;TestSlot27()
;DropItem3()
