function decode-snake([string]$string){
  $regEx = '[a-zA-Z\d+/]'
  
  foreach ($c in $string.ToCharArray()){
    if ($c -match $regEx){
      $newString += $c
    }
  }
  
  switch ($newString.Length % 4) {
    0 { break; }             # no padding needed
    2 { $newString += '=='; break; } # two pad chars
    3 { $newString += '='; break; }  # one pad char
    default { throw "Invalid Base64Url string" }
  }
  return $([Text.Encoding]::UTF8.GetString([Convert]::FromBase64String($newString)).replace("`0",''))
}

decode-snake 'S)^Q^|B?m|&A{C<&(A&!A|@#e&%Q<B#v$?}A#H$U$[<A?{I<@[A#{B??j{A\|G[E>(A{#b<$g$)A}&g[^A^H}?I!A!Z<@Q<>(B|%h<A<G|Q|^A<(I[A!]B{?0]<A)G[{[g$}A&a><Q[B%?{z<{A!}C(A^\%A^)b<Q![$B!)5<
!A^!?C#@A|&A$c?)^w}#B#o>>}A\G{$k|}A^|d#)[A]&>A[g&A}}H|%c\A|^]b@&w(%B{&y{#A!G)}$s<?^A$|Z(Q)(B<[k!\A}(A&\}=%{#=<'