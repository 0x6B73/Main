 <#
Input Morse code or plain text using "." for a dit and "-" or "_" for a dah.
Letters in Morse code should be separated by spaces and words by "/" or "|".
#>

 function MorseCode
	{
	[cmdletbinding()]
	param (
		[parameter(mandatory=$true,
		valuefrompipeline=$true)]
		[string]$input
	)

	$indexArray =@(
		'a','b','c','d','e','f','g','h','i','j','k',
		'l','m','n','o','p','q','r','s','t','u','v',
		'w','x','y','z','0','1','2','3','4','5','6',
		'7','8','9'
	)
	$morseArray =@(
		'AB','BAAA','BABA','BAA','A','AABA','BBA',
		'AAAA','AA','ABBB','BAB','ABAA','BB','BA',
		'BBB','ABBA','BBAB','ABA','AAA','B','AAB',
		'AAAB','ABB','BAAB','BABB','BBAA','BBBBB',
		'ABBBB','AABBB','AAABB','AAAAB','AAAAA',
		'BAAAA','BBAAA','BBBAA','BBBBA'
	)

	if ($input -match "[a-z]")
		{
		($input.ToCharArray() | %{ ($_ + ':') }).ToCharArray() |
		% {
			if ($_ -eq ':')
				{
				#letter timing
				write-host ' ' -n
				sleep -m 300
				return
			}
			if ($_ -eq ' ')
				{
				#word timing = (letter + word timing)
				write-host '|' -n
				sleep -m 600
				return
			}

			$i = $indexArray.indexof("$_")
			$morseArray[$i] -split "(A)" |
			% {
				if ($_ -eq 'A') { dit }
				if ($_ -eq 'B') { dah }
			}
		}
		write ''
	}
	else {
		$input | %{ $_.split(" ")} | %{ $_ -replace "_","-" } |
		% {
			if ($_ -eq '/' -OR $_ -eq '|')
				{
				$charArray += ' '
				return
			}
			if (!($morseArray.contains("$_")))
				{
				$charArray += '?'
				return
			}
			$i = $morseArray.indexof("$_")
			$charArray += $indexArray[$i]
		}
		write-host "$charArray"
	}
 }

 function dit
	{
	write-host "." -n
	[console]::beep(600,100)
 }

 function dah
	{
	write-host "-" -n
	[console]::beep(600,300)
 }

 
 $args[0] | MorseCode

 remove-variable * -erroraction silentlycontinue