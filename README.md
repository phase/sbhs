#Sonic Battle Hacking
Character sprites are left uncompressed, which makes it annoying to insert new images. Everything else is LZ77 compressed, so you can use unLZ to edit them.

##Documentation
All docs on Sonic Battle reverse engineering are in (**`DOCUMENTATION.md`**)[DOCUMENTATION.md].

##Talking Text
Talking text, otherwise known as the speech of the story, is left in a "compressed" hex format in the code. Here's a table of it.

```
Character|Hex Value|Character seen in a hex editor (ASCII Value)
-|-|-
!|01|N/A
"|02|N/A
#|03|N/A
$|04|N/A
%|05|N/A
&|06|N/A
'|07|N/A
(|08|N/A
)|09|N/A
*|0A|N/A
+|0B|N/A
,|0C|N/A
-|0D|N/A
/|0F|N/A
0|10|N/A
1|11|N/A
2|12|N/A
3|13|N/A
4|14|N/A
5|15|N/A
6|16|N/A
7|17|N/A
8|18|N/A
9|19|N/A
:|1A|N/A
;|1B|N/A
<|1C|N/A
=|1D|N/A
>|1E|N/A
?|1F|N/A
@|20|Space
A|21|!
B|22|"
C|23|#
D|24|$
E|25| %
F|26|&
G|27|'
H|28|(
I|29|)
J|2A|*
K|2B|+
L|2C|,
M|2D|-
N|2E|.
O|2F|/
P|30|0
Q|31|1
R|32|2
S|33|3
T|34|4
U|35|5
V|36|6
W|37|7
X|38|8
Y|39|9
Z|3A|:

Story|Offsets
-|-
Sonic's story text|1DB468 - 1E1469
Tails' story text|1E146A - 1E6FA7
Rouge's story text|1E6FA8 - 1ED2C3
Knuckles' story text|1ED2C4 - 1F417F
Amy's story text|1F4180 - 1F9CDB
Cream's story text|1F9CDC - 1FE86F
Shadow's story text|1FE870 - 206103
Emerl's story text|206104 - 20B131
```

##unLZ-gba offsets:
###Overworld
Overworld char sprites go from 363 to 372. Before that are a bunch of option menus and whatnot, more on that later.

To get a view of the sprites normally, hit minus all the way, then plus 3.

###Talking
Talking animations begin at 393 with Sonic and go to 425, ending with E-102.