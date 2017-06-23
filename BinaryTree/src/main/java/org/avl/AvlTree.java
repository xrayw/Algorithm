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

    private static Node add(Node node, int val) {
        if (node == null) {
            node = new Node(val, null, null);         // 寻找要插入的位置
        }
        else if (val > node.val) {
            node.rightChild = add(node.rightChild, val);

            if (unbalance(node)) {
                // 失去平衡, 通过旋转来重新平衡
                if (val > node.rightChild.val) {
                    node = leftRotate(node);        // rr, 左旋
                }
                else if (val < node.rightChild.val) {
                    node = rightLeftRotate(node);       // rl
                }
            }
        }
        else if (val < node.val) {
            node.leftChild = add(node.leftChild, val);

            if (unbalance(node)) {
                if (val < node.leftChild.val) {
                    node = rightRotate(node);       // ll, 右旋
                }
                else if (val > node.leftChild.val) {
                    node = leftRightRotate(node);       // lr
                }
            }
        }
        else {
            throw new IllegalArgumentException("should not happen!");
        }

        node.height = max(height(node.leftChild), height(node.rightChild)) + 1;
        return node;
    }

    private static boolean unbalance(Node node) {
        return Math.abs(height(node.rightChild) - height(node.leftChild)) >= 2;
    }

    public boolean contains(int n) {
        Node node = this.root;
        while (node != null) {
            int val = node.val;
            if (n == val) {
                return true;
            }
            else if (n > val) {
                node = node.rightChild;
            }
            else {
                node = node.leftChild;
            }
        }
        return false;
    }

    public boolean delete(int n) {
        return false;
    }

    private static Node delete(Node node, int n) {
        if (node == null) {
            return null;
        }

        if (n == node.val) {

        }
        else if (n > node.val) {

        }
        else {

        }
        return node;
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
    private static Node rightRotate(Node a) {
        Node b = a.leftChild;
        a.leftChild = b.rightChild;
        b.rightChild = a;
        a.height = max(height(a.leftChild), height(a.rightChild)) + 1;
        b.height = max(height(b.rightChild), height(b.rightChild)) + 1;
        return b;
    }


    /**
     * 左旋
     */
    private static Node leftRotate(Node a) {
        Node b = a.rightChild;
        a.rightChild = b.leftChild;
        b.leftChild = a;
        a.height = max(height(a.leftChild), height(a.rightChild)) + 1;
        b.height = max(height(b.rightChild), height(b.rightChild)) + 1;
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
    private static Node leftRightRotate(Node a) {
        a.leftChild = leftRotate(a.leftChild);
        return rightRotate(a);
    }

    private static Node rightLeftRotate(Node a) {
        a.rightChild = rightRotate(a.rightChild);
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
        Node leftChild;
        Node rightChild;

        Node(int n, Node leftChild, Node rightChild) {
            this.val = n;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        @Override
        public String toString() {
            return "Node{val=" + val + '}';
        }
    }
}
