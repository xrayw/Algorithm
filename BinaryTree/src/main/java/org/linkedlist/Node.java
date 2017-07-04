package org.linkedlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 单链表节点
 */
@Getter
@Setter
@AllArgsConstructor
public class Node{
    int val;
    Node next;
}
