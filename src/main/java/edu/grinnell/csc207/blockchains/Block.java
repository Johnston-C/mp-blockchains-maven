package edu.grinnell.csc207.blockchains;

/**
 * Blocks to be stored in blockchains.
 *
 * @author Your Name Here
 * @author Samuel A. Rebelsky
 */
public class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
/**number of the block */
  private int num;
  /**This is the transaction of the block.*/
  private Transaction transaction;
  /**This is the Previous hash of the block*/
  private Hash prevHash;
  /**This is the block's own hash. */
  private Hash ownHash;
  /**This is the nonce of the block. */
  private int nonce;



  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new block from the specified block number, transaction, and
   * previous hash, mining to choose a nonce that meets the requirements
   * of the validator.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param check
   *   The validator used to check the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, 
      HashValidator check) {
        this.num = num;
        this.transaction = transaction;
        this.prevHash = prevHash;
        this.ownHash = null;
        this.nonce = 0;



    // STUB
  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Create a new block, computing the hash for the block.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param nonce
   *   The nonce of the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    // STUB
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   */
  static void computeHash() {
    
    // STUB
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return this.num;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return this.transaction;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return this.nonce;
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  Hash getHash() {
    return this.ownHash;
  } // getHash

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  public String toString() {
    String output = "Block " + this.num + " (Transaction: [";
    if(this.transaction.getSource().equals("")){
      output += "Deposit, ";
    } else {
      output += "Source: " + this.transaction.getSource() + ", ";
    } //end of else case
    output += "Target " + this.transaction.getTarget() + ", Amount: " + this.transaction.getAmount();
    output += "], Nonce: " + this.nonce + ", prevHash: " + this.prevHash + ", hash: " + this.ownHash;
    return output;
  } // toString()
} // class Block
