/*
 * Script to patch binary files
 */
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

String action = args[0]

if(action == "apply") {
    if(args.length != 3) {
        println "patch.jar.exe apply <new> <old>"
        System.exit(0)
    }
    String patch = new File(args[1]).text
    RandomAccessFile raf = new RandomAccessFile(args[2], "rw")
    def patches = []

    String currentOffset = ""
    String offsetBuffer = ""
    String codeBuffer = ""
    boolean offsetFlag = false
    boolean commentFlag = false
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
        else codeBuffer += c
    }
    for(Patch p : patches) {
        println "Applying Patch " + p.offset + ": " + p.patches
        raf.seek(p.offset)
        raf.write(p.patches as byte[], 0, p.patches.size())
    }
}
else if(action == "create") {
    if(args.length != 3) {
        println "patch.jar.exe create <new> <old>"
        System.exit(0)
    }
    RandomAccessFile n = new RandomAccessFile(args[1], "rw")
    RandomAccessFile o = new RandomAccessFile(args[2], "rw")
    int maxl = Math.max(n.length(), o.length())
    int minl = Math.max(n.length(), o.length())
}
else {
    println "patch.jar.exe: Action not found"
}