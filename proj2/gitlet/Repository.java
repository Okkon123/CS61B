package gitlet;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Leonard
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* TODO: fill in the rest of this class. */

    /**
     * Description:
     * Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that contains no files
     * and has the commit message initial commit (just like that, with no punctuation).
     * It will have a single branch: master, which initially points to this initial commit,
     * and master will be the current branch. The timestamp for this initial commit will be
     * 00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose for dates (this
     * is called “The (Unix) Epoch”, represented internally by the time 0.) Since the initial
     * commit in all repositories created by Gitlet will have exactly the same content, it
     * follows that all repositories will automatically share this commit (they will all have
     * the same UID) and all commits in all repositories will trace back to it.
     *
     * Failure cases: If there is already a Gitlet version-control system in the current directory,
     * it should abort. It should NOT overwrite the existing system with a new one. Should print the
     * error message A Gitlet version-control system already exists in the current directory.
     */
    public static void init() throws IOException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        } else {
            GITLET_DIR.mkdir();
            Staging_Area.STAGING_AREA_DIR.mkdir();
            Commit.COMMITS_DIR.mkdir();
            Branch.BRANCH_DIR.mkdir();
            Branch.BRANCHES_DIR.mkdir();
            Branch.HEAD.createNewFile();
            Blobs.BLOBS_DIR.mkdir();
            Staging_Area stageState = new Staging_Area();
            stageState.saveStageState();
            Commit first = new Commit();                         //initial commit
            String newCommitSha = first.saveCommit();            //save commit file
            Branch.creatBranch("master", newCommitSha);    //creat master branch
            Branch.updateHead("master");             //更新HEAD
        }
    }

    /**
     * 查询在CWD目录下是否存在名为fileName的文件，若存在则返回此文件，否则返回null
     * @param fileName
     * @return
     */
    public static File weatherFileExit (String fileName) {
        File[] files = CWD.listFiles();
        for (File file : files) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }
    /**
     * Description:
     * Displays what branches currently exist, and marks
     * the current branch with a *. Also displays what files have been
     * staged for addition or removal. An example of the exact format
     * it should follow is as follows.There is an empty line between
     * sections, and the entire status ends in an empty line as well.
     * Entries should be listed in lexicographic order, using the Java
     * string-comparison order (the asterisk doesn’t count). A file in
     * the working directory is “modified but not staged” if it is
     */
    public static void status() {
        printBranchStatus();
        printStagedFileStatus();
        printRemovedFileStatus();
        printMnsfcStatus();
        printUntrackedFilesStatus();
    }

    /**
     * 打印Branches
     */
    public static void printBranchStatus() {
        List<String> branchList = plainFilenamesIn(Branch.BRANCHES_DIR);
        String headCommitName = Branch.headBranchName();
        System.out.println("=== Branches ===");
        for (String branchName : branchList) {
            if (branchName.equals(headCommitName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }
        System.out.println();
    }
    /**
     * 打印StagedStatus
     */
    public static void printStagedFileStatus() {
        Staging_Area stageState = Staging_Area.readStageState();
        TreeMap<String, String> addition = stageState.getAddition();
        System.out.println("=== Staged Files ===");
        printStageState(addition);
    }
    /**
     * 打印Staged Files
     */
    public static void printRemovedFileStatus() {
        Staging_Area stageState = Staging_Area.readStageState();
        TreeMap<String, String> removal = stageState.getRemoval();
        System.out.println("=== Removed Files ===");
        printStageState(removal);
    }
    /**
     * 打印Modifications Not Staged For Commit
     */
    private static void printMnsfcStatus() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
    }
    /**
     * 打印Untracked Files
     */
    private static void printUntrackedFilesStatus() {
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }
    /**
     * 打印Removed Files
     * @param removal
     */
    private static void printStageState(TreeMap<String, String> removal) {
        List<String> removedFiles = new ArrayList<>();
        for (String fileName : removal.keySet()) {
            removedFiles.add(fileName);
        }
        Collections.sort(removedFiles);
        for (String fileName : removedFiles) {
            System.out.println(fileName);
        }
        System.out.println();
    }

//    /**
//     * 返回未被跟踪的文件名列表
//     * @return
//     */
//    public static List<String> fileNotTracked() {
//        List<String> result = new ArrayList<>();
//        List<String> fileNames = plainFilenamesIn(CWD);
//        Commit headCommit = Commit.readHeadCommit();
//        Staging_Area stageState = Staging_Area.readStageState();
//        for (String fileName : fileNames) {
//            if (!headCommit.fileExitInCommit(fileName) && !stageState.fileExitInAddition(fileName)) {
//                result.add(fileName);
//            }
//        }
//        return result;
//    }

    /**
     * 返回未被跟踪的文件名列表
     * @return
     */
    public static List<String> fileNotTracked() {
        Map<String, String> fileMap = fileNotTrackedMap();
        List<String> result = new ArrayList<>();
        for (String fileName : fileMap.keySet()) {
            result.add(fileName);
        }
        return result;
    }

    public static Map<String, String> fileNotTrackedMap() {
        Map<String, String> fileNameShaMap = plainFileMapIn(CWD);
        Map<String, String> result = new TreeMap<>();
        Commit headCommit = Commit.readHeadCommit();
        Staging_Area stageState = Staging_Area.readStageState();
        for (Map.Entry<String, String> entry : fileNameShaMap.entrySet()) {
            String fileName = entry.getKey();
            String sha = entry.getValue();
            if (!headCommit.fileExitInCommit(fileName) && !stageState.fileExitInAddition(fileName)) {
                result.put(fileName, sha);
            }
        }
        return result;
    }

    public static void checkout(String[] args) throws IOException {
        if (args.length == 3)   {                                                   // checkout -- [file name]
            if (!args[1].equals("--")) {                                            // 命令格式错误
                System.out.println("Command format wrong.");
                System.exit(0);
            }
            String fileName = args[2];
            Commit headCommit = Commit.readHeadCommit();                            // 获取headCommit
            checkoutHelper(headCommit, fileName);                                   // 辅助函数，作用见辅助函数注释
        } else if (args.length == 4) {                                              // checkout [commit id] -- [file name]
            String commitSha = args[1];
            Commit commit = Commit.getCommitBySha(commitSha);                       // 获取给定的commit
            if (commit == null) {
                System.out.println("No commit with that id exits.");
                System.exit(0);
            }
            if (!args[2].equals("--")) {                                            // 命令格式错误
                System.out.println("Command format wrong.");
                System.exit(0);
            }
            String fileName = args[3];
            checkoutHelper(commit, fileName);                                       // 辅助函数，作用见辅助函数注释
        } else if (args.length == 2) {                                              // checkout [branch name]
            String branchName = args[1];
            Commit commit = Branch.getBranchCommit(branchName);
            Staging_Area stageState = Staging_Area.readStageState();
            validateCheckoutForSwitchBranch(branchName);
            for (File file : CWD.listFiles()) {
                restrictedDelete(file.getName());
            }
            for (String fileName : commit.fileNameInBlob()) {
                File blob = commit.blobInCommit(fileName);
                File file = join(CWD, fileName);
                byte[] blobByte = readContents(blob);
                writeContents(file, blobByte);
                file.createNewFile();
            }
            Branch.updateHead(branchName);
            stageState.clearStage();
            stageState.saveStageState();
        } else {
            System.out.println("Incorrect operands");                               // 参数错误
            System.exit(0);
        }
    }

    /**
     * 辅助函数，传入commit和fileName，若commit不存在
     * 此file的映射则报错，若存在则将原文件删除，替换为commit映射的版本
     * @param commit
     * @param fileName
     * @throws IOException
     */
    private static void checkoutHelper(Commit commit, String fileName) throws IOException {
        File targetFile = commit.blobInCommit(fileName);
        if (targetFile == null) {
            System.out.println("File does not exit in that commit.");
            System.exit(0);
        }
        restrictedDelete(fileName);
        File newFile = join(CWD, fileName);
        byte[] fileByte = readContents(targetFile);
        writeContents(newFile, fileByte);
        newFile.createNewFile();
    }

    /**
     * 如果有文件未被跟踪且将会被切换到的分支重写则报错并退出
     * @param branchName
     */
    private static void validateCheckoutForSwitchBranch(String branchName) {
        Commit commit = Branch.getBranchCommit(branchName);
        if (commit == null) {
            System.out.println("No such branch exits.");
            System.exit(0);
        }
        String headBranch = Branch.headBranch();
        if (headBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        Map<String, String> notTrackedFileMap = fileNotTrackedMap();
        for (Map.Entry<String, String> entry : notTrackedFileMap.entrySet()) {
            String fileName = entry.getKey();
            String sha = entry.getValue();
            if (!commit.sameFileExitInCommit(fileName, sha)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }
    public static void removeFile() {

    }

}
