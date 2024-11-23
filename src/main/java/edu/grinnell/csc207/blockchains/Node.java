package edu.grinnell.csc207.blockchains;

/**
 * An implementation of the node class, parameterized.
 *
 * @param <T>
 *   The type of value stored in the node. The next node must also be
 *   a node of this type.
 *
 * @author Cade Johnston
 * @author Sunjae Kim
 */
public class Node<T> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The value stored in the node. */
  private T value;

  /** The next node. */
  private Node<T> next;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a node with a value, but no next node.
   *
   * @param val
   *   The value of the node.
   */
  public Node(T val) {
    this.value = val;
    this.next = null;
  } // Node(T)

  /**
   * Create a node with a value and a next node.
   *
   * @param val
   *   The value of the node.
   * @param nextNode
   *   The next node.
   */
  public Node(T val, Node<T> nextNode) {
    this.value = val;
    this.next = nextNode;
  } // Node(T, Node<T>)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Set the value of this node. Return the old value.
   *
   * @param val
   *   The value to change this node to.
   * @return
   *   The old value of the node.
   */
  public T setValue(T val) {
    T temp = this.value;
    this.value = val;
    return temp;
  } // setValue(T)

  /**
   * Set the next node this object refers to. Returns the old value.
   *
   * @param nextNode
   *   The new next node to point to.
   * @return
   *   The old value of next.
   */
  public Node<T> setNext(Node<T> nextNode) {
    Node<T> temp = this.next;
    this.next = nextNode;
    return temp;
  } // setNext(Node<T>)

  /**
   * Get the value of this node.
   *
   * @return
   *   The value of this node.
   */
  public T getValue() {
    return this.value;
  } // getValue()

  /**
   * Get the next node.
   *
   * @return
   *   The next node.
   */
  public Node<T> getNext() {
    return this.next;
  } // getNext()
} // Class Node
