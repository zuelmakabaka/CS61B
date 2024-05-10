package gitlet;


import java.io.File;
import java.sql.Blob;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static Commit Head = null;
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File Adding = join(GITLET_DIR, "adding");
    public static final File Removal = join(GITLET_DIR, "removal");
    public static final File Commits = join(GITLET_DIR, "commit");
    public static final File Branches = join(GITLET_DIR, "branches");
    public static final File Blobs = join(GITLET_DIR, "blobs");
    //创建hashmap<sha1，文件名>
    public static final File Headfile = join(GITLET_DIR, "Head");
    public static final File nowbranch = join(GITLET_DIR, "nowbranch");
    public static TreeMap<String, File> CommitFilemap = new TreeMap<>();
    public static File CommitFilemapFile = join(GITLET_DIR,"CommitFilemapFile");
    public static HashMap<String, Blob> Blobmap = new HashMap<>();

    /* TODO: fill in the rest of this class. */
    public static void initrepo() {
        GITLET_DIR.mkdir();
        //创建initialCommit
        Commit initialCommit = new Commit();
        //将initialcommit序列化后取sha1
        String id = sha1(serialize(initialCommit));
        File initialCommitFile = join(Commits, id); //
        Utils.writeObject(initialCommitFile, initialCommit);
        writeObject(Headfile,initialCommit); //写回Head
        CommitFilemap.put(id,initialCommitFile);
        writeObject(CommitFilemapFile,CommitFilemap);
        Commits.mkdir();
        Adding.mkdir();
        Removal.mkdir();
        Branches.mkdir();
        Blobs.mkdir();
        branch("master");
        writeContents(nowbranch,"master");
    }

    public static void addfile(String file_to_add) {
        List<String> cwd_contents = new ArrayList<>(plainFilenamesIn(CWD));
        if(!cwd_contents.contains(file_to_add)){
            System.out.println("File does not exist.");
            System.exit(0);
        }
        File file_cwd = join(CWD, file_to_add); //定位到要添加的文件
        String contents = readContentsAsString(file_cwd);
        String fileid = sha1(serialize(contents)); //获得该文件id
        //先读取Head
        Commit nowHead = readObject(Headfile, Commit.class);
        if(nowHead != null) {
            //Head里的文件和add的一样，不添加
            if (nowHead.Commitcontents.containsKey(file_to_add)) {
                if (fileid.equals(nowHead.Commitcontents.get(file_to_add))) {
                    return;
                } else {
                    File added_file = join(Adding, file_to_add); //将文件加入staging路径
                    writeContents(added_file, contents); //将原文件内容复制到staging中
                }
            } else {
                File added_file = join(Adding, file_to_add); //将文件加入adding路径
                writeContents(added_file, contents); //将原文件内容复制到adding中
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void createcommit(String message) {
        Commit nowHead = readObject(Headfile, Commit.class);
        if (nowHead != null) {
            String parentID1 = sha1(serialize(nowHead)); //获取父commit的id
            Commit addcommit = new Commit(message, parentID1,null);//创建新commit
            addcommit.Commitcontents = nowHead.Commitcontents;//将父commit内容文件指针复制到addcommit
            List<String> files_to_commit = new ArrayList<>(plainFilenamesIn(Adding));
            //将Staging里的新file加入addcommit中
            while (!files_to_commit.isEmpty()) {
                String one_in_files = files_to_commit.removeFirst();//获得staging中要添加的一个文件名
                File to_commit = join(Adding, one_in_files);
                String contents = readContentsAsString(to_commit);
                String file_id = sha1(serialize(contents));
                File file_in_blobs = join(Blobs,file_id);
                writeContents(file_in_blobs,contents);
                if (!addcommit.Commitcontents.containsKey(one_in_files)) {
                    addcommit.Commitcontents.put(one_in_files, file_id);
                } else {
                    addcommit.Commitcontents.replace(one_in_files, file_id);//替换同一名字的不同文件内
                }
                to_commit.delete();
            }
            List<String> files_to_remove = new ArrayList<>(plainFilenamesIn(Removal));
            while (!files_to_remove.isEmpty()) {
                String one_in_remove = files_to_remove.removeFirst();
                File to_remove = join(Removal, one_in_remove);
                addcommit.Commitcontents.remove(one_in_remove);
                to_remove.delete();
            }
            String this_id = sha1(serialize(addcommit));
            File parentfile = join(Commits,addcommit.getParentID1());
            Commit parent = readObject(parentfile, Commit.class);
            parent.children[parent.index] = this_id;
            parent.index += 1;
            writeObject(parentfile,parent);
            File newcommitfile = join(Commits, sha1(serialize(addcommit)));//新commit的dir
            writeObject(newcommitfile, addcommit);//将添加的commit写回文件
            //更新Head指针
            writeObject(Headfile,addcommit);
            String branch_name = readContentsAsString(nowbranch);
            File branchfile = join(Branches,branch_name);
            writeObject(branchfile,addcommit);
            CommitFilemap = readObject(CommitFilemapFile, TreeMap.class);
            CommitFilemap.put(this_id, newcommitfile);//在treemap中加入新的commit
            writeObject(CommitFilemapFile,CommitFilemap);
        }else {
            System.exit(0);
        }
    }
    public static void rm_file(String filename) {
        List<String> add_contents = new ArrayList<>(plainFilenamesIn(Adding));
        Commit nowHead = readObject(Headfile, Commit.class);
        if (add_contents.contains(filename)) {
            File file_to_remove = join(Adding, filename);
            file_to_remove.delete();
        } else if (nowHead.Commitcontents.containsKey(filename)) {
            File file_cwd = join(CWD, filename);
            File file_in_removal = join(Removal, filename);
            writeContents(file_in_removal, file_cwd);
            file_cwd.delete();
        } else {
            System.out.println("No reason to remove the file.");
        }
    }
    @SuppressWarnings("unchecked")
    public static void gitlog() {
        Commit traverse = readObject(Headfile, Commit.class);
            while (traverse != null) {
                System.out.println("===");
                System.out.println("commit " + sha1(serialize(traverse)));
                System.out.println("Date: "+ traverse.getTimestamp());
                System.out.println(traverse.getMessage());
                System.out.println();
                if(traverse.getParentID1() != null) {
                    CommitFilemap = readObject(CommitFilemapFile, TreeMap.class);
                    File parentfile = CommitFilemap.get(traverse.getParentID1());
                    if(parentfile != null) {
                        traverse = readObject(parentfile, Commit.class);
                    }else{
                        System.exit(0);
                    }
                }else{
                    System.exit(0);
                }
            }
    }
    public static void global_log(){
        List<String> all_commits = new ArrayList<>(plainFilenamesIn(Commits));
        while(!all_commits.isEmpty()){
            String one_commit_file_name = all_commits.removeFirst();
            File one_commit_file = join(Commits,one_commit_file_name);
            Commit one_commit = readObject(one_commit_file, Commit.class);
            System.out.println("===");
            System.out.println("commit " + sha1(serialize(one_commit)));
            System.out.println("Date: "+ one_commit.getTimestamp());
            System.out.println(one_commit.getMessage());
            System.out.println();
        }
    }
    public static void find(String commit_message) {
        List<String> all_commits = new ArrayList<>(plainFilenamesIn(Commits));
        int num = 0;
        while (!all_commits.isEmpty()){
            String one_commit_file_name = all_commits.removeFirst();
            File one_commit_file = join(Commits,one_commit_file_name);
            Commit one_commit = readObject(one_commit_file, Commit.class);
            if(one_commit.getMessage().equals(commit_message)){
                System.out.println(sha1(serialize(one_commit)));
                num += 1;
            }
        }
        if(num == 0){
            System.out.println("Found no commit with that message.");
        }
    }
    public static void git_status(){
        System.out.println("=== Branches ===");
        List<String> all_commits = new ArrayList<>(plainFilenamesIn(Branches));
        Commit nowHead = readObject(Headfile, Commit.class);
        while (!all_commits.isEmpty()){
            String one_branch_name = all_commits.removeFirst();
            File one_branch_file = join(Branches,one_branch_name);
            Commit one_branch = readObject(one_branch_file, Commit.class);
            if(one_branch_name.equals(readContentsAsString(nowbranch))){
                System.out.println("*"+one_branch_name);
            }else {
                System.out.println(one_branch_name);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        List<String> all_adds = new ArrayList<>(plainFilenamesIn(Adding));
        while(!all_adds.isEmpty()){
            System.out.println(all_adds.removeFirst());
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        List<String> all_removes = new ArrayList<>(plainFilenamesIn(Removal));
        while (!all_removes.isEmpty()){
            System.out.println(all_removes.removeFirst());
        }
        System.out.println();
    }
    public static void git_checkout(String branch_name){
        List<String> all_branchess = new ArrayList<>(plainFilenamesIn(Branches));
        if(all_branchess.contains(branch_name)) {
            File branch_file = join(Branches, branch_name);
            Commit to_checkout = readObject(branch_file, Commit.class);
            String to_checkout_id = sha1(serialize(to_checkout));
            Commit nowHead = readObject(Headfile, Commit.class);
            String head_id = sha1(serialize(nowHead));
            if(branch_name.equals(readContentsAsString(nowbranch))){
                System.out.println("No need to checkout the current branch.");
                System.exit(0);
            }
            List<String> files_cwd = new ArrayList<>(plainFilenamesIn(CWD));
            files_cwd.remove("gitlet-design.md");
            files_cwd.remove("pom.xml");
            files_cwd.remove("Makefile");
            List<String> files_add = new ArrayList<>(plainFilenamesIn(Adding));
            while (!files_cwd.isEmpty()){
                String one_file_name = files_cwd.removeFirst();
                File one_cwd_file = join(CWD,one_file_name);
                String one_cwd_file_contents = readContentsAsString(one_cwd_file);
                if(nowHead.Commitcontents.containsKey(one_file_name)){
                    String same_name_file_in_head_id = nowHead.Commitcontents.get(one_file_name);
                    File same_name_file_in_head = join(Blobs,same_name_file_in_head_id);
                    String this_contents = readContentsAsString(same_name_file_in_head);
                    if(!sha1(serialize(this_contents)).equals(sha1(serialize(one_cwd_file_contents)))){
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.1");
                        System.exit(0);
                    }
                }
                else if(files_add.contains(one_file_name)){//如果add里有该名字
                    File one_file_add = join(Adding,one_file_name);
                    String one_file_add_contents = readContentsAsString(one_file_add);
                    //如果add里的文件和cwd的不一样
                    if(!sha1(serialize(one_cwd_file_contents)).equals(sha1(serialize(one_file_add_contents)))){
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.2");
                        System.exit(0);
                    }
                }
                else {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.3");
                    System.exit(0);
                }
            }
            List<String> files_cwd2 = new ArrayList<>(plainFilenamesIn(CWD));
            files_cwd2.remove("gitlet-design.md");
            files_cwd2.remove("pom.xml");
            files_cwd2.remove("Makefile");
            while (!files_cwd2.isEmpty()) {
                String one_file_name = files_cwd2.removeFirst();
                File one_cwd_file = join(CWD, one_file_name);
                one_cwd_file.delete();
            }
            //找到branchcommit中所有文件复制到CWD
            SequencedSet<Map.Entry<String, String>> keys_values = to_checkout.Commitcontents.sequencedEntrySet();
            while(!keys_values.isEmpty()){
                Map.Entry one_pair = keys_values.removeFirst();
                File file_in_blobs = join(Blobs, (String) one_pair.getValue());
                File file_in_cwd = join(CWD, (String) one_pair.getKey());
                String file_contents = readContentsAsString(file_in_blobs);
                writeContents(file_in_cwd,file_contents);
            }

            //
            writeContents(nowbranch,branch_name);
            Commit nowbranchcommit = readObject(branch_file, Commit.class);
            writeObject(Headfile,nowbranchcommit);
        }else {
            System.out.println("No such branch exists.");
        }
    }
    public static void git_checkout(String commit_id,String filename) {
        List<String> all_commits = new ArrayList<>(plainFilenamesIn(Commits));
        if (all_commits.contains(commit_id)) {
            File commit_file = join(Commits, commit_id);
            Commit commit_to_checkout = readObject(commit_file, Commit.class);
            if (commit_to_checkout.Commitcontents.containsKey(filename)) {
                String file_to_checkout_id = commit_to_checkout.Commitcontents.get(filename);
                File file_to_checkout = join(Blobs, file_to_checkout_id);
                String contents = readContentsAsString(file_to_checkout);
                File file_in_cwd = join(CWD, filename);
                writeContents(file_in_cwd, contents);
            } else {
                System.out.println("File does not exist in that commit.");
            }
        }else {
            System.out.println("No commit with that id exists.");
        }
    }
    public static void git_head_check(String filename){
        Commit nowHead = readObject(Headfile, Commit.class);
        String headid = sha1(serialize(nowHead));
        git_checkout(headid,filename);
    }
    public static void branch(String branch_name){
        List<String> all_branches = new ArrayList<>(plainFilenamesIn(Branches));
        if(all_branches.contains(branch_name)){
            System.out.println("A branch with that name already exists.");
        }
        File new_branch = join(Branches,branch_name);
        Commit nowhead = readObject(Headfile, Commit.class);
        writeObject(new_branch,nowhead);
    }
    public static void rm_branch(String branch_name){
        String now_branch = readContentsAsString(nowbranch);
        if(branch_name.equals(now_branch)){
            System.out.println("Cannot remove the current branch.");
        }else {
            List<String> all_branches = new ArrayList<>(plainFilenamesIn(Branches));
            if(!all_branches.contains(branch_name)){
                System.out.println("A branch with that name does not exist.");
            }else {
                File br_to_rm = join(Branches,branch_name);
                br_to_rm.delete();
            }
        }
    }
    public static void reset(String commit_id){
        List<String> all_commits = new ArrayList<>(plainFilenamesIn(Commits));
        if(all_commits.contains(commit_id)) {
            File to_reset_commit_file = join(Commits, commit_id);
            //
            Commit nowHead = readObject(Headfile, Commit.class);
            List<String> files_cwd = new ArrayList<>(plainFilenamesIn(CWD));
            files_cwd.remove("gitlet-design.md");
            files_cwd.remove("pom.xml");
            files_cwd.remove("Makefile");
            List<String> files_add = new ArrayList<>(plainFilenamesIn(Adding));
            while (!files_cwd.isEmpty()){
                String one_file_name = files_cwd.removeFirst();
                File one_cwd_file = join(CWD,one_file_name);
                String one_cwd_file_contents = readContentsAsString(one_cwd_file);
                if(nowHead.Commitcontents.containsKey(one_file_name)){
                    String same_name_file_in_head_id = nowHead.Commitcontents.get(one_file_name);
                    File same_name_file_in_head = join(Blobs,same_name_file_in_head_id);
                    String this_contents = readContentsAsString(same_name_file_in_head);
                    if(!sha1(serialize(this_contents)).equals(sha1(serialize(one_cwd_file_contents)))){
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.1");
                        System.exit(0);
                    }
                }
                else if(files_add.contains(one_file_name)){//如果add里有该名字
                    File one_file_add = join(Adding,one_file_name);
                    String one_file_add_contents = readContentsAsString(one_file_add);
                    //如果add里的文件和cwd的不一样
                    if(!sha1(serialize(one_cwd_file_contents)).equals(sha1(serialize(one_file_add_contents)))){
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.2");
                        System.exit(0);
                    }
                }
                else {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.3");
                    System.exit(0);
                }
            }
            List<String> files_cwd2 = new ArrayList<>(plainFilenamesIn(CWD));
            files_cwd2.remove("gitlet-design.md");
            files_cwd2.remove("pom.xml");
            files_cwd2.remove("Makefile");
            while (!files_cwd2.isEmpty()) {
                String one_file_name = files_cwd2.removeFirst();
                File one_cwd_file = join(CWD, one_file_name);
                one_cwd_file.delete();
            }
            //
            Commit to_reset_commit = readObject(to_reset_commit_file, Commit.class);
            SequencedSet<Map.Entry<String, String>> keys_values = to_reset_commit.Commitcontents.sequencedEntrySet();
            while (!keys_values.isEmpty()) {
                Map.Entry one_pair = keys_values.removeFirst();
                String one_reset_file_name = (String) one_pair.getKey();
                git_checkout(commit_id, one_reset_file_name);
            }
            Commit to_reset_commit2 = readObject(to_reset_commit_file, Commit.class);
            writeObject(Headfile, to_reset_commit2);
        }else {
            System.out.println("No commit with that id exists.");
        }
    }
    public static void merge(String branch_name) {
        File branch_commit_file = join(Branches, branch_name);
        Commit branch_commit = readObject(branch_commit_file, Commit.class);
        File now_branch_commit_file = join(Branches, readContentsAsString(nowbranch));
        Commit now_branch_commit = readObject(now_branch_commit_file, Commit.class);
        Commit traverse_branch = readObject(branch_commit_file, Commit.class);
        while (traverse_branch.getParentID1() != null) {
            if(sha1(serialize(traverse_branch)).equals(sha1(serialize(now_branch_commit)))){
                git_checkout(branch_name);
                System.out.println("Current branch fast-forwarded.");
            }
            CommitFilemap = readObject(CommitFilemapFile, TreeMap.class);
            File parentfile1 = CommitFilemap.get(traverse_branch.getParentID1());
            if (parentfile1 != null) {
                traverse_branch = readObject(parentfile1, Commit.class);
            }
        }
        Commit traverse_nowbranch = readObject(now_branch_commit_file, Commit.class);
        while (traverse_nowbranch.getParentID1() != null) {
            if(sha1(serialize(traverse_nowbranch)).equals(sha1(serialize(branch_commit)))){
                System.out.println("Given branch is an ancestor of the current branch.");
            }
            CommitFilemap = readObject(CommitFilemapFile, TreeMap.class);
            File parentfile2 = CommitFilemap.get(traverse_nowbranch.getParentID1());
            if (parentfile2 != null) {
                traverse_nowbranch = readObject(parentfile2, Commit.class);
            }
        }
        File init_commit_file = join(Commits,"initial_commit");
        Commit init_commit = readObject(init_commit_file, Commit.class);



    }
    public static String find_first_ancestor(String init_commit_id,String id1,String id2){
        File init_commit_file = join(Commits,init_commit_id);
        Commit init_commit = readObject(init_commit_file, Commit.class);
        Commit traverse = readObject(init_commit_file, Commit.class);
        boolean a= false;
        boolean b= false;
        for(String childid: traverse.children){
                if(find_first_ancestor(childid,id1,id2).equals(id1)){
                    a = true;
                }
                if(find_first_ancestor(childid,id1,id2).equals(id2)){
                    b = true;
                }
                if(a || b ){
                    return init_commit_id;
                }
        }
        return "no";
    }
}
