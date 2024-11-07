#include <Constants.au3>
#include <MsgBoxConstants.au3>
Global $scriptName = "BattleScape Chop n Cut"
Global $clientName = "BattleScape"

Global $lc = "left"
Global $rc = "right"

Global $Clientpos = WinGetPos("BattleScape - Powered by RuneLite")

Global $Alerts = True

;GameScreen
Global $GameScreen[4] = [8 + $Clientpos[0], 31 + $Clientpos[1], 519 + $Clientpos[0], 364 + $Clientpos[1]]
Global $CutColor = 0x691509
;Inventory
Global $Inventory[4] = [560 + $Clientpos[0], 235 + $Clientpos[1], 730 + $Clientpos[0], 490 + $Clientpos[1]]
Global $Slot27[4] = [695 + $Clientpos[0], 465 + $Clientpos[1], 730 + $Clientpos[0], 490 + $Clientpos[1]]

Global $InvRow1[4] = [560 + $Clientpos[0], 240 + $Clientpos[1], 730 + $Clientpos[0], 274 + $Clientpos[1]]
Global $InvRow2[4] = [$InvRow1[0], $InvRow1[3] + 35, $InvRow1[2], $InvRow1[0] + 35]
Global $InvRow3[4] = [$InvRow2[0], $InvRow1[3] + 35, $InvRow1[2], $InvRow2[0] + 35]
Global $InvRow4[4] = [$InvRow3[0], $InvRow1[3] + 35, $InvRow1[2], $InvRow3[0] + 35]
Global $InvRow5[4] = [$InvRow4[0], $InvRow1[3] + 35, $InvRow1[2], $InvRow4[0] + 35]
Global $InvRow6[4] = [$InvRow5[0], $InvRow1[3] + 35, $InvRow1[2], $InvRow5[0] + 35]
Global $InvRow7[4] = [$InvRow6[0], $InvRow1[3] + 35, $InvRow1[2], $InvRow6[0] + 35]

;Item 1
Global $Item1[4] = [410 + $Clientpos[0], 170 + $Clientpos[1], 440 + $Clientpos[0], 200 + $Clientpos[1]]
Global $item1Color = 0x776F6E ;Knife
Global $item2Color = 0x78490D ;Logs
Global $item3Color = 0x8B4540 ;Shield


;Box
Global $BoxPosition[4] = [$Clientpos[0] + 216, $Clientpos[1] + 420, $Clientpos[0] + 311, $Clientpos[1] + 490]
Global $BoxColor = 0xB29D7B

;ChatBox
Global $ChatBoxPosition[4] = [$Clientpos[0] + 50, $Clientpos[1] + 470, $Clientpos[0] + 499, $Clientpos[1] + 484]
Global $ChatBoxColor = 0x0000FF


;Hotkeys
Global $Paused
HotKeySet("`", "Stop") ;Stops when ` is pressed
HotKeySet("~", "Stop") ;Stops when ~ is pressed
;HotKeySet("`", "TogglePause")

MsgBox(0, $scriptName, "Starting up... Press the ~ key to stop.")
$Timer = TimerInit()
WinActivate($clientName)
WinWaitActive($clientName)

;TEST

Func TestChatBox()
	MouseMove($ChatBoxPosition[0], $ChatBoxPosition[1])
	ToolTip("ChatBox - Top Left")
	Sleep(1000)
	MouseMove($ChatBoxPosition[2], $ChatBoxPosition[3])
	ToolTip("ChatBox - Bottom Right")
	Sleep(1000)
EndFunc   ;==>TestChatBox

Func TestBox()
	MouseMove($BoxPosition[0], $ChatBoxPosition[1])
	ToolTip("Box - Top Left")
	Sleep(1000)
	MouseMove($BoxPosition[2], $ChatBoxPosition[3])
	ToolTip("Box - Bottom Right")
	Sleep(1000)
EndFunc   ;==>TestBox

Func TestScreen()
	MouseMove($GameScreen[0], $GameScreen[1])
	ToolTip("Game Screen - Top Left")
	Sleep(1000)
	MouseMove($GameScreen[2], $GameScreen[3])
	ToolTip("Game Screen - Bottom Right")
	Sleep(1000)
EndFunc   ;==>TestScreen

Func TestItems()
	$Item1Pos = PixelSearch($Inventory[0], $Inventory[1], $Inventory[2], $Inventory[3], $item1Color)
	MouseMove($Item1Pos[0], $Item1Pos[1])
	ToolTip("Item1")
	Sleep(1000)
	$Item2Pos = PixelSearch($Inventory[0], $Inventory[1], $Inventory[2], $Inventory[3], $item2Color)
	MouseMove($Item2Pos[0], $Item2Pos[1])
	ToolTip("Item2")
	Sleep(1000)
	$Item3Pos = PixelSearch($Inventory[0], $Inventory[1], $Inventory[2], $Inventory[3], $item3Color)
	MouseMove($Item3Pos[0], $Item3Pos[1])
	ToolTip("Item3")
	Sleep(1000)
EndFunc   ;==>TestItems

Func TestSlot27()
	MouseMove($Slot27[0], $Slot27[1])
	ToolTip("Slot27 TL")
	Sleep(1000)
	MouseMove($Slot27[2], $Slot27[3])
	ToolTip("Slot27 BR")
	Sleep(1000)
EndFunc   ;==>TestSlot27

;END TEST

Func CutTree()
	$CutPos = PixelSearch($GameScreen[0], $GameScreen[1], $GameScreen[2], $GameScreen[3], $CutColor)
	MouseMove($CutPos[0], $CutPos[1])
	ToolTip("Clicking Tree")
	MouseClick($lc)
	;loop
	Do
		Sleep(99)
		$Slot27Search = PixelSearch($Slot27[0], $Slot27[1], $Slot27[2], $Slot27[3], $item2Color)
		If Not @error Then
			AlertOnChat()
			ToolTip("INV FULL")
			$InvFull = True
		Else
			AlertOnChat()
			ToolTip("INV NOT FULL")
			$InvFull = False
		EndIf
	Until $InvFull == True
EndFunc   ;==>CutTree

Func CombineItems()
	;Get positions for both items
	$Inventory1Pos = PixelSearch($Inventory[0], $Inventory[1], $Inventory[2], $Inventory[3], $item1Color)
	$Inventory2Pos = PixelSearch($Inventory[0], $Inventory[1], $Inventory[2], $Inventory[3], $item2Color)
	;click item 1
	ToolTip("Clicking item1")
	MouseMove($Inventory1Pos[0], $Inventory1Pos[1])
	MouseClick($lc)
	;click item 2
	ToolTip("Clicking item2")
	MouseMove($Inventory2Pos[0], $Inventory2Pos[1])
	MouseClick($lc)
	;wait for input box
	Do
		ToolTip("Waiting for box")
		Sleep(99)
		PixelSearch($BoxPosition[0], $BoxPosition[1], $BoxPosition[2], $BoxPosition[3], $BoxColor)
		If Not @error Then
			$ItemWithdraw = True
			ToolTip("Box opened")
		Else
			$ItemWithdraw = False
		EndIf
	Until $ItemWithdraw == True
	;end wait for input box

	;press 1
	Send("2")
EndFunc   ;==>CombineItems

Func WaitForCompletion()
	ToolTip("Waiting to finihsh")
	;wait for item to disappear
	Do
		Sleep(99)
		$Inventory1Pos = PixelSearch($Inventory[0], $Inventory[1], $Inventory[2], $Inventory[3], $item2Color)

		If Not @error Then
			AlertOnChat()
			$item2gone = False
		Else
			$item2gone = True
		EndIf
	Until $item2gone == True
EndFunc   ;==>WaitForCompletion

Func DropItem3()
	Send("{SHIFTDOWN}")
	Do
		PixelSearch($Inventory[0], $Inventory[1], $Inventory[2], $Inventory[3], $item3Color)
		If Not @error Then
			$Item3Pos = PixelSearch($Inventory[0], $Inventory[1], $Inventory[2], $Inventory[3], $item3Color)
			ToolTip("Dropping Item3")
			$item3gone = False
			MouseMove($Item3Pos[0], $Item3Pos[1])
			MouseClick($lc)

			For $i = 35 To 105 Step 35
				$Item3Pos1 = PixelSearch($Item3Pos[0]+$i, $Item3Pos[1], $Inventory[2]+$i, $Inventory[3], $item3Color)
				If Not @error Then
					ToolTip("Dropping Item3")
					$item3gone = False
					MouseMove($Item3Pos1[0], $Item3Pos[1])
					MouseClick($lc)
				EndIf
				Sleep(300)
			Next
		Else
			Send("{SHIFTUP}")
			$item3gone = True
		EndIf
	Until $item3gone == True
EndFunc   ;==>DropItem3

Func AlertOnChat()
	If $Alerts == True Then
		PixelSearch($ChatBoxPosition[0], $ChatBoxPosition[1], $ChatBoxPosition[2], $ChatBoxPosition[3], $ChatBoxColor)
		If Not @error Then
			;ToolTip("CHAT!")
			Beep(3000, 1000)
		Else
			;ToolTip("No Chat!")
		EndIf
	EndIf
EndFunc   ;==>AlertOnChat

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
	CutTree()
	CombineItems()
	WaitForCompletion()
	DropItem3()
	AlertOnChat()
WEnd
;TestScreen()
;TestItems()
;TestSlot27()
;DropItem3()
