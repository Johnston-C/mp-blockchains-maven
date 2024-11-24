package edu.grinnell.csc207.blockchains;

import java.util.Arrays;

/**
 * Encapsulated hashes.
 *
 * @author Cade Johnston
 * @author Sunjae Kim
 * @author Samuel A. Rebelsky
 */
public class Hash {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The bytes stored in this object. */
  byte[] data;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new encapsulated hash.
   *
   * @param data The data to copy into the hash.
   */
  public Hash(byte[] data) {
    this.data = Arrays.copyOf(data, data.length);
  } // Hash(byte[])

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Determine how many bytes are in the hash.
   *
   * @return the number of bytes in the hash.
   */
  public int length() {
    return this.data.length;
  } // length()

  /**
   * Get the ith byte.
   *
   * @param i The index of the byte to get, between 0 (inclusive) and length() (exclusive).
   * @return the ith byte
   */
  public byte get(int i) {
    // Accessing out of range will throw an exception anyways,
    // so no need to check.
    return data[i];
  } // get()

  /**
   * Get a copy of the bytes in the hash. We make a copy so that the client cannot change them.
   *
   * @return a copy of the bytes in the hash.
   */
  public byte[] getBytes() {
    return Arrays.copyOf(data, length());
  } // getBytes()

  /**
   * Convert to a hex string.
   *
   * @return the hash as a hex string.
   */
  public String toString() {
    String output = "";
    for (int i = 0; i < length(); i++) {
      if (data[i] >= 0) {
        output += numToHex(data[i] / 16);
        output += numToHex(data[i] % 16);
      } else {
        output += numToHex((data[i] + 128) / 16 + 8);
        output += numToHex((data[i] + 128) % 16);
      } // if / else
    } // for [i]
    return output;
  } // toString()

  /**
   * Convert a number between 0 and 15 to its hex equivalent. The output is a string.
   *
   * @param num1 The number to convert from base 10 to base 16.
   * @return The number converted. If the number is not between 0 and 15 (inclusive), return an
   *     empty string.
   */
  private String numToHex(int num1) {
    switch (num1) {
      case 0:
        return "0";
      case 1:
        return "1";
      case 2:
        return "2";
      case 3:
        return "3";
      case 4:
        return "4";
      case 5:
        return "5";
      case 6:
        return "6";
      case 7:
        return "7";
      case 8:
        return "8";
      case 9:
        return "9";
      case 10:
        return "A";
      case 11:
        return "B";
      case 12:
        return "C";
      case 13:
        return "D";
      case 14:
        return "E";
      case 15:
        return "F";
      default:
        return "";
    } // switch
  } // numToHex(int)

  /**
   * Determine if this is equal to another object.
   *
   * @param other The object to compare to.
   * @return true if the two objects are conceptually equal and false otherwise.
   */
  public boolean equals(Object other) {
    return (other instanceof Hash) && (this.equals((Hash) other));
  } // equals(Object)

  /**
   * Determine if this is equal to another Hash.
   *
   * @param other The object to compare to.
   * @return true if the two objects are conceptually equal and false otherwise.
   */
  public boolean equals(Hash other) {
    return Arrays.equals(getBytes(), other.getBytes());
  } // equals(Hash)

  /**
   * Get the hash code of this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    return this.toString().hashCode();
  } // hashCode()
} // class Hash
