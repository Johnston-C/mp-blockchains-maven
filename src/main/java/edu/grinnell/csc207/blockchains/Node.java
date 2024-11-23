package edu.grinnell.csc207.blockchains;

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

  public Node(T val) {
    this.value = val;
    this.next = null;
  } // Node(T)

  public Node(T val, Node<T> nextNode) {
    this.value = val;
    this.next = nextNode;
  } // Node(T, Node<T>)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  public T setValue(T val) {
    T temp = this.value;
    this.value = val;
    return temp;
  } // setValue(T)

  public Node<T> setNext(Node<T> nextNode) {
    Node<T> temp = this.next;
    this.next = nextNode;
    return temp;
  } // setNext(Node<T>)

  public T getValue() {
    return this.value;
  } // getValue()

  public Node<T> getNext() {
    return this.next;
  } // getNext()
}
