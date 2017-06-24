package org.avl;

import org.junit.Test;

public class AvlTreeTest {
    @Test
    public void add() {
        //AvlTree avlTree = new AvlTree();
        //avlTree.add(4);
        //avlTree.add(5);
        //avlTree.add(6);
        //avlTree.add(3);
        //avlTree.add(2);
        //avlTree.add(8);
        //avlTree.add(7);
        //avlTree.add(0);
        //avlTree.add(1);

        AvlTree avl = new AvlTree();
        avl.add(8);
        avl.add(12);
        avl.add(4);
        avl.add(2);
        avl.add(6);
        avl.add(5);

        avl.remove(8);
        avl.remove(12);
        avl.remove(4);
        avl.remove(2);
        avl.remove(4);
        System.out.println(avl);
    }

    static void preOrder(AvlTree.Node node) {
        if (node != null) {
            preOrder(node.leftChild);
            System.out.println(node.val);
            preOrder(node.rightChild);
        }
    }
}
