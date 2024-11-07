$ValidChars = @("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9","+","/")
$newString = "";$string = "";$solved = "";$s = ""

$String = $args[0]
if ($String -eq $null){
    $String = Read-Host "Enter message to decode"
}

$charArray = $string.ToCharArray()
foreach ($char in $charArray){
    if ($ValidChars -contains "$char"){
        $newString += $char
    }
}

switch ($newString.Length % 4) {
    0 { break; }             # no padding needed
    2 { $newString += '=='; break; } # two pad chars
    3 { $newString += '='; break; }  # one pad char
    default { throw "Invalid Base64Url string" }
}

$solved = $([Text.Encoding]::UTF8.GetString([Convert]::FromBase64String($newString)).replace("`0",''))

$solved; $solved | clip
