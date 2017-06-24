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
            // pass
            return node;
        }

        node.height = max(height(node.leftChild), height(node.rightChild)) + 1;
        return node;
    }

    private static boolean unbalance(Node node) {
        int abs = Math.abs(height(node.rightChild) - height(node.leftChild));
        if (abs > 2) {
            throw new IllegalArgumentException("should not happen");
        }
        return abs == 2;
    }

    public boolean contains(int n) {
        Node node = this.root;
        while (node != null) {
            int val = node.val;
            if (n > val) {
                node = node.rightChild;
            }
            else if (n < val) {
                node = node.leftChild;
            }
            else {
                return true;
            }
        }
        return false;
    }

    public boolean remove(int n) {
        this.root = remove(this.root, n);
        return true;
    }

    private static Node remove(Node node, int n) {
        if (node == null) {
            return null;
        }

        if (n == node.val) {
            if (node.leftChild != null && node.rightChild != null) {
                // 在高度较大的一侧选择节点替换要删除的节点, 这样删除节点后, avl树依然是平衡的
                if (height(node.leftChild) > height(node.rightChild)) {
                    // 找出左子树的最大节点替换待删除节点
                    Node max = maximum(node.leftChild);
                    node.val = max.val;
                    node.leftChild = remove(node.leftChild, max.val);
                }
                else {
                    // 找出右子树的最小节点替换待删除节点
                    Node min = minimum(node.rightChild);
                    node.val = min.val;
                    node.rightChild = remove(node.rightChild, min.val);
                }
            }
            else  {
                if (node.leftChild == null) {
                    node = node.rightChild;
                }
                else {
                    node = node.leftChild;
                }
                return node;
            }
        }
        else if (n > node.val) {
            node.rightChild = remove(node.rightChild, n);
            // 删除右子节点导致的不平衡,
            if (unbalance(node)) {
                if (height(node.leftChild.rightChild) > height(node.leftChild.leftChild)) {
                    node = leftRightRotate(node);
                }
                else {
                    node = rightRotate(node);
                }
            }
        }
        else {
            node.leftChild = remove(node.leftChild, n);
            if (unbalance(node)) {
                if (height(node.rightChild.leftChild) > height(node.rightChild.rightChild)) {
                    node = rightLeftRotate(node);
                }
                else {
                    node = leftRotate(node);
                }
            }
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
        b.height = max(height(b.leftChild), height(b.rightChild)) + 1;
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
        b.height = max(height(b.leftChild), height(b.rightChild)) + 1;
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
     * if node is null, return -1
     */
    private static int height(Node node) {
        return node == null ? -1 : node.height;
    }

    private static Node maximum(Node node) {
        if (node != null) {
            while (node.rightChild != null) {
                node = node.rightChild;
            }
        }
        return node;
    }

    private static Node minimum(Node node) {
        if (node != null) {
            while (node.leftChild != null) {
                node = node.leftChild;
            }
        }
        return node;
    }

    @Override
    public String toString() {
        return "AvlTree{root=" + (root == null ? "" : root) + '}';
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
