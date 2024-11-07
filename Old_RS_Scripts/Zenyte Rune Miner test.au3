

;Hotkeys
Global $Paused
HotKeySet("`", "Stop") ;Stops when ` is pressed
HotKeySet("~", "Stop") ;Stops when ~ is pressed
;HotKeySet("`", "TogglePause")

MsgBox(0,"", "Starting up... Press the ~ key to stop.")
Global $OreColor = 0x42351B
Global $MinePos = PixelSearch(6,29, 2280, 1397, 0x42351B, 100)
MouseMove($MinePos[0], $MinePos[1])