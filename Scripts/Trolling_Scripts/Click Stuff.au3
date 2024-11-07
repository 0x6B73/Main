HotKeySet("{ESC}", "Terminate")


;GameScreen
Global $ItemColor = 0x6D580D
Global $InvScreen[4] = [1710,740,1890,1000]
Global $invChunk1[4] = [1710,740,1759,1000]
Global $invChunk2[4] = [1760,740,1799,1000]
Global $invChunk3[4] = [1800,740,1839,1000]
Global $invChunk4[4] = [1840,740,1890,1000]


Global $interval = 10000

Func Terminate()
   Exit 1
EndFunc

func ShowClientPos()
	ToolTip("Showing Client Pos")
	MouseMove ($InvScreen[0],$InvScreen[1])
	MouseMove ($InvScreen[2],$InvScreen[3])
EndFunc
;ShowClientPos()

Func ClickItem()
	ToolTip("Clickng Item")
	$ItemPos = PixelSearch($InvScreen[0], $InvScreen[1], $InvScreen[2], $InvScreen[3], $ItemColor)
	if IsArray($ItemPos) Then
		MouseClick("left", $ItemPos[0], $ItemPos[1])
		;Sleep($interval)
	Else
		MsgBox(0,"","ITEM NOT FOUND")
		Terminate
	EndIf
	Sleep(50)
EndFunc

Func ClickMultiItem()
	For $i = 5 To 1 Step -1
		ToolTip("Clickng Item" & $i & ".")
		Local $CurrentChunk = "$invChunk" & $i
		$ItemPos = PixelSearch($CurrentChunk[0], $CurrentChunk[1], $CurrentChunk[2], $CurrentChunk[3], $ItemColor)
		if IsArray($ItemPos) Then
			MouseClick("left", $ItemPos[0], $ItemPos[1])
			;Sleep($interval)
		Else
			MsgBox(0,"","ITEM NOT FOUND")
			Terminate
	EndIf
	Sleep(50)
	Next

EndFunc

While(1)
	;ClickItem()
	ClickMultiItem()
WEnd