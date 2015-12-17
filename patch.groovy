/*
 * Script to patch binary files
 */

String patch = new File(args[0]).text
RandomAccessFile raf = new RandomAccessFile(args[1], "rw")

class Patch {
    public int offset
    public def patches = []
    Patch(String offset, String code) {
        println offset + " : " + code
        this.offset = Integer.parseInt(offset, 16)
        def bytes = getParts(code, 2)
        for(String b : bytes) this.patches << Integer.parseInt(b, 16).intValue()
    }

    private static String[] getParts(String string, int partitionSize) {
        def parts = []
        int len = string.length()
        for (int i=0; i<len; i+=partitionSize)
            parts << string.substring(i, Math.min(len, i + partitionSize))
        return parts;
    }
}
def patches = []

String currentOffset = ""
String offsetBuffer = ""
String codeBuffer = ""
boolean offsetFlag = false
boolean commentFlag = false
char last
int i = 0

for(char c : patch.toCharArray()) {
    i++
    if(commentFlag){
        if(c == ';' || c == '\n'){
            commentFlag = false
        }
        continue;
    }
    else if(c == ';') {
        commentFlag = true
    }
    else if(offsetFlag) {
        if(c == '\n') {
            currentOffset = offsetBuffer
            offsetBuffer = ""
            offsetFlag = false
        } else {
            offsetBuffer += c
        }
    }
    else if(c == '$') {
        offsetBuffer = ""
        offsetFlag = true
        if(currentOffset != "") {
            patches << new Patch(currentOffset.replaceAll(/\s/,""), codeBuffer.replaceAll(/\s/,""))
            currentOffset = ""
            offsetBuffer = ""
            codeBuffer = ""
        }
    }
    else if (i == patch.length()) {
        codeBuffer += c
        patches << new Patch(currentOffset.replaceAll(/\s/,""), codeBuffer.replaceAll(/\s/,""))
        break;
    }
    else {
        codeBuffer += c
        //println codeBuffer.replaceAll(/\s/,"")
    }
    last = c
}
for(Patch p : patches) {
    println "Applying Patch " + p.offset + ": " + p.patches
    raf.seek(p.offset)
    raf.write(p.patches as byte[], 0, p.patches.size())
}