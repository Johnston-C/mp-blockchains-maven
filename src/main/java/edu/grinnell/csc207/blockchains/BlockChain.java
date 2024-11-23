package edu.grinnell.csc207.blockchains;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;

/**
 * A full blockchain.
 *
 * @author Cade Johnston
 * @author Sunjae Kim
 * @author Samuel A. Rebelsky
 */
public class BlockChain implements Iterable<Transaction> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The current size of the BlockChain. */
  int size;

  /** The first block in the BlockChain. */
  Node<Block> first;

  /** The last block in the BlockChain. */
  Node<Block> last;

  /** The validator for the blockchain's hasing method. */
  HashValidator checker;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new blockchain using a validator to check elements.
   *
   * @param check
   *   The validator used to check elements.
   */
  public BlockChain(HashValidator check) {
    this.size = 1;
    this.checker = check;
    this.first = new Node<Block>(new Block(0, new Transaction("", "", 0),
        new Hash(new byte[] {}), this.checker));
    this.last = this.first;
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Determine if a block's hash is incorrect, based on the fields it has.
   *
   * @param blk
   *   The block to check.
   * @return
   *   If the block's hash is incorrect.
   */
  private boolean hashIncorrect(Block blk) {
    return (!(new Block(blk.getNum(), blk.getTransaction(),
        blk.getPrevHash(), blk.getNonce())).getHash().equals(blk.getHash()));
  } // boolean

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Mine for a new valid block for the end of the chain, returning that
   * block.
   *
   * @param t
   *   The transaction that goes in the block.
   *
   * @return a new block with correct number, hashes, and such.
   */
  public Block mine(Transaction t) {
    return new Block(this.size, t, this.last.getValue().getHash(), this.checker);
  } // mine(Transaction)

  /**
   * Get the number of blocks curently in the chain.
   *
   * @return the number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return this.size;
  } // getSize()

  /**
   * Add a block to the end of the chain.
   *
   * @param blk
   *   The block to add to the end of the chain.
   *
   * @throws IllegalArgumentException if (a) the hash is not valid, (b)
   *   the hash is not appropriate for the contents, or (c) the previous
   *   hash is incorrect.
   */
  public void append(Block blk) {
    if (checker.isValid(blk.getHash())) {
      if (this.last.getValue().getHash().equals(blk.getPrevHash())) {
        if (hashIncorrect(blk)) {
          throw new IllegalArgumentException("The hash is not appropriate for the contents.");
        } else {
          size++;
          this.last.setNext(new Node<Block>(blk));
          this.last = this.last.getNext();
        } // if / else
      } else {
        throw new IllegalArgumentException("The previous hash is incorrect.");
      } // if / else
    } else {
      throw new IllegalArgumentException("The hash is not valid.");
    } // if / else
  } // append()

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *   not removed) or true otherwise (in which case the last block
   *   is removed).
   */
  public boolean removeLast() {
    if (size == 1) {
      return false;
    } else {
      Node<Block> cursor = this.first;
      while (cursor.getNext().getNext() != null) {
        cursor = cursor.getNext();
      } // while
      this.last = cursor;
      this.last.setNext(null);
      size--;
      return true;
    } // if / else
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last sblock in the chain.
   */
  public Hash getHash() {
    return this.last.getValue().getHash();
  } // getHash()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @return true if the blockchain is correct and false otherwise.
   */
  public boolean isCorrect() {
    try {
      check();
      return true;
    } catch (Exception e) {
      return false;
    } // try / catch
  } // isCorrect()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception
   *   If things are wrong at any block.
   */
  public void check() throws Exception {
    int balance;
    Iterator<String> users = this.users();
    int errorIndex = -1;
    String errorString = "";
    boolean noError;
    while (users.hasNext()) {
      noError = true;
      String user = users.next();
      balance = 0;
      Iterator<Block> blocks = this.blocks();
      while ((blocks.hasNext()) && (noError)) {
        Block block = blocks.next();
        if (block.getTransaction().getSource().equals(user)) {
          balance -= block.getTransaction().getAmount();
        } // if
        if (block.getTransaction().getTarget().equals(user)) {
          balance += block.getTransaction().getAmount();
        } // if
        if (balance < 0) {
          if ((errorIndex > block.getNum()) || (errorIndex == -1)) {
            errorString = "User \"" + user + "\" had a negative balance after block "
                + block.getNum() + ".";
            errorIndex = block.getNum();
          } // if
          noError = false;
        } // if
      } // while
    } // while
    noError = true;
    Iterator<Block> blocks = blocks();
    Block lastBlock = blocks.next();
    Block nextBlock;
    while ((noError) && (blocks.hasNext())) {
      nextBlock = blocks.next();
      if (nextBlock.getPrevHash().equals(lastBlock.getHash())) {
        lastBlock = nextBlock;
      } else {
        if ((errorIndex > lastBlock.getNum()) || (errorIndex == -1)) {
          errorString = "Block " + lastBlock.getNum()
              + " has a prevHash that is different from block "
              + nextBlock.getNum() + "'s ownHash.";
          errorIndex = lastBlock.getNum();
        } // if
        noError = false;
      } // if / else
    } // while
    blocks = blocks();
    lastBlock = blocks.next();
    noError = true;
    while ((noError) && (blocks.hasNext())) {
      lastBlock = blocks.next();
      if (lastBlock.getTransaction().getAmount() < 0) {
        if ((errorIndex > lastBlock.getNum()) || (errorIndex == -1)) {
          errorString = "Block " + lastBlock.getNum()
              + " has a negative amount for its transaction.";
          errorIndex = lastBlock.getNum();
        } // if
        noError = false;
      } // if
      if (hashIncorrect(lastBlock)) {
        if (errorIndex < lastBlock.getNum()) {
          errorString = "Block " + lastBlock.getNum() + " has an incorrect hash for its contents.";
          errorIndex = lastBlock.getNum();
        } // if
        noError = false;
      } // if
    } // while
    blocks = blocks();
    lastBlock = blocks.next();
    noError = true;
    while ((noError) && (blocks.hasNext())) {
      lastBlock = blocks.next();
      if (!(checker.isValid(lastBlock.getHash()))) {
        if ((errorIndex > lastBlock.getNum()) || (errorIndex == -1)) {
          errorString = "Block " + lastBlock.getNum() + " has an invalid hash.";
          errorIndex = lastBlock.getNum();
        } // if
        noError = false;
      } // if
    } // while
    if (errorIndex != -1) {
      throw new Exception(errorString);
    } // if
  } // check()

  /**
   * Return an iterator of all the people who participated in the
   * system.
   *
   * @return an iterator of all the people in the system.
   */
  public Iterator<String> users() {
    return new Iterator<String>() {
      Node<Block> cursor = first;
      String[] past = new String[]{""};

      public boolean hasNext() {
        while (this.cursor != null) {
          if (isNew(this.cursor.getValue().getTransaction().getSource())) {
            return true;
          } // if
          if (isNew(this.cursor.getValue().getTransaction().getTarget())) {
            return true;
          } // if
          this.cursor = this.cursor.getNext();
        } // while
        return false;
      } // hasNext()

      public String next() {
        if (hasNext()) {
          if (isNew(this.cursor.getValue().getTransaction().getSource())) {
            past = Arrays.copyOf(past, past.length + 1);
            past[past.length - 1] = this.cursor.getValue().getTransaction().getSource();
            return this.cursor.getValue().getTransaction().getSource();
          } // if
          if (isNew(this.cursor.getValue().getTransaction().getTarget())) {
            past = Arrays.copyOf(past, past.length + 1);
            past[past.length - 1] = this.cursor.getValue().getTransaction().getTarget();
            return this.cursor.getValue().getTransaction().getTarget();
          } // if
        } // if
        throw new NoSuchElementException();
      } // next()

      private boolean isNew(String arg) {
        for (String str : past) {
          if (str.equals(arg)) {
            return false;
          } // if
        } // for
        return true;
      } // isNew(String)
    };
  } // users()

  /**
   * Find one user's balance.
   *
   * @param user
   *   The user whose balance we want to find.
   *
   * @return that user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    int bal = 0;
    Iterator<Block> blocks = this.blocks();
    while (blocks.hasNext()) {
      Block block = blocks.next();
      if (block.getTransaction().getSource().equals(user)) {
        bal -= block.getTransaction().getAmount();
      } // if
      if (block.getTransaction().getTarget().equals(user)) {
        bal += block.getTransaction().getAmount();
      } // if
    } // while
    return bal;
  } // balance()

  /**
   * Get an interator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      Node<Block> cursor = null;

      @Override
      public boolean hasNext() {
        return ((cursor == null) || (cursor.getNext() != null));
      } // hasNext()

      @Override
      public Block next() {
        if (hasNext()) {
          if (cursor == null) {
            cursor = first;
          } else {
            cursor = cursor.getNext();
          } // if / else
          return cursor.getValue();
        } else {
          throw new NoSuchElementException();
        } // if / else
      } // next()
    };
  } // blocks()

  /**
   * Get an interator for all the transactions in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  @Override
  public Iterator<Transaction> iterator() {
    return new Iterator<Transaction>() {
      Node<Block> cursor = null;

      @Override
      public boolean hasNext() {
        return ((cursor == null) || (cursor.getNext() != null));
      } // hasNext()

      @Override
      public Transaction next() {
        if (hasNext()) {
          if (cursor == null) {
            cursor = first;
          } else {
            cursor = cursor.getNext();
          } // if / else
          return cursor.getValue().getTransaction();
        } else {
          throw new NoSuchElementException();
        } // if / else
      } // next()
    };
  } // iterator()

} // class BlockChain
