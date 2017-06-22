package org.avl;

import static java.lang.Math.max;

/**
 * AVL树
 */
public class AvlTree {
    private Node root;

    public boolean add(int n) {
        this.root = add(this.root, n);

        return true;
    }

    private Node add(Node node, int val) {
        if (node == null) {
            node = new Node(val, null, null);         // 寻找要插入的位置
        }
        else if (val > node.val) {
            node.rchild = add(node.rchild, val);

            if (unbalance(node)) {
                // 失去平衡, 通过旋转来重新平衡
                if (val > node.rchild.val) {
                    node = leftRotate(node);        // rr, 左旋
                }
                else if (val < node.rchild.val) {
                    node = rightLeftRotate(node);       // rl
                }
            }
        }
        else if (val < node.val) {
            node.lchild = add(node.lchild, val);

            if (unbalance(node)) {
                if (val < node.lchild.val) {
                    node = rightRotate(node);       // ll, 右旋
                }
                else if (val > node.lchild.val) {
                    node = leftRightRotate(node);       // lr
                }
            }
        }
        else {
            throw new IllegalArgumentException("should not happen!");
        }

        node.height = max(height(node.lchild), height(node.rchild)) + 1;
        return node;
    }

    private static boolean unbalance(Node node) {
        return Math.abs(height(node.rchild) - height(node.lchild)) >= 2;
    }

    public boolean contains(int n) {
        return false;
    }

    public boolean delete(int n) {
        return false;
    }


    /**
     * 向a的左子树的左子树插入节点, 右旋
     * <p>
     *             a
     *           /  \
     *         b     x
     *       /
     *     c
     */
    private Node rightRotate(Node a) {
        Node b = a.lchild;
        a.lchild = b.rchild;
        b.rchild = a;
        a.height = max(height(a.lchild), height(a.rchild)) + 1;
        b.height = max(height(b.rchild), height(b.rchild)) + 1;
        return b;
    }


    /**
     * 左旋
     */
    private Node leftRotate(Node a) {
        Node b = a.rchild;
        a.rchild = b.lchild;
        b.lchild = a;
        a.height = max(height(a.lchild), height(a.rchild)) + 1;
        b.height = max(height(b.rchild), height(b.rchild)) + 1;
        return b;
    }


    /**
     * LR型
     *           a
     *          /
     *         b
     *          \
     *           c
     */
    private Node leftRightRotate(Node a) {
        a.lchild = leftRotate(a.lchild);
        return rightRotate(a);
    }

    private Node rightLeftRotate(Node a) {
        a.rchild = rightRotate(a.rchild);
        return leftRotate(a);
    }

    /**
     * height of the {@code node}
     */
    private static int height(Node node) {
        return node == null ? 0 : node.height;
    }

    @Override
    public String toString() {
        return "AvlTree{root=" + (root == null ? "" : root) + '}';
    }

    public Node getRoot() {
        return root;
    }


    static class Node {
        int val;
        int height;
        Node lchild;
        Node rchild;

        Node(int n, Node lchild, Node rchild) {
            this.val = n;
            this.lchild = lchild;
            this.rchild = rchild;
        }

        @Override
        public String toString() {
            return "Node{val=" + val + '}';
        }
    }
}
