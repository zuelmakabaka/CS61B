package gitlet;

// TODO: any imports you need here
//set存储Blob的sha1
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import org.junit.Test;

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String parentID1;
    private String parentID2 = null;
    public String[] children;
    public int index;

    public TreeMap<String, String> Commitcontents = new TreeMap<>();//文件名，文件id

    /* TODO: fill in the rest of this class. */
    public Commit(){
        this.parentID1 = null;
        this.parentID2 = null;
        this.timestamp = new Date(0);
        this.message = "initial commit";
        this.children = null;
        this.index = 0;
    }

    public Commit(String message,String parentID1, String parentID2){
        this.message = message;
        this.parentID1 = parentID1;
        this.parentID2 = parentID2;
        this.timestamp = new Date();
        this.children = null;
        this.index = 0;
    }

    public String getMessage(){
        return this.message;
    }
    public String getParentID1(){
        return this.parentID1;
    }
    public String getParentID2(){
        return this.parentID2;
    }
    public Date getTimestamp(){
        return this.timestamp;
    }
}
