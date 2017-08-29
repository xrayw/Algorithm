package org.avl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AvlTreeTest {
    private AvlTree tree;

    private Set<Integer> nodesSet;

    @Before
    public void before() {
        int number = 100000;

        tree = new AvlTree();
        nodesSet = new HashSet<>(number);
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int val = random.nextInt(Integer.MAX_VALUE);
            tree.add(val);

            nodesSet.add(val);
        }
    }

    @Test
    //@Ignore
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

        //AvlTree avl = new AvlTree();
        //avl.add(8);
        //avl.add(12);
        //avl.add(4);
        //avl.add(2);
        //avl.add(6);
        //avl.add(5);
        //
        //avl.remove(8);
        //avl.remove(12);
        //avl.remove(4);
        //avl.remove(2);
        //avl.remove(5);


        int number = 10000000;

        AvlTree tree = new AvlTree();
        Set<Integer> nodesSet = new HashSet<>(number);
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int val = random.nextInt(Integer.MAX_VALUE);
            tree.add(val);

            nodesSet.add(val);
        }

        nodesSet.forEach(tree::remove);
    }

    @Test
    @Ignore
    public void getNodeNumber() {
        int nodeNumber = tree.getNodeNumber();
        assertThat(nodeNumber, is(nodesSet.size()));
    }

    @Test
    @Ignore
    public void getDepth() {
        System.out.println(tree.getDepth());
        System.out.println(tree.getRoot().getHeight());
    }

    @Test
    @Ignore
    public void getLeafNumber() {
        int leafNodeNumber = tree.getLeafNodeNumber(tree.getRoot());
        System.out.println(leafNodeNumber);
    }

    @Test
    @Ignore
    public void balance() {
        AvlTree tree = new AvlTree();
        tree.add(1);
        tree.add(2);
        tree.add(3);

        System.out.println(tree.isAvl());

        AvlTree.TreeNode node = new AvlTree.TreeNode(1, null, null);
        AvlTree.TreeNode two = new AvlTree.TreeNode(2, null, null);
        node.setRightChild(two);
        AvlTree.TreeNode three = new AvlTree.TreeNode(3, null, null);
        two.setRightChild(three);

        System.out.println(AvlTree.isBalanced(node));
    }

    /**
     * 测试分层遍历
     */
    @Test
    @Ignore
    public void levelTraverse() {
        AvlTree avl = new AvlTree();
        avl.add(8);
        avl.add(12);
        avl.add(4);
        avl.add(2);
        avl.add(6);
        avl.add(5);
        avl.levelTraverse(x -> {
            System.out.print(x);
            System.out.print(" ");
        });

        System.out.println();

        // 测试镜像
        AvlTree.mirror(avl.getRoot());
        avl.levelTraverse(x -> {
            System.out.print(x);
            System.out.print(" ");
        });
    }

    private static void preOrder(AvlTree.TreeNode node) {
        if (node != null) {
            preOrder(node.leftChild);
            System.out.println(node.val);
            preOrder(node.rightChild);
        }
    }
}
