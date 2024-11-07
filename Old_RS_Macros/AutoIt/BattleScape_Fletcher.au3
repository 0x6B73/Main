#include <Constants.au3>
Global $scriptName = "BattleScape Fletcher"
Global $clientName = "BattleScape"

Global $lc = "left"
Global $rc = "right"

Global $Clientpos = WinGetPos("BattleScape - Powered by RuneLite")

Global $Alerts = True

;Bank
Global $BankLeft = 385
Global $BankTop = 190
Global $BankRight = 435
Global $BankBottom = 245
Global $BankColor = 0x5C4615

;CorrectedBank
Global $CorrectedBank[4] = [385+$Clientpos[0],190+$Clientpos[1],435+$Clientpos[0],245+$Clientpos[1]]

;Deposit
Global $DepositLeft = 433
Global $DepositTop = 325
Global $DepositRight = 460
Global $DepositBottom =350
Global $DepositColor = 0x73431A

;Inventory
Global $InventoryLeft = 560
Global $InventoryTop = 235
Global $InventoryRight = 730
Global $InventoryBottom =490

;Item 1
Global $item1Left = 410
Global $item1Top = 170
Global $item1Right = 440
Global $item1Bottom =200
Global $item1Color = 0x155751

;Item 2
Global $item2Left = 410
Global $item2Top = 105
Global $item2Right = 440
Global $item2Bottom =300
Global $item2Color = 0x837758

;Box
Global $BoxPosition[4] = [$Clientpos[0]+216,$Clientpos[1]+420,$Clientpos[0]+311,$Clientpos[1]+490]
Global $BoxColor = 0xB29D7B

;ChatBox
Global $ChatBoxPosition[4] = [$Clientpos[0]+50,$Clientpos[1]+470,$Clientpos[0]+499,$Clientpos[1]+484]
Global $ChatBoxColor = 0x0000FF



;Hotkeys
Global $Paused
HotKeySet("`","Stop") ;Stops when ` is pressed
;HotKeySet("`", "TogglePause")

MsgBox ( 0, $scriptName, "Starting up... Press the ~ key to stop." )
WinActivate ($clientName)
WinWaitActive ($clientName)

Func TestBank()
	MouseMove ($Clientpos[0]+$BankLeft,$Clientpos[1]+$BankTop)
	ToolTip("Bank - Top Left")
	Sleep(1000)
	MouseMove ($Clientpos[0] + $BankRight, $Clientpos[1] + $BankBottom)
	ToolTip("Bank - Bottom Right")
	Sleep(1000)
EndFunc

Func TestChatBox()
	MouseMove ($ChatBoxPosition[0],$ChatBoxPosition[1])
	ToolTip("ChatBox - Top Left")
	Sleep(1000)
	MouseMove ($ChatBoxPosition[2],$ChatBoxPosition[3])
	ToolTip("ChatBox - Bottom Right")
	Sleep(1000)
EndFunc


Func TestBox()
	MouseMove ($BoxPosition[0],$ChatBoxPosition[1])
	ToolTip("Box - Top Left")
	Sleep(1000)
	MouseMove ($BoxPosition[2],$ChatBoxPosition[3])
	ToolTip("Box - Bottom Right")
	Sleep(1000)
EndFunc

Func OpenBank()
	; Find bank and click it
	Local $BankPos = PixelSearch ( $Clientpos[0] + $BankLeft,$Clientpos[1]+$BankTop, $Clientpos[0]+$BankRight, $Clientpos[1]+$BankBottom, $BankColor )
	If Not @error Then
	   ;if pixel colors are found, move mouse to bank and click.
	MouseMove ($BankPos[0], $BankPos[1] )
	MouseClick ($lc)
	;wait for bank
	    Do
			ToolTip("Waiting for bank to open")
			Sleep(99)
			PixelSearch($Clientpos[0]+26,$Clientpos[1]+39,$Clientpos[0]+501,$Clientpos[1]+61,0x483E33)
			If Not @error Then
				$BankOpen = True
				ToolTip("Bank Opened")
			Else
				$BankOpen = False
			EndIf
		Until $BankOpen == True
	;end wait for bank
	Else
	   MsgBox (0,"","Bank not found")
	EndIf
EndFunc

Func Deposit()
	; Find deposit button and click it
	Local $DepositPos = PixelSearch ( $Clientpos[0]+$DepositLeft, $Clientpos[1]+$DepositTop, $Clientpos[0]+$DepositRight, $Clientpos[1]+$DepositBottom, $DepositColor )
	If Not @error Then
	;if pixel colros are found for deposit button, moves and left clicks
	ToolTip("Depositing")
	MouseMove($DepositPos[0],$DepositPos[1])
	MouseClick($lc)
	Else
	  MsgBox(0,"","I don't see the deposit button!")
	EndIf
	sleep(1200)
EndFunc

Func WithdrawItem1()
	$item1Pos = PixelSearch ( $Clientpos[0]+$item1Left, $Clientpos[1]+$item1Top, $Clientpos[0]+$item1Right, $Clientpos[1]+$item1Bottom, $item1Color )
	ToolTip("Withdrawing item 1")
	If Not @error Then
		MouseMove ($item1Pos[0],$item1Pos[1])
		Sleep(100)
		MouseClick($rc)
		Sleep(100)
		MouseMove ($item1Pos[0],$item1Pos[1]+70)
		Sleep(100)
		MouseClick($lc)
		;wait for item
	    Do
			ToolTip("Waiting for item1")
			Sleep(99)
			PixelSearch($Clientpos[0]+$InventoryLeft,$Clientpos[1]+$InventoryTop,$Clientpos[0]+$InventoryRight,$Clientpos[1]+$InventoryBottom,$item1Color)
			If Not @error Then
				$item1Withdraw = True
				ToolTip("Item1 Withdrew")
			Else
				$item1Withdraw = False
			EndIf
		Until $item1Withdraw == True
		;end wait for Item
	Else
		MsgBox ( 0, "", "I don't see the item!")
	EndIf
EndFunc

Func WithdrawItem2()
	 $item2Pos = PixelSearch ( $Clientpos[0]+$item2Left, $Clientpos[1]+$item2Top, $Clientpos[0]+$item2Right, $Clientpos[1]+$item2Bottom, $item2Color )
	ToolTip("Withdrawing item 2")
	If Not @error Then
		MouseMove ($item2Pos[0],$item2Pos[1])
		Sleep(100)
		MouseClick($rc)
		Sleep(100)
		MouseMove ($item2Pos[0],$item2Pos[1]+70)
		Sleep(100)
		MouseClick($lc)
		;wait for item
	    Do
			ToolTip("Waiting for item2")
			Sleep(99)
			PixelSearch($Clientpos[0]+$InventoryLeft,$Clientpos[1]+$InventoryTop,$Clientpos[0]+$InventoryRight,$Clientpos[1]+$InventoryBottom,$item2Color)
			If Not @error Then
				$item2Withdraw = True
				ToolTip("Item2 Withdrew")
			Else
				$item2Withdraw = False
			EndIf
		Until $item2Withdraw == True
		;end wait for Item
	Else
		MsgBox ( 0, "", "I don't see the item!")
	EndIf
EndFunc

Func CloseBank()
	ToolTip("Closing Bank")
	Send("{ESC}")
EndFunc

Func CombineItems()
	;Get positions for both items
	Local $Inventory1Pos = PixelSearch ( $Clientpos[0]+$InventoryLeft, $Clientpos[1]+$InventoryTop, $Clientpos[0]+$InventoryRight, $Clientpos[1]+$InventoryBottom, $Item1Color )
	Local $Inventory2Pos = PixelSearch ( $Clientpos[0]+$InventoryLeft, $Clientpos[1]+$InventoryTop, $Clientpos[0]+$InventoryRight, $Clientpos[1]+$InventoryBottom, $Item2Color )
	;click item 1
	ToolTip("Clicking item1")
	MouseMove ($Inventory1Pos[0],$Inventory1Pos[1])
	MouseClick($lc)
	;click item 2
	ToolTip("Clicking item2")
	MouseMove ($Inventory2Pos[0],$Inventory2Pos[1])
	MouseClick($lc)
	;wait for input box
	Do
		ToolTip("Waiting for box")
		Sleep(99)
		PixelSearch($BoxPosition[0],$BoxPosition[1],$BoxPosition[2],$BoxPosition[3],$BoxColor)
		If Not @error Then
			$ItemWithdraw = True
			ToolTip("Box opened")
		Else
			$ItemWithdraw = False
		EndIf
	Until $ItemWithdraw == True
	;end wait for input box

	;press 1
	Send("1")
EndFunc

Func WaitForCompletion()
	ToolTip("Waiting to finihsh")
		;wait for item to disappear
	    Do
			Sleep(99)
			PixelSearch($Clientpos[0]+$InventoryLeft,$Clientpos[1]+$InventoryTop,$Clientpos[0]+$InventoryRight,$Clientpos[1]+$InventoryBottom,$item2Color)
			If Not @error Then
				AlertOnChat()
				$item2gone = False
			Else
				$item2gone = True
			EndIf
		Until $item2gone == True
EndFunc

Func AlertOnChat()
	If $Alerts == True Then
		PixelSearch($ChatBoxPosition[0],$ChatBoxPosition[1],$ChatBoxPosition[2],$ChatBoxPosition[3],$ChatBoxColor)
		If Not @error Then
			ToolTip("CHAT!")
			Beep(3000, 1000)
		Else
			ToolTip("No Chat!")
		EndIf
	EndIf
EndFunc

Func Stop()
   MsgBox (0, $scriptName, "Script stopped!" )
   Exit
EndFunc

Func TogglePause()
	$Paused = not $Paused
	While $Paused
		MsgBox (0, $scriptName, $scriptName + "Script paused")
	sleep(100)
	WEnd
EndFunc

;;;;;;;;;; SCRIPT ;;;;;;;;;;
While True
	OpenBank()
	Deposit()
	WithdrawItem1()
	WithdrawItem2()
	CloseBank()
	CombineItems()
	WaitForCompletion()
	AlertOnChat()
Wend