<#

.SYNOPSIS
This is a simple Powershell CMDlet to generate obfuscated base64

.DESCRIPTION
Takes an input string and converts it to base64. It also inserts random junk into the base64
to make it look like garbage data.

.EXAMPLE
Get-Snake64 -encode "I love Python"

.NOTES
Pretty simple, just run Get-Snake64

.LINK

#>

function Get-Snake64([SWITCH]$Help, [string]$encode){
$msgUsuage = @"
USAGE:

    Get-Snake64 -encode "I love Python"

EXAMPLE:

    `$snake64 = Get-Snake64 -encode "I love Python"

"@
	
	if (!$Help -AND $encode.length -gt 0){
    $nString = ""
    $invalidCharacters = "!@#$%^&()\|[]{}<>?"
    $EncodedString = [Convert]::ToBase64String([Text.Encoding]::Unicode.GetBytes($encode))

    foreach ($c in $EncodedString.toCharArray()) {
      $nString += $c
      For ($i=0; $i-le(Get-Random -Minimum 0 -Maximum 3); $i++){
          $ranomCharacter = $invalidCharacters[(Get-Random -Minimum 0 -Maximum $invalidCharacters.Length)]
          $nString += $ranomCharacter
      }
    }
    return $nString
	}else{
		Write-host "$msgUsuage"
	}
}
Export-ModuleMember -Function Get-Snake64
