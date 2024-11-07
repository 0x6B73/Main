HotKeySet("{ESC}", "Terminate")



Global $Clientpos = WinGetPos("RuneLite - Hc Allza")

;GameScreen
;Global $GameScreen[4] = [8 + $Clientpos[0], 31 + $Clientpos[1], 519 + $Clientpos[0], 364 + $Clientpos[1]]
Global $GameScreen[4] = [0,0,1900,1040]
Global $smelterColor = 0xFF0000
Global $OreColor = 0x475B48
Global $CoalColor = 0x11110C
Global $bankColor = 0xFFAFAF
Global $depositColor = 0x88672C


Global $interval = 10000

Func Terminate()
   Exit 1
EndFunc

func ShowClientPos()
	ToolTip("Showing Client Pos")
	MouseMove ($GameScreen[0],$GameScreen[1])
	MouseMove ($GameScreen[2],$GameScreen[3])
EndFunc
;ShowClientPos()

Func ClickOre()
	ToolTip("Clickng Ore")
	$OrePos = PixelSearch($GameScreen[0], $GameScreen[1], $GameScreen[2], $GameScreen[3], $OreColor)
	if IsArray($OrePos) Then
		MouseClick("left", $OrePos[0], $OrePos[1])
		Sleep($interval)
	Else
		MsgBox(0,"","ORE NOT FOUND")
	EndIf
	Sleep(50)
EndFunc
;ClickAddyOre()

Func ClickSmelter()
	ToolTip("Clickng Smelter")
	$smelter = PixelSearch($GameScreen[0], $GameScreen[1], $GameScreen[2], $GameScreen[3], $smelterColor)
	if IsArray($smelter) Then
		MouseClick("left", $smelter[0], $smelter[1])
		Sleep($interval)
	Else
		MsgBox(0,"","SMELTER NOT FOUND")
	EndIf
	Sleep(50)
EndFunc
;ClickSmelter()

Func ClickBank()
	ToolTip("Clickng Bank")
	$bankPos = PixelSearch($GameScreen[0], $GameScreen[1], $GameScreen[2], $GameScreen[3], $bankColor)
	if IsArray($bankPos) Then
		MouseClick("left", $bankPos[0], $bankPos[1])
		Sleep($interval)
	Else
		ToolTip("Bank NOT FOUND")
	EndIf
	Sleep(50)
EndFunc
;ClickBank()

Func ClickDeposit()
	;MsgBox(0,"","Clickng Deposit All")
	ToolTip("Clicking Deposit Button")
	$depositPos = PixelSearch($GameScreen[0], $GameScreen[1], $GameScreen[2], $GameScreen[3], $depositColor)
	if IsArray($depositPos) Then
		MouseClick("left", $depositPos[0], $depositPos[1])
		Sleep($interval)
	Else
		MsgBox(0,"","Deposit Button NOT FOUND")
	EndIf
	Sleep(50)
EndFunc
;ClickDeposit()

Func withdrawOres()
	ToolTip("Withdrawing Ore")
	$OrePos = PixelSearch($GameScreen[0], $GameScreen[1], $GameScreen[2], $GameScreen[3], $OreColor, 2)
	if IsArray($OrePos) Then
		MouseClick("right", $OrePos[0], $OrePos[1])
		Sleep(50)
		MouseClick("left", $OrePos[0], $OrePos[1]+60)
		Sleep($interval)
	Else
		MsgBox(0,"","ORE NOT FOUND")
	EndIf
	Sleep(50)
EndFunc

Func withdrawCoal()
	ToolTip("Withdrawing Coal")
	$CoalPos = PixelSearch($GameScreen[0], $GameScreen[1], $GameScreen[2], $GameScreen[3], $CoalColor, 2)
	if IsArray($CoalPos) Then
		MouseClick("right", $CoalPos[0], $CoalPos[1])
		Sleep(50)
		MouseClick("left", $CoalPos[0], $CoalPos[1]+60)
		Sleep($interval)
	Else
		MsgBox(0,"","ORE NOT FOUND")
	EndIf
	Sleep(50)
EndFunc
withdrawOres()
withdrawCoal()