package edu.grinnell.csc207.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.grinnell.csc207.blockchains.Block;
import edu.grinnell.csc207.blockchains.BlockChain;
import edu.grinnell.csc207.blockchains.HashValidator;
import edu.grinnell.csc207.blockchains.Transaction;
import edu.grinnell.csc207.util.IOUtils;

/**
 * A simple UI for our BlockChain class.
 *
 * @author Cade Johnston
 * @author Sunjae Kim
 * @author Samuel A. Rebelsky
 */
public class BlockChainUI {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The number of bytes we validate. Should be set to 3 before submitting.
   */
  static final int VALIDATOR_BYTES = 0;

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Print out the instructions.
   *
   * @param pen
   *   The pen used for printing instructions.
   */
  public static void instructions(PrintWriter pen) {
    pen.println("""
      Valid commands:
        mine: discovers the nonce for a given transaction
        append: appends a new block onto the end of the chain
        remove: removes the last block from the end of the chain
        check: checks that the block chain is valid
        users: prints a list of users
        balance: finds a user's balance
        transactions: prints out the chain of transactions
        blocks: prints out the chain of blocks (for debugging only)
        help: prints this list of commands
        quit: quits the program""");
  } // instructions(PrintWriter)

  // +------+--------------------------------------------------------
  // | Main |
  // +------+

  /**
   * Run the UI.
   *
   * @param args
   *   Command-line arguments (currently ignored).
   */
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);
    BufferedReader eyes = new BufferedReader(new InputStreamReader(System.in));

    // Set up our blockchain.
    HashValidator validator =
      (hash) -> (hash.length() >= 1) && (hash.get(0) == 0);
    BlockChain chain = new BlockChain(validator);

    instructions(pen);

    boolean done = false;

    String source;
    String target;
    int amount;

    while (!done) {
      pen.print("\nCommand: ");
      pen.flush();
      String command = eyes.readLine();
      if (command == null) {
        command = "quit";
      } // if

      switch (command.toLowerCase()) {
        case "append":
          source = IOUtils.readLine(pen, eyes, "Source (return for deposit): ");
          target = IOUtils.readLine(pen, eyes, "Target: ");
          amount = IOUtils.readInt(pen, eyes, "Amount: ");
          long nonce = IOUtils.readLong(pen, eyes, "Nonce: ");
          Block b = chain.mine(new Transaction(source, target, amount));
          if (nonce == b.getNonce()) {
            try {
              chain.append(b);
              pen.println("Sucessfully appended a new block");
            } catch (IllegalArgumentException e) {
              pen.println(e.getMessage());
            }
          } else {
            pen.println("Nonce is not the expected nonce.");
          }
          
          break;

        case "balance":
          target = IOUtils.readLine(pen, eyes, "User: ");
          pen.println("Balance: " + chain.balance(target));
          break;

        case "blocks":
          Iterator<Block> printableBlock = chain.blocks();
          while (printableBlock.hasNext()) {
            pen.println(printableBlock.next());
          }
          break;

        case "check":
          try {
            chain.check();
            pen.println("No Error");
          } catch (Exception e) {
            pen.println(e.getMessage());
          }
          break;

        case "help":
          instructions(pen);
          break;

        case "mine":
          source = IOUtils.readLine(pen, eyes, "Source (return for deposit): ");
          target = IOUtils.readLine(pen, eyes, "Target: ");
          amount = IOUtils.readInt(pen, eyes, "Amount: ");
          Block d = chain.mine(new Transaction(source, target, amount));
          pen.println("Nonce: " + d.getNonce());
          break;

        case "quit":
          done = true;
          break;

        case "remove":
          if(chain.removeLast()){
            pen.println("Block successfully removed.");
          } else{
            pen.println("Failed to remove block.");
          }
          break;

        case "transactions":
          Iterator<Transaction> printableTransaction = chain.iterator();
          while (printableTransaction.hasNext()) { 
            pen.println(printableTransaction.next());
          }
          break;

        case "users":
          Iterator<String> printableUsers = chain.users();
          while(printableUsers.hasNext()){
            pen.println(printableUsers.next());
          }
          break;

        default:
          pen.printf("invalid command: '%s'. Try again.\n", command);
          break;
      } // switch
    } // while

    pen.printf("\nGoodbye\n");
    eyes.close();
    pen.close();
  } // main(String[])
} // class BlockChainUI
