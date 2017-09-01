package org.linkedlist;

/**
 * 反转单链表
 */
public class ReverseList {

  public static Node reverse(Node head) {
    Node current = head;
    Node newHead = null;
    while (current != null) {
      head = current.next;
      current.next = newHead;
      newHead = current;
      current = head;
    }
    return newHead;
  }
}
