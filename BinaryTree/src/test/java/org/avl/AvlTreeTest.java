package org.avl;

import org.junit.Test;

public class AvlTreeTest {
    @Test
    public void add() {
        AvlTree avlTree = new AvlTree();
        avlTree.add(4);
        avlTree.add(5);
        avlTree.add(6);
        avlTree.add(3);
        avlTree.add(2);
        avlTree.add(8);
        avlTree.add(7);
        avlTree.add(0);
        avlTree.add(1);
        System.out.println(avlTree);
    }
}
