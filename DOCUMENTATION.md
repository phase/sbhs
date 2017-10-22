Documentation
=============
A lot of information in this document
came from [Sonic Retro](http://info.sonicretro.org/index.php?title=SCHG:Sonic_Battle),
though I did my fair share of adding to it. There is also a lot of information in the
code that needs to be added to here.

Text Editing
------------

Several instances of text can be found in the game. The format goes as
follows:

-   For capital letters, use the character table below.
-   For lowercase letters, use capital letters.
-   Put `00` between each letter.

For the rest of the guide, we will refer to the lowercase letters as the
capital letter part and the uppercase as the formatted letter part.

`FE FF` indicates that the text box now becomes blank, and will be
followed by the next line of text in the ROM. Use FE FF at the end of
text boxes.

`FD FF` ends a line of text.

`FB FF ??` is a value for changing colors. Here is a table:

| Hex  | Color  |
|------|--------|
| `03` | Black  |
| `04` | Red    |
| `05` | Blue   |
| `06` | Green  |
| `07` | Purple |

-   `00` is a space for the capital letter part.
-   `0E` is a period for the capital letter part.
-   The periods in the format = `0e`
-   `1f` = _?_ and `01` = _!_ in the capital letter part.
-   `07` = _'_ in the capital letter part
-   `02` = _"_ in the capital letter part
-   _+_ = `1` and _/_ = `0` in the capital letter part
-   `08` = _(_ and `09` = _)_ in the capital letter part.
-   `0a` = _..._ in the capital letter part

For example, the string for _Chaos Emeralds_ would look like:
`23 00 48 00 41 00 4F 00 53 00 00 00 25 00 4D 00 45 00 52 00 41 00 4C 00 44 00 53 00 FB FF`

In your hex editor, it looks like: `\#.H.A.O.S...%.M.E.R.A.L.D.S..`

### Offsets

All these offsets are for the English text, not the foreign language
text; you'll have to search for the foreign language yourself, since I
only know English and therefore can't list any offsets.

| Description                | Offset (EU)     | Offset (US)     |
|----------------------------|-----------------|-----------------|
| Skills?                    |                 | 0C6D3C - 109E9F |
| Sonic's story text         | 1DC290 - 1E2269 | 1DB468 - 1E1469 |
| Tails' story text          | 1E2300 - 1E7E29 | 1E146A - 1E6FA7 |
| Rouge's story text         | 1E7E29 - 1EE145 | 1E6FA8 - 1ED2C3 |
| Knuckles' story text       | 1EE145 - 1F5000 | 1ED2C4 - 1F417F |
| Amy's story text           | 1F5000 - 1FAB5D | 1F4180 - 1F9CDB |
| Cream's story text         | 1FAB5D - 1FF6F5 | 1F9CDC - 1FE86F |
| Shadow's story text        | 1FF6F5 - 206F70 | 1FE870 - 206103 |
| Emerl's story text         | 206F70 - 20DA83 | 206104 - 20B131 |
| Prof. Gerald's Journal     | 20DA83 - 2104B9 | (see below)     |
| Battle win/lose/start text | 2104B9 - 212115 |                 |
| Phi text                   | 212115 - 2121A9 |                 |
| Miscellaneous text         | 2121A9 - 212585 |                 |
| SONICTEAM access text      | 212585 - 212715 |                 |
| Journal & Misc. Text       |                 | 20CC1A - 2118A7 |

NOTE: 1E2269 - 1E22FE may be lost text. It reads "Find out the forgotten
secret of Emerl in the next episode, 'Tails'!"

### Character Table

Note that if you subtract _$20_ to a character from the ROM, it will give you
that character in ASCII. Hence all lowercase letters are uppercase in
the ROM and will not be listed here.

Here is the character table:

| Character | Hex Value | Character seen in a hex editor |
|-----------|-----------|--------------------------------|
| !         | 01        | N/A                            |
| "         | 02        | N/A                            |
| \#        | 03        | N/A                            |
| \$        | 04        | N/A                            |
| %         | 05        | N/A                            |
| &         | 06        | N/A                            |
| '         | 07        | N/A                            |
| (         | 08        | N/A                            |
| )         | 09        | N/A                            |
| \*        | 0A        | N/A                            |
| +         | 0B        | N/A                            |
| ,         | 0C        | N/A                            |
| -         | 0D        | N/A                            |
| /         | 0F        | N/A                            |
| 0         | 10        | N/A                            |
| 1         | 11        | N/A                            |
| 2         | 12        | N/A                            |
| 3         | 13        | N/A                            |
| 4         | 14        | N/A                            |
| 5         | 15        | N/A                            |
| 6         | 16        | N/A                            |
| 7         | 17        | N/A                            |
| 8         | 18        | N/A                            |
| 9         | 19        | N/A                            |
| :         | 1A        | N/A                            |
| ;         | 1B        | N/A                            |
| &lt;      | 1C        | N/A                            |
| =         | 1D        | N/A                            |
| &gt;      | 1E        | N/A                            |
| ?         | 1F        | N/A                            |
| @         | 20        | Space                          |
| A         | 21        | !                              |
| B         | 22        | "                              |
| C         | 23        | 1.                             |
| D         | 24        | \$                             |
| E         | 25        | %                              |
| F         | 26        | &                              |
| G         | 27        | '                              |
| H         | 28        | (                              |
| I         | 29        | )                              |
| J         | 2A        | -                              |
| K         | 2B        | +                              |
| L         | 2C        | ,                              |
| M         | 2D        | -                              |
| N         | 2E        | .                              |
| O         | 2F        | /                              |
| P         | 30        | 0                              |
| Q         | 31        | 1                              |
| R         | 32        | 2                              |
| S         | 33        | 3                              |
| T         | 34        | 4                              |
| U         | 35        | 5                              |
| V         | 36        | 6                              |
| W         | 37        | 7                              |
| X         | 38        | 8                              |
| Y         | 39        | 9                              |
| Z         | 3A        | :                              |

Art Editing
-----------

### unLZ-gba offsets
Most of the images are LZ77 compressed, meaning you can edit them in unLZ.

#### Overworld
Overworld char sprites go from 363 to 372. Before that are a bunch of option menus and whatnot, more on that later.

To get a view of the sprites normally, hit minus all the way, then plus 3.

#### Talking
Talking animations begin at 393 with Sonic and go to 425, ending with E-102.

### Decompressed Art

Most of the art in Sonic Battle is decompressed, which is good. You can
find many instances of decompressed art while browsing through the game
with Tile Layer Pro. This art includes attacks, card text art, and more.

| Offset          | Art                                                    |
|-----------------|--------------------------------------------------------|
| 7AA00           | ASCII characters (for the dialogue scenes)             |
| 8DDD0-A1400     | Attack Text                                            |
| A1400-A1600     | Numbers for HUD                                        |
| 10FE00-113200   | Unknown (Dialogue sprites?)                            |
| ?????-1A5258    | Central City "SONIC"                                   |
| 31FE00          | "Go" in a battle's start.                              |
| 333200-333E00   | GAME SET                                               |
| 320000-322E00   | Health bars. Very repetetive tiles.                    |
| 322E00-323200   | Looks like numbers. Poorly mapped numbers, that is.    |
| 323200-3237D4   | Life icons                                             |
| 3237D4-323E00   | POW and DEF icons. No recognizable AIR icon.           |
| 323E00-324200   | GROUND, DEFEND, AERIAL                                 |
| 324200-324800   | PRESS START (as in demo)                               |
| 338000-363E00   | Text in the credits.                                   |
| 366A00-3D2800   | Unknown (A beach or a map).                            |
| 3D5000-3DEE0    | "Now you can play the new episode" info.               |
| 3E1000-3E1400   | "Ground Aerial Defend", before the start of battle.    |
| 3E3400          | "Ground Aerial Defend", before the start of battle(2). |
| 3ED000-3EF600   | "Comunication error" info.                             |
| 423800-426A00   | Title screen art.                                      |
| 47ABB8-A8C600   | Character battle sprites.                              |
| A8F000-BE6600   | Attack effects and other animated stuff in battle.     |
| BE8400-BF1200   | Chao (in battle?)                                      |

### Character Battle Sprites

**Note:** Tile Layer Pro does **not**
[display these tiles correctly](https://twitter.com/Phasesaber/status/678361932290592769),
each tile is off by 2 rows:

![TLP Error Example](https://pbs.twimg.com/media/CWoGlRvUwAAaD6K.png)

In this section there is a lot of notation using `_`'s and numbers,
here's how this works:

- `_` 0 or "blank" space in sprite
- `1-F` the number from the palette which is
  used to display the pixel from the sprite

| Character                    | Offset |
|------------------------------|--------|
| Ground Shadow                | 47ABB8 |
| Sonic                        | 47B078 |

Each section of each sprite is split up into 8x8 grids.
The number that is in each space is the color that shows up in game,
which is retrieved from the palette of the character. HxD can only
go down to 8 bytes per line, so keep this in mind when editing.

The numbers are stored two per byte in reverse order,
meaning `00 00 00 10` is one layer that shows up like
`_______1` in game. `00 00 00 00 10 00 00 00` will
show up as:

```
________
_1______
```

Sprite sections are ordered in a weird way. Character sprites are 6x6 sections.

```
0123 OP
4567 QR
89AB ST
CDEF UV
GHIJ WX
KLMN YZ
```

####Animations
Each animation has a certain number of frames
(listed in order of appearance in ROM).

| Animation         | Frames |
|-------------------|--------|
| Idle              | 6      |
| Jog               | 1      |
| Run               | 8      |
| Halt              | 4      |
| Dash              | 7      |
| Turn              | 3      |
| Change Direction  | 4      |
| Fall              | 4      |
| Jump              | 5      |
| Land              | 3      |
| Double Jump       | 7      |
| Attack 1          | 6      |
| Attack 2          | 6      |
| Attack 3          | 7      |

####Ground Shadow
The ground shadow (the shadow that displays underneath every
character's feet, not Shadow the Hedgehog) has 4 states to it:

- Jump 0 (Sqaures 1-7): When a character is on the ground,
  they will have this shadow.
- Jump 1 (Squares 9-15): When a character jumps, they will have this shadow.
- Jump 2 (Squares 18-22): For characters that can double jump,
  this shadow appears under them when they are at the peak
  of their jump.
- Jump 3 (Squares 26-30): A third shadow for characters that can go really high
  like Rouge.

## 3D Map Editing

3D Maps are stored as little 2D grids on the BG2 and BG3 maps (use VBA
to see how this works). They are made into fully-fledged 3D worlds on
runtime. The texture for the bottom of the platforms are stored as
sprites. Tiles are represented by a byte value and go along in rows.
When one row is done, the tiles go on to the next. There are 2 maps for
a battle map; the texture map and the ground map. The texture map contains
the textures above the ground (except in the Crater map), and the ground
map contains the textures and shadows at ground level.

### Texture Map Section

Here are the offsets for all of the texture maps:

| Offset          | Texture Map    |
|-----------------|----------------|
| 456FA5 - 457FA5 | Emerald Beach  |
| 44BC96 - 44CC96 | Tails' Lab     |
| 47AB45 - 47BB45 | Chao Ruins     |
| 42D405 - 42E405 | Battle Highway |
| 4449C5 - 4459C5 | Club Rouge     |
| 454BA0 - 455BA0 | Amy's Room     |
| 4353C5 - 4363C5 | Metal Depot    |

NOTE: I can't find the Library texture map offset.

Each map has a set of tiles, represented by values from 00 to FF
(obviously!), though not all of the slots are used. Here is a incomplete
texture value list:

As a global value, 00 = Blank tile. Anything above values used = Black
tile

#### Emerald Beach

Tiles use values 00 to 6C. Due to the huge texturization used in Emerald
Beach, this has been divided into parts for your convenience.

##### Grass Texture

| Value | Tile                         |
|-------|------------------------------|
| 01    | Grass Part 1                 |
| 02    | Grass Part 2                 |
| 03    | Grass Part 3 / 6 / 9         |
| 04    | Grass Part 4 / 7 / 10        |
| 05    | Grass Part 5                 |
| 06    | Grass Part 11                |
| 07    | Grass Part 12                |
| 08    | Grass Part 13                |
| 09    | Grass Part 14                |
| 0A    | Grass Part 15 / 18 / 21      |
| 0B    | Grass Part 16                |
| 0C    | Grass Part 17                |
| 0D    | Grass Part 19 / 22           |
| 0E    | Grass Part 20                |
| 0F    | Grass Part 23                |
| 10    | Grass Part 24                |
| 11    | Grass Part 25                |
| 12    | Grass Part 26                |
| 13    | Grass Part 27 / 30           |
| 14    | Grass Part 28 / 34           |
| 15    | Grass Part 29 / 32           |
| 16    | Grass Part 31                |
| 17    | Grass Part 35                |
| 18    | Grass Part 36                |
| 19    | Grass Part 37 / 40 / 43 / 46 |
| 1A    | Grass Part 38 / 41 / 44      |
| 1B    | Grass Part 39 / 42 / 45      |
| 1C    | Grass Part 47                |

##### Rock Texture

| Value | Tile             |
|-------|------------------|
| 1D    | Rocks Part 1     |
| 1E    | Rocks Part 2     |
| 1F    | Rocks Part 3 / 7 |
| 20    | Rocks Part 4 / 8 |
| 21    | Rocks Part 5     |
| 22    | Rocks Part 6     |
| 23    | Rocks Part 7     |
| 24    | Rocks Part 8     |
| 25    | Rocks Part 9     |
| 26    | Rocks Part 10    |
| 27    | Rocks Part 11    |
| 28    | Rocks Part 12    |
| 29    | Rocks Part 13    |
| 2A    | Rocks Part 14    |
| 2B    | Rocks Part 15    |
| 2C    | Rocks Part 16    |
| 2D    | Rocks Part 17    |
| 2E    | Rocks Part 18    |
| 2F    | Rocks Part 19    |
| 30    | Rocks Part 20    |
| 31    | Rocks Part 21    |
| 32    | Rocks Part 22    |
| 33    | Rocks Part 23    |
| 34    | Rocks Part 24    |
| 35    | Rocks Part 25    |
| 36    | Rocks Part 26    |
| 37    | Rocks Part 27    |
| 38    | Rocks Part 28    |
| 39    | Rocks Part 29    |
| 3A    | Rocks Part 30    |
| 3B    | Rocks Part 31    |
| 3C    | Rocks Part 32    |
| 3D    | Rocks Part 33    |
| 3E    | Rocks Part 34    |
| 3F    | Rocks Part 35    |
| 40    | Rocks Part 36    |
| 41    | Rocks Part 37    |
| 42    | Rocks Part 38    |
| 43    | Rocks Part 39    |
| 44    | Rocks Part 40    |
| 45    | Rocks Part 41    |
| 46    | Rocks Part 42    |
| 47    | Rocks Part 43    |
| 48    | Rocks Part 44    |
| 49    | Rocks Part 45    |
| 4A    | Rocks Part 46    |
| 4B    | Rocks Part 47    |
| 4C    | Rocks Part 48    |
| 4D    | Rocks Part 49    |
| 4E    | Rocks Part 50    |
| 4F    | Rocks Part 51    |
| 50    | Rocks Part 52    |
| 51    | Rocks Part 53    |
| 52    | Rocks Part 54    |
| 53    | Rocks Part 55    |
| 54    | Rocks Part 56    |
| 55    | Rocks Part 57    |
| 56    | Rocks Part 58    |
| 57    | Rocks Part 59    |
| 5A    | Rocks Part 60    |
| 5B    | Rocks Part 61    |
| 5C    | Rocks Part 62    |
| 5D    | Rocks Part 63    |
| 5E    | Rocks Part 64    |
| 5F    | Rocks Part 65    |
| 60    | Rocks Part 66    |
| 61    | Rocks Part 67    |
| 62    | Rocks Part 68    |
| 63    | Rocks Part 69    |
| 64    | Rocks Part 70    |
| 65    | Rocks Part 71    |
| 66    | Rocks Part 72    |
| 67    | Rocks Part 73    |
| 68    | Rocks Part 74    |
| 69    | Rocks Part 75    |
| 6A    | Rocks Part 76    |
| 6B    | Rocks Part 77    |
| 6C    | Rocks Part 78    |

#### Tails' Lab

Tiles use values from 01 to 10.

| Value | Tile             |
|-------|------------------|
| 01    | Cylinder Part 1  |
| 02    | Cylinder Part 2  |
| 03    | Cylinder Part 3  |
| 04    | Cylinder Part 4  |
| 05    | Cylinder Part 5  |
| 06    | Cylinder Part 6  |
| 07    | Cylinder Part 7  |
| 08    | Cylinder Part 8  |
| 09    | Cylinder Part 9  |
| 0A    | Cylinder Part 10 |
| 0B    | Cylinder Part 11 |
| 0C    | Cylinder Part 12 |
| 0D    | Cylinder Part 13 |
| 0E    | Cylinder Part 14 |
| 0F    | Cylinder Part 14 |
| 10    | Cylinder Part 15 |

#### Crater

Tiles use values 00 to 1C.

| Value | Tile                                    |
|-------|-----------------------------------------|
| 01    | Icepit Part 1 / 52                      |
| 02    | Icepit Part 2 / 53                      |
| 03    | Icepit Part 3 / 7 / 54 / 58 / 62        |
| 04    | Icepit Part 4 / 8 / 55 / 63             |
| 05    | Icepit Part 5 / 9 / 56 / 60             |
| 06    | Icepit Part 6 / 10 / 57 / 59 / 61       |
| 07    | Icepit Part 11 / 64                     |
| 08    | Icepit Part 12 / 65                     |
| 09    | Icepit Part 13 / 17 / 27 / 66 / 70 / 74 |
| 0A    | Icepit Part 14 / 18 / 28 / 67 / 71 / 75 |
| 0B    | Icepit Part 15 / 19 / 68 / 72           |
| 0C    | Icepit Part 16 / 18 / 20 / 69 / 73      |
| 0D    | Icepit Part 21 / 76 / 80 / 84           |
| 0E    | Icepit Part 22 / 77 / 81 / 85           |
| 0F    | Icepit Part 23 / 26 / 31 / 39           |
| 10    | Icepit Part 24 / 32 / 37 / 40           |
| 11    | Icepit Part 25 / 33 / 38                |
| 12    | Icepit Part 26 / 34 / 39                |
| 13    | Icepit Part 40 / 96 / 100 / 104         |
| 14    | Icepit Part 41 / 97 / 101 / 105         |
| 15    | Icepit Part 42 / 46 / 50                |
| 16    | Icepit Part 43 / 47 / 51                |
| 17    | Icepit Part 44 / 48                     |
| 18    | Icepit Part 45 / 49                     |
| 19    | Icepit Part 86 / 90 / 94                |
| 1A    | Icepit Part 87 / 91 / 95                |
| 1B    | Icepit Part 98 / 102 / 106              |
| 1C    | Icepit Part 99 / 103 / 107              |

#### Chao Ruins

Tiles use values 00 to 20.

| Value | Tile          |
|-------|---------------|
| 01    | Rocks Part 1  |
| 02    | Rocks Part 2  |
| 03    | Rocks Part 3  |
| 04    | Rocks Part 4  |
| 05    | Rocks Part 5  |
| 06    | Rocks Part 6  |
| 07    | Rocks Part 7  |
| 08    | Rocks Part 8  |
| 09    | Rocks Part 9  |
| 0A    | Rocks Part 10 |
| 0B    | Rocks Part 11 |
| 0C    | Rocks Part 12 |
| 0D    | Rocks Part 13 |
| 0E    | Rocks Part 14 |
| 0F    | Rocks Part 15 |
| 10    | Rocks Part 16 |
| 11    | Rocks Part 17 |
| 12    | Rocks Part 18 |
| 13    | Rocks Part 19 |
| 14    | Rocks Part 20 |
| 15    | Rocks Part 21 |
| 16    | Rocks Part 22 |
| 17    | Rocks Part 23 |
| 18    | Rocks Part 24 |
| 19    | Rocks Part 25 |
| 1A    | Rocks Part 26 |
| 1B    | Rocks Part 27 |
| 1C    | Rocks Part 28 |
| 1D    | Rocks Part 29 |
| 1E    | Rocks Part 30 |
| 1F    | Rocks Part 31 |
| 20    | Rocks Part 32 |

#### Battle Highway

Tiles use values 00 to 20.

| Value | Tile                   |
|-------|------------------------|
| 01    | Metallic Boxes Part 1  |
| 02    | Metallic Boxes Part 2  |
| 03    | Metallic Boxes Part 3  |
| 04    | Metallic Boxes Part 4  |
| 05    | Metallic Boxes Part 5  |
| 06    | Metallic Boxes Part 6  |
| 07    | Metallic Boxes Part 7  |
| 08    | Metallic Boxes Part 8  |
| 09    | Metallic Boxes Part 9  |
| 0A    | Metallic Boxes Part 10 |
| 0B    | Metallic Boxes Part 11 |
| 0C    | Metallic Boxes Part 12 |
| 0D    | Metallic Boxes Part 13 |
| 0E    | Metallic Boxes Part 14 |
| 0F    | Metallic Boxes Part 15 |
| 10    | Metallic Boxes Part 16 |
| 11    | Stripes Part 1         |
| 12    | Stripes Part 2         |
| 13    | Stripes Part 3         |
| 14    | Stripes Part 4         |
| 15    | Stripes Part 5         |
| 16    | Stripes Part 6         |
| 17    | Stripes Part 7         |
| 18    | Stripes Part 8         |
| 19    | Stripes Part 9         |
| 1A    | Stripes Part 10        |
| 1B    | Stripes Part 11        |
| 1C    | Stripes Part 12        |
| 1D    | Stripes Part 13        |
| 1E    | Stripes Part 14        |
| 1F    | Stripes Part 15        |
| 20    | Stripes Part 16        |

#### Amy's Room

Tiles use values 00 to 60.

| Value | Tile                      |
|-------|---------------------------|
| 01    | Amy Confused Face Part 1  |
| 02    | Amy Confused Face Part 2  |
| 03    | Amy Confused Face Part 3  |
| 04    | Amy Confused Face Part 4  |
| 0D    | Amy Confused Face Part 5  |
| 0E    | Amy Confused Face Part 6  |
| 0F    | Amy Confused Face Part 7  |
| 10    | Amy Confused Face Part 8  |
| 19    | Amy Confused Face Part 9  |
| 1A    | Amy Confused Face Part 10 |
| 1B    | Amy Confused Face Part 11 |
| 1C    | Amy Confused Face Part 12 |
| 05    | Amy Shocked Face Part 1   |
| 06    | Amy Shocked Face Part 2   |
| 07    | Amy Shocked Face Part 3   |
| 08    | Amy Shocked Face Part 4   |
| 11    | Amy Shocked Face Part 5   |
| 12    | Amy Shocked Face Part 6   |
| 13    | Amy Shocked Face Part 7   |
| 14    | Amy Shocked Face Part 8   |
| 1D    | Amy Shocked Face Part 9   |
| 1E    | Amy Shocked Face Part 10  |
| 1F    | Amy Shocked Face Part 11  |
| 20    | Amy Shocked Face Part 12  |
| 09    | Amy Angry Face Part 1     |
| 0A    | Amy Angry Face Part 2     |
| 0B    | Amy Angry Face Part 3     |
| 0C    | Amy Angry Face Part 4     |
| 15    | Amy Angry Face Part 5     |
| 16    | Amy Angry Face Part 6     |
| 17    | Amy Angry Face Part 7     |
| 18    | Amy Angry Face Part 8     |
| 21    | Amy Angry Face Part 9     |
| 22    | Amy Angry Face Part 10    |
| 23    | Amy Angry Face Part 11    |
| 24    | Amy Angry Face Part 12    |
| 25    | Amy Talking Face Part 1   |
| 26    | Amy Talking Face Part 2   |
| 27    | Amy Talking Face Part 3   |
| 28    | Amy Talking Face Part 4   |
| 2D    | Amy Talking Face Part 5   |
| 2E    | Amy Talking Face Part 6   |
| 2F    | Amy Talking Face Part 7   |
| 30    | Amy Talking Face Part 8   |
| 35    | Amy Talking Face Part 9   |
| 36    | Amy Talking Face Part 10  |
| 37    | Amy Talking Face Part 11  |
| 38    | Amy Talking Face Part 12  |
| 29    | Amy Posed Face Part 1     |
| 2A    | Amy Posed Face Part 2     |
| 2B    | Amy Posed Face Part 3     |
| 2C    | Amy Posed Face Part 4     |
| 31    | Amy Posed Face Part 5     |
| 32    | Amy Posed Face Part 6     |
| 33    | Amy Posed Face Part 7     |
| 34    | Amy Posed Face Part 8     |
| 39    | Amy Posed Face Part 9     |
| 3A    | Amy Posed Face Part 10    |
| 3B    | Amy Posed Face Part 11    |
| 3C    | Amy Posed Face Part 12    |
| 3D    | Amy ??? Face Part 1       |
| 3E    | Amy ??? Face Part 2       |
| 3F    | Amy ??? Face Part 3       |
| 40    | Amy ??? Face Part 4       |
| 49    | Amy ??? Face Part 5       |
| 4A    | Amy ??? Face Part 6       |
| 4B    | Amy ??? Face Part 7       |
| 4C    | Amy ??? Face Part 8       |
| 55    | Amy ??? Face Part 9       |
| 56    | Amy ??? Face Part 10      |
| 57    | Amy ??? Face Part 11      |
| 58    | Amy ??? Face Part 12      |
| 41    | Amy Goofy Face Part 1     |
| 42    | Amy Goofy Face Part 2     |
| 43    | Amy Goofy Face Part 3     |
| 44    | Amy Goofy Face Part 4     |
| 4D    | Amy Goofy Face Part 5     |
| 4E    | Amy Goofy Face Part 6     |
| 4F    | Amy Goofy Face Part 7     |
| 50    | Amy Goofy Face Part 8     |
| 59    | Amy Goofy Face Part 9     |
| 5A    | Amy Goofy Face Part 10    |
| 5B    | Amy Goofy Face Part 11    |
| 5C    | Amy Goofy Face Part 12    |
| 45    | Amy Sad Face Part 1       |
| 46    | Amy Sad Face Part 2       |
| 47    | Amy Sad Face Part 3       |
| 48    | Amy Sad Face Part 4       |
| 51    | Amy Sad Face Part 5       |
| 52    | Amy Sad Face Part 6       |
| 53    | Amy Sad Face Part 7       |
| 54    | Amy Sad Face Part 8       |
| 5D    | Amy Sad Face Part 9       |
| 5E    | Amy Sad Face Part 10      |
| 5F    | Amy Sad Face Part 11      |
| 60    | Amy Sad Face Part 12      |

#### Library

Tiles use values 00 to 10.

| Value | Tile             |
|-------|------------------|
| 01    | Bookcase Part 1  |
| 02    | Bookcase Part 2  |
| 03    | Bookcase Part 3  |
| 04    | Bookcase Part 4  |
| 05    | Bookcase Part 5  |
| 06    | Bookcase Part 6  |
| 07    | Bookcase Part 7  |
| 08    | Bookcase Part 8  |
| 09    | Bookcase Part 9  |
| 0A    | Bookcase Part 10 |
| 0B    | Bookcase Part 11 |
| 0C    | Bookcase Part 12 |
| 0D    | Bookcase Part 13 |
| 0E    | Bookcase Part 14 |
| 0F    | Bookcase Part 15 |
| 10    | Bookcase Part 16 |

#### Metal Depot

Tiles use values 00 to 30.

| Value | Tile                |
|-------|---------------------|
| 01    | Lined Metal Part 1  |
| 02    | Lined Metal Part 2  |
| 03    | Lined Metal Part 3  |
| 04    | Lined Metal Part 4  |
| 05    | Lined Metal Part 5  |
| 06    | Lined Metal Part 6  |
| 07    | Lined Metal Part 7  |
| 08    | Lined Metal Part 8  |
| 09    | Lined Metal Part 9  |
| 0A    | Lined Metal Part 10 |
| 0B    | Lined Metal Part 11 |
| 0C    | Lined Metal Part 12 |
| 0D    | Lined Metal Part 13 |
| 0E    | Lined Metal Part 14 |
| 0F    | Lined Metal Part 15 |
| 10    | Lined Metal Part 16 |
| 11    | ??? Metal Part 1    |
| 12    | ??? Metal Part 2    |
| 13    | ??? Metal Part 3    |
| 14    | ??? Metal Part 4    |
| 19    | ??? Metal Part 5    |
| 1A    | ??? Metal Part 6    |
| 1B    | ??? Metal Part 7    |
| 1C    | ??? Metal Part 8    |
| 21    | ??? Metal Part 9    |
| 22    | ??? Metal Part 10   |
| 23    | ??? Metal Part 11   |
| 24    | ??? Metal Part 12   |
| 29    | ??? Metal Part 13   |
| 2A    | ??? Metal Part 14   |
| 2B    | ??? Metal Part 15   |
| 2C    | ??? Metal Part 16   |
| 15    | Vent Metal Part 1   |
| 16    | Vent Metal Part 2   |
| 17    | Vent Metal Part 3   |
| 18    | Vent Metal Part 4   |
| 1D    | Vent Metal Part 5   |
| 1E    | Vent Metal Part 6   |
| 1F    | Vent Metal Part 7   |
| 20    | Vent Metal Part 8   |
| 25    | Vent Metal Part 9   |
| 26    | Vent Metal Part 10  |
| 27    | Vent Metal Part 11  |
| 28    | Vent Metal Part 12  |
| 2D    | Vent Metal Part 13  |
| 2E    | Vent Metal Part 14  |
| 2F    | Vent Metal Part 15  |
| 30    | Vent Metal Part 16  |

#### Club Rouge

Club Rouge is \*still\* incomplete, but it will be complete soon, I
promise.

| Value | Tile                                                                           |
|-------|--------------------------------------------------------------------------------|
| 01    | Rectacircular Platform Rotated &gt; Part 1                                     |
| 02    | Rectacircular Platform Rotated &gt; Part 2                                     |
| 03    | Rectacircular Platform Rotated &gt; Part 3 / 4 / 5 / 6 / 7 / 8 / 9 / 10        |
| 04    | Rectacircular Platform Rotated &gt; Part 11                                    |
| 05    | Rectacircular Platform Rotated &gt; Part 12                                    |
| 06    | Rectacircular Platform Rotated &gt; Part 13                                    |
| 07    | Rectacircular Platform Rotated &gt; Part 14                                    |
| 08    | Rectacircular Platform Rotated &gt; Part 15 / 16 / 17 / 18 / 19 / 20 / 21 / 22 |
| 09    | Rectacircular Platform Rotated &gt; Part 23                                    |
| 0A    | Rectacircular Platform Rotated &gt; Part 24                                    |
| 0B    | Rectacircular Platform Rotated \^ Part 1                                       |
| 0C    | Rectacircular Platform Rotated \^ Part 2                                       |
| 0D    | Rectacircular Platform Rotated \^ \#2 Part 1                                   |
| 0E    | Rectacircular Platform Rotated \^ \#2 Part 2                                   |

### Ground Map Section

Ground maps are the maps of things at ground level. Except for the
Crater area, the BG3 Map is always the ground map(In the Crater, the
maps are reversed; the texture map, BG3 is the stuff underground, and
the ground map, BG2, is the stuff aboveground.

Offsets:

-   Emerald Beach: `46DC70`
-   Tails' Lab: `44A664`
-   Cyberspace: `4721B4`

#### Cyberspace

NOTE: Cyberspace has constant cycling palette effects, so a fully
accurate list is impossible. This list is based on the semi-squares at
the corners being the Raster 0 (lightest) color. In other words, the
colors start as light colors, gradually go on to dark colors, and then
gradually come back to light at the ends. Raster 0 is the lightest
color, and Raster 5 is the darkest. (Of course, I shouldn't call it
Raster, because the color cycling isn't a rasterization effect, though I
originally thought it was.)

| Value | Raster Effect                                                              |
|-------|----------------------------------------------------------------------------|
| 01    | Upgoing line, Raster 0. Also acts as | for Raster 0 square                 |
| 02    | Upgoing line, Raster 1. Also acts as | for Raster 1 square.                |
| 03    | Upgoing line, Raster 2. Also acts as | for Raster 2 square.                |
| 04    | Upgoing line, Raster 3. Also acts as | for Raster 3 square.                |
| 05    | Upgoing line, Raster 3. Also acts as | for second Raster 3 square.         |
| 06    | Upgoing line, Raster 2. Also acts as | for second Raster 2 square.         |
| 07    | Upgoing line, Raster 1. Also acts as | for second Raster 1 square.         |
| 08    | Upgoing line, Raster 0. Also acts as | for second Raster 0 square.         |
| 09    | Rightgoing line, Raster 0. Acts as lines between Raster 0 square corners.  |
| 0A    | Upleft square corner, Raster 0.                                            |
| 0B    | Rightgoing line, Raster 1. Goes at end of Raster 0 square. Acts as corner. |
| 0C    | Upleft square corner, Raster 1.                                            |
| 0D    | Rightgoing line, Raster 1. Acts as lines between Raster 1 square corners.  |
| 0E    | Rightgoing line, Raster 2. Goes at end of Raster 1 square. Acts as corner. |
| 0F    | Upleft square corner, Raster 2.                                            |
| 10    | Rightgoing line, Raster 3. Acts as lines between Raster 2 square corners.  |
| 11    | Upright square corner, Raster 4.                                           |
| 12    | Upleft square corner, Raster 4                                             |
| 13    | Rightgoing line, Raster 4. Acts as lines between Raster 4 square corners.  |
| 14    | Upright square corner, Raster 2. Goes at end of Raster 4 square.           |
| 15    | Rightgoing line, Raster 2. Goes after the second Raster 2 upright corner.  |
| 16    | Rightgoing line, Raster 2. Goes at end of second Raster 2 square.          |

OAM Editing
-----------

The OAM is a listing of the tile numbers of the sprites that are
onscreen. It also lists the Y position of the sprite, the X position of
the sprite, the flags, and the palette number. The OAM, in a way, is a
sort of sprite map. This can be useful when editing battle maps; you'll
usually have to edit the sprite map to get your work done. It can also
be useful when editing sprite mappings; the OAM usually IS the mappings.
Not only that, it determines the screen position for each part, so you
can have a mapped part of a mapped sprite outside the sprite.

But sadly, the OAM changes every second, so lasting changes to the OAM
are, as far as I know, impossible.

### The OAM Format

```
aa bb cc dd ee f? gg hh (next entry in OAM)
```

- aa = Y Position on Screen
- bb = Flags
- cc = X Position on Screen
- dd = Flags
- ee = Tile Number
- f  = Palette No. (Backwards)
- ?  = I have no idea what this is, but on some sprites it can do strange things when changed.
- gg = I don't know what this is, but it does nothing.
- hh = This byte is always either 00 or 01. But it doesn't do anything.

The default values in the OAM are:

-   Position: 0,0
-   Mode: 0
-   Colors: 16
-   Palette Number: 0
-   Tile Number: 0
-   Priority: 0
-   Size: 8x8
-   Rotation: 0
-   Flags: R

In other words, an empty slot will look something like:

```
    00 02 00 00 00 00 00 00
OR
    00 02 00 00 00 00 00 01
OR
    00 00 00 00 00 00 00 00
OR
    00 00 00 00 00 00 00 01
```

The unused slots all use these values, making it easy to spot them.

You can also add to the OAM by changing the unused slots as you wish.
Unused slots usually all have the same bytes, and therefore unused slots
in the OAM are marked by a handful of redundant (except for the gg and
hh bytes) space.

The end of the OAM is easily recognizable; the redundant space suddenly
changing to all-unredundant space.

In VBA, the XY positions, the tile number, and the palette number are
shown as decimal values, so it is helpful to have a calculator that can
convert from Decimal to Hex and back. The Windows Calculator supports
all of that and more.

Here is the value list for the two flags:

- 00 / 10 / 20 = Nothing happens
- 01 - 10 = Tile with same tile as following
- 11 - 20 = Tile with same tile as following
- 21 = REALLY glitchy 32x32 image
- 22 / 23 = REALLY glitchy 32x32 image R
- 24 / 25 = REALLY glitchy 32x32 image D (???)
- 26 / 27 / 2F = REALLY glitchy 32x32 image R and D (???)
- 30 / 40 = Same tile as following
- 31 / 38 / 39 = REALLY glitchy 32x32 image mirrored
- 32 / 33 / 3A / 3B = REALLY glitchy 32x32 image M and R
- 34 / 35 / 3C / 3D = REALLY glitchy 32x32 image MD(???)
- 36 / 37 / 3E / 3F = REALLY glitchy 32x32 image R and MD(???)
- 41 / 48 / 49 = Nothing happens
- 42 / 43 / 4A / 4B = R
- 44 / 45 / 4C / 4D / 4E = D(???)
- 46 / 47 / 4F = R and D(???)
- 50 = M
- 51 = M
- 52 / 53 / 5A / 5B = R and M
- 54 / 55 / 5C / 5D = MD(???)
- 56 / 57 / 5E / 5F = R and MD
- 58 / 59 = M
- 60 / 68 / 69 = Random position, glitchy tile
- 61 = Glitchy tile
- 62 / 63 / 6A / 6B = Glitchy tile R
- 64 / 65 / 6C / 6D = Glitchy tile D
- 66 / 67 / 6E / 6F = Glitchy tile R and D
- 70 = Random position, glitchy tile M
- 71 / 78 / 79 = Glitchy tile M
- 72 / 73 / 7A / 7B = Glitchy tile R and M
- 74 / 75 / 7C / 7D = Glitchy tile MD
- 76 / 77 / 7E / 7F = Glitchy tile R and MD
- 80 = Double-tile
- 81 - 8F = Glitchy tile R and MD, Y = 4 and on 81, X = 129 and goes up from there
- 90 = Double-tile M
- 91 - 9F = Glitchy tile R and MD. Y = 4 and on 91, X = 145 and goes up from there
- A0 = Double-Y glitchy tile
- A1 - AF = Glitchy tile R and MD; Y = 4 and on A1, X = 161 and goes up from there
- B0 / B1 / B8 / B9 = Double-Y glitchy tile M
- B2 / B3 / BA / BB = Double-Y glitchy tile R and M
- B4 / B5 / BC / BD = Double-Y glitchy tile MD
- B6 / B7 / BE / BF = Double-Y glitchy tile R and MD
- C0 / D0 / E0 / F0 = Same tile as following one
- C1 - CF = Same tile as following one
- D1 - DF = Same tile as following one
- E1 - EF = Same tile as following one
- F1 - FF = Same tile as following one

That's a lot of values! The glitchy tiles, by the way, can be
unglitched into good 256-color tiles by changing the tile number into
the 256-color tile you want it to show.

Here is an example OAM slot: `0F 50 01 10 04 1C F2 01`

This will produce the tile number 04, with the XY location 1,10, the
flag R, and the palette in slot 1. The offset for the OAM in memory is
0x7000000. There are no offsets in the ROM.

Palette Editing: Beauty and the Binary
--------------------------------------

There are two ways to palette edit. One is to use Icy
Guy's excellent program, GBA Color
Picker; the other option is to convert RGB
values into binary. Which one would you rather do?

Each palette line can hold 16 colors, but the first seems to be reserved
for transparency reasons, so you've got 15 colors available in each
palette. Counting from the 2nd second color to left (this one is often
white), the colors are numbered according to standard hex numbering,
sans 0, so that's 1-F. Palettes are byteswapped, meaning that 2700 shows
up as 0027 in a hex editor. The first color in each palette controls
transparency, for your information.

Here are the offsets for each palette.

| Offset | Description                                                |
|--------|------------------------------------------------------------|
| 1A7F90 | Text/speech box palette                                    |
| 1A8E25 | Emerald Beach                                              |
| 41A218 | Title screen "background" palette                          |
| 468A18 | Emerald Beach shadow (on grass) palette                    |
| 468A38 | Emerald Beach shadow (on sand) palette                     |
| 47AB78 | Gray/"imposter" Emerl's in-fight palette                   |
| 47AFB8 | Sonic's in-fight palette                                   |
| 4CADD8 | Knuckles' in-fight palette                                 |
| 5283F8 | Tails' in-fight palette                                    |
| 58D818 | Shadow's in-fight palette                                  |
| 5F3E38 | Rouge's in-fight palette                                   |
| 636458 | Amy's in-fight palette                                     |
| 681A78 | E-102's in-fight palette                                   |
| 6F6A98 | Cream's in-fight palette                                   |
| 7336B8 | Chaos' in-fight palette                                    |
| 7822D8 | Robotnik's in-fight palette                                |
| 787CF8 | Emerl's in-fight palette                                   |
| BF2058 | Dust cloud (from end of combo) palette                     |
| BF2078 | In-fight shield palette ("opening" animation, when purple) |
| BF2098 | In-fight shield palette/Tails' gun's power ball            |
| BF20D8 | Sonic's mine palette                                       |

### VBA Hacking

Visual Boy Advance is THE ultimate tool
for ROM hacking, when combined with Hex
Workshop. This is especially true for Sonic
Battle. These are steps that will let you do some radical stuff with VBA
too for any game compatible with it:

1.  Open up the respective viewer and get the offset of the spriteData
    you want.
2.  Open up the Memory Viewer and go to that offset.
3.  Take notes on the format and experiment with it.
4.  Now that you've gotten the format into a file, do a search for the
    original hex values using Hex Workshop. Record the offset.
5.  Change as you please.
6.  If you want to distribute your results, make an IPS patch with StealthPatch.

Strange Things
--------------

There are many strange objects in Sonic Battle as can be seen by looking
at the OAM. (What the heck is that messed-up Emerl doing there? Or the 1
1/2 Eggman?) This gives the location, the position, the color mode, the
palette, the tile number, the priority, the size, and the description of
each strange object I've come across so far.

NOTE: This would probably go in a glitches guide, but these slots can
also serve as extra object places, I think.

### Messed-Up Fire Thingy

-   Where: Emerald Town Map Cutscene 1 (Sonic Finds Emerl), OAM Slot 6
-   Position: 97, 160
-   Color Mode: 16-Colors
-   Palette: 2
-   Tile: 236
-   Priority: 3
-   Size: 32x32

Description: Sonic is covering up this object, but it's there.

### White Sonic with ACH

-   Where: Emerald Town Map, OAM Slot 3
-   Position: 155, 160
-   Color Mode: 16-Colors
-   Palette: 0
-   Tile: 424
-   Priority: 2
-   Size: 32x32

Description: This White Sonic moves as Sonic moves. Now, you might be
saying "It's the ACH in Emerald Beach with a wrong-palette Sonic! DUH!"
It does become the slot for the ACH in Emerald Beach, but it might be
relevant to note that the ACH is in a different location, and that we
don't ever see this object.

### Event Place with Arrow

-   Where: Emerald Town Map

(NOTE:There are two of these, so I have seperated them by slots.)

-   OAM Slot 6
-   Position: 98, 160
-   Color Mode: 16-Colors
-   Palette: 2
-   Tile: 116
-   Priority: 3
-   Size: 32x32
-   Description: N/A

<br>

-   OAM Slot 10
-   Position|107, 160
-   Color Mode: 16-Colors
-   Palette: 2
-   Tile: 116
-   Priority: 3
-   Size: 32x32
-   Description: N/A

\*.SGM Savestate Hacking
------------------------

### Palette Editing

Like the ROM, palettes are stored in an RCGB value. R is Dark Red, C is
Contrast, G is Green, and B is blue.

.sgm files are compressed. They could be decompressed with WinZip. 
First, add the "\*.gz" extension to your file. Then open up WinZip,
decompress, and voila. You don't even have to recompress.

The offset for the palettes in the \*.sgm savestate is 0x05000000. Edit
heartily.

RAM Breakdown
-------------

If you want to make savestate hacks the easy way, you need to know the
memory and what goes where, and where where you want to put something
is. Wow, confusing sentence, huh? If you don't know this, the chances of
you making a savestate hack that accomplishes something OTHER than
crashing the game is not that high, unless you use VBA (See "Hacking
with VBA" section for info about that.). VBA is the ultimate tool for
RAM editing, too. Get it or be doomed to not knowing what you're doing
in the savestate.

### Palettes in the RAM

The palette is located at 0x05000000, and it, like the game itself, uses
an RCGB value.

### Map Editing in the RAM

Map editing is a tile-by-tile grid, like the game. Here are RAM offsets
for BG0, BG1, BG2, and BG3 maps:

In Battle Mode:

| Value   | Map                  |
|---------|----------------------|
| 600E000 | BG2 Map(Texture map) |
| 600F000 | BG3 Map(Ground map)  |

In Menu Mode:

| Value   | Map                                        |
|---------|--------------------------------------------|
| 600F800 | BG0 Map(Mode)                              |
| 600F000 | BG1 Map(Fiery Thing)                       |
| 600E800 | BG2 Map(Menu BG with "SONIC BATTLE" on t)  |
| 600E000 | BG3 Map(Rolling background behind menu BG) |

In Episode Select Mode:

| Value   | Map                                                  |
|---------|------------------------------------------------------|
| 600F000 | BG0 Map(Drawings map)                                |
| 600E800 | BG1 Map(2 Semitransparent lines, below-screen lines) |
| 600E000 | BG2 Map(Two things, one at bottom and one at top)    |
| 600D800 | BG3 Map(Rolling background behind everything)        |

In Training Menu Mode:

| Value   | Map                                           |
|---------|-----------------------------------------------|
| 600F800 | BG0 Map(Semitransparent curviness at top)     |
| 600F000 | BG1 Map(Black thing that menu is on)          |
| 600E800 | BG2 Map(Menu)                                 |
| 600E000 | BG3 Map(Rolling background behind everything) |

In SEGA/SONICTEAM Mode:

| Value   | Map                      |
|---------|--------------------------|
| 6007000 | BG2 Map (Blurry Map)     |
| 600F000 | BG3 Map (Non-Blurry Map) |

In Intro Mode:

| Value   | Map                             |
|---------|---------------------------------|
| 600F800 | BG0 Map(BG with the background) |
| 6000000 | BG1 Map(Glitchy-glitchy map)    |
| 600B800 | BG2 Map(Drawings map)           |

In Title Screen Mode:

| Value   | Map                              |
|---------|----------------------------------|
| 6007800 | BG0 Map(Top Background Cover)    |
| 600B800 | BG1 Map(Bottom Background Cover) |
| 600F800 | BG2 Map(Rolling Background)      |
| 600F000 | BG3 Map(SONIC BATTLE)            |

In Cutscene Mode:

| Value   | Map                                                            |
|---------|----------------------------------------------------------------|
| 6009800 | BG0 Map(Text Box with Text)                                    |
| 600A000 | BG1 Map(Darkened version of things you can walk behind on map) |
| 600C000 | BG2 Map(Darkened version of things that are solid on map)      |
| 600E000 | BG3 Map(Darkened version of the basic map)                     |

In World Map Mode:

| Value   | Map                                        |
|---------|--------------------------------------------|
| 6009800 | BG0 Map(A text box? What the...)           |
| 600A000 | BG1 Map(Things you can walk behind on map) |
| 600C000 | BG2 Map(Things that are solid on map)      |
| 600E000 | BG3 Map(The basic map)                     |

### The OAM in the RAM
The offset in memory is 0x070000000.
