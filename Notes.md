# Headings 
`# First-level heading`
`## Second-level heading`
`### Third-level heading`

# Style
Style	Syntax	Keyboard shortcut (if any)	Example	Output

Bold	`** **` or `__ __`	Ctrl+B (Windows/Linux)	**This is bold text**

Italic	`* *` or `_ _`     	Ctrl+I (Windows/Linux)	_This text is italicized_

Strikethrough	`~~ ~~`	~~This was mistaken text~~

Bold and nested italic	`** **` and `_ _`	**This text is _extremely_ important**

All bold and italic	`*** ***`	***All this text is important***

Subscript	`<sub> </sub>`	This is a <sub>subscript</sub> text

Superscript	`<sup> </sup>`	This is a <sup>superscript</sup> text

Underline	`<ins> </ins>`	This is an <ins>underlined</ins> text

# Quotes
You can quote text with a `>`.

Text that is not a quote

> Text that is a quote


# Supported color models
In issues, pull requests, and discussions, you can call out colors within a sentence by using backticks. A supported color model within backticks will display a visualization of the color.


The background color is `#ffffff` for light mode and `#000000` for dark mode.

Screenshot of rendered GitHub Markdown showing how HEX values within backticks create small circles of color. #ffffff shows a white circle, and #000000 shows a black circle.

Here are the currently supported color models.

Color	Syntax	Example	Output

HEX	`#RRGGBB`	`#0969DA`	Screenshot of rendered GitHub Markdown showing how HEX value #0969DA appears with a blue circle.

RGB	`rgb(R,G,B)`	`rgb(9, 105, 218)`	Screenshot of rendered GitHub Markdown showing how RGB value 9, 105, 218 appears with a blue circle.

HSL	`hsl(H,S,L)`	`hsl(212, 92%, 45%)`	Screenshot of rendered GitHub Markdown showing how HSL value 212, 92%, 45% appears with a blue circle.

Note

A supported color model cannot have any leading or trailing spaces within the backticks.

The visualization of the color is only supported in issues, pull requests, and discussions.

# Notes
> [!NOTE]  
> Highlights information that users should take into account, even when skimming.

> [!TIP]
> Optional information to help a user be more successful.

> [!IMPORTANT]  
> Crucial information necessary for users to succeed.

> [!WARNING]  
> Critical content demanding immediate user attention due to potential risks.

> [!CAUTION]
> Negative potential consequences of an action.

# Code Blocks
Call out specific code using \`
Check out this example `code`

Call out multiple lines of code using \`\`\`
```
This is
Multiple
Lines of
Code
```

# Lists
You can make an unordered list by preceding one or more lines of text with `-`, `*`, or `+`.
```
- List item
* List item
+ List item
```
- List item
* List item
+ List item

## Nested Lists
1. List item
    - 1st nested item
        - 2nd nested item
2. 2nd list item
    - nested
3. 3rd list item

```
1. List item
    - 1st nested item
        - 2nd nested item
2. 2nd list item
    - nested
3. 3rd list item
```


## Tasklist
- [] to do
- [] to do
- [x] done
- [] \(Something) I needed to escape