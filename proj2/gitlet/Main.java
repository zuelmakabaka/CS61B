package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if(args == null){
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        int input_num = args.length;
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if(input_num != 1){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.initrepo();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                if(input_num < 2){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.addfile(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                if (input_num == 1){
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.createcommit(args[1]);
                break;
            case "rm":
                if(input_num != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.rm_file(args[1]);
                break;
            case "log":
                if(input_num != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.gitlog();
                break;
            case "global-log":
                if(input_num != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.global_log();
                break;
            case "find":
                if(input_num != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.find(args[1]);
                break;
            case "status":
                if(input_num != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.git_status();
                break;
            case "checkout":
                if(input_num == 2){
                    Repository.git_checkout(args[1]);
                } else if (input_num == 4) {
                    Repository.git_checkout(args[1],args[3]);
                } else if (input_num == 3) {
                    Repository.git_head_check(args[2]);
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "branch":
                if(input_num != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                if(input_num != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.rm_branch(args[1]);
                break;
            case "reset":
                if(input_num != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.reset(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
