package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Utils.*;

public class Blobs implements Serializable {
    public static final File BLOBS_DIR = join(Repository.GITLET_DIR, "blobs");

    /**
     * 将文件保存为Blob,文件名为file的sah, 内容为file的byte[]
     * @param file
     * @param sha
     * @throws IOException
     */
    public static void creatNewBlob(File file, String sha) throws IOException {
        File newBlob = join(BLOBS_DIR, sha);
        byte[] fileByte = readContents(file);
        writeContents(newBlob, fileByte);
        newBlob.createNewFile();
    }
}
