from PIL import Image
import sys

if len(sys.argv) < 4:
    print("This script changes all the colors of a file from palette 'A' to palette 'B'")
    print(" python3 palette_swap.py <old_palette> <new_palette> <image> [new_image]")
    sys.exit(1)

old = Image.open(sys.argv[1]).load()
new = Image.open(sys.argv[2]).load()
img = Image.open(sys.argv[3])
imgp = img.load()

changes = 0
for y in range(img.size[1]):
    for x in range(img.size[0]):
        for p in range(16):
            a = old[p,0]
            b = new[p,0]
            if imgp[x,y] == a:
                changes += 1
                imgp[x,y] = b
print("Image processing finised, " + str(changes) + " changes made.")
img.save(sys.argv[4 if len(sys.argv) == 5 else 3])