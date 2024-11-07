#clear newstring variable and set list of invalid base64 characters to use
$newString = ""
$invalidCharacters = "!@#$%^&()\|[]{}<>?"
$String = $args[0]
if ($String -eq $null){
    #Get user input
    $String = Read-Host "Enter message to encode"
}
#Encode message to base64
$EncodedString = [Convert]::ToBase64String([Text.Encoding]::Unicode.GetBytes($String))

#Convert string to char array so we can loop through it
$msg = $EncodedString.toCharArray()

#Get random invalid character(s) and make a new string
foreach ($letter in $msg) {
  $newString += $letter
  For ($i=0; $i-le(Get-Random -Minimum 0 -Maximum 3); $i++){
      $ranomCharacter = $invalidCharacters[(Get-Random -Minimum 0 -Maximum $invalidCharacters.Length)]
      $newString += $ranomCharacter
  }
}

#output new base64 string
$newString
#copy string to clipboard
$newString | clip