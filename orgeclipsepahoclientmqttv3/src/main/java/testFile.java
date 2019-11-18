import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class testFile {
    public static byte[] getContent(String filepath) throws IOException {
        File file=new File(filepath);
        long filesize=file.length();
        if (filesize>Integer.MAX_VALUE){
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi=new FileInputStream(file);
        byte[] buffer=new byte[(int)filesize];
        int offset=0;
        int numRead=0;
//        while(offset<filesize&&(numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0)
//        {
//            offset+=numRead;
//        }
//        if(offset!=buffer.length){
//            throw new IOException("Could not completely read file "
//                    + file.getName());
//        }
        while(fi.read(buffer)!=-1);
        String b=new String(buffer);
        System.out.println(b);
        fi.close();
        return buffer;
    }

    public static void main(String[] args){
        try {
            byte[] bb=getContent("/Users/tiechengshen/Downloads/testfile1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
