package org.linkedlist;

import org.junit.Test;

/**
 * 测试反转链表
 */
public class ReverseTest {
  @Test
  public void reverse() {
    Node head = new Node(1, null);
    Node c = head;
    for (int i = 2; i <= 16; i++) {
      c.next = new Node(i, null);
      c = c.next;
    }

    print(head);

    Node reversed = ReverseList.reverse(head);
    print(reversed);
  }

  private void print(Node head) {
    while (head != null) {
      System.out.print(head.val);
      if (head.next != null) {
        System.out.print("->");
      }
      head = head.next;
    }
    System.out.println();
  }
}
