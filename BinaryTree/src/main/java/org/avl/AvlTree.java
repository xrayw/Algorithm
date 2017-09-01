package org.avl;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 * AVL树
 * Reference: http://blog.csdn.net/luckyxiaoqiang/article/details/7518888/
 */
public class AvlTree {
  @Getter
  private TreeNode root;

  public boolean add(int n) {
    this.root = add(this.root, n);
    return true;
  }

  private static TreeNode add(TreeNode node, int val) {
    if (node == null) {
      node = new TreeNode(val, null, null);         // 寻找要插入的位置
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

    updateHeight(node);
    return node;
  }

  private static boolean unbalance(TreeNode node) {
    int abs = Math.abs(height(node.rightChild) - height(node.leftChild));
    if (abs > 2) {
      throw new IllegalStateException("should not happen");
    }
    return abs >= 2;
  }

  public boolean contains(int n) {
    TreeNode node = this.root;
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

  private static TreeNode remove(TreeNode node, int n) {
    if (node == null) {
      return null;
    }

    if (n == node.val) {
      if (node.leftChild != null && node.rightChild != null) {

        if (height(node.leftChild) > height(node.rightChild)) {

          // 在高度较大的一侧选择节点替换要删除的节点, 这样删除节点后, avl树依然是平衡的
          // 找出左子树的最大节点替换待删除节点
          TreeNode max = maximum(node.leftChild);
          node.val = max.val;
          node.leftChild = remove(node.leftChild, max.val);
        }
        else {
          // 找出右子树的最小节点替换待删除节点
          TreeNode min = minimum(node.rightChild);
          node.val = min.val;
          node.rightChild = remove(node.rightChild, min.val);
        }
      }
      else {
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

    updateHeight(node);
    return node;
  }

  private static void updateHeight(TreeNode node) {
    if (node != null) {
      node.height = max(height(node.leftChild), height(node.rightChild)) + 1;
    }
  }

  public int getNodeNumber() {
    return this.getNodeNumber(root);
  }

  /**
   * 获取节点数
   */
  private static int getNodeNumber(TreeNode root) {
    if (root == null) {
      return 0;
    }

    return getNodeNumber(root.leftChild) + getNodeNumber(root.rightChild) + 1;
  }

  public int getDepth() {
    return getDepth(root);
  }

  /**
   * 获取以root为根节点的树的深度
   */
  private static int getDepth(TreeNode root) {
    if (root == null) {
      return 0;
    }
    int leftDepth = getDepth(root.leftChild);
    int rightDepth = getDepth(root.rightChild);
    return max(leftDepth, rightDepth) + 1;
  }

  public void levelTraverse(Consumer<Integer> consumer) {
    Objects.requireNonNull(consumer);
    levelTraverse(root, consumer);
  }

  /**
   * 分层遍历二叉树
   */
  private static void levelTraverse(TreeNode root, Consumer<Integer> consumer) {
    if (root != null) {
      Queue<TreeNode> queue = new LinkedList<>();
      queue.offer(root);
      TreeNode node;
      while ((node = queue.poll()) != null) {
        consumer.accept(node.getVal());        // accept node val

        if (node.getLeftChild() != null) {
          queue.offer(node.getLeftChild());
        }
        if (node.getRightChild() != null) {
          queue.offer(node.getRightChild());
        }
      }
    }
  }


  /**
   * 获取二叉树中叶子节点个数
   */
  public static int getLeafNodeNumber(TreeNode root) {
    if (root == null) {
      return 0;
    }

    if (root.getLeftChild() == null && root.getRightChild() == null) {
      return 1;
    }

    return getLeafNodeNumber(root.getLeftChild()) + getLeafNodeNumber(root.getRightChild());
  }

  public boolean isAvl() {
    return isBalanced(root) >= 0;
  }

  /**
   * 判断二叉树是不是avl树
   *
   * @return -1 if is not balance
   */
  public static int isBalanced(TreeNode root) {
    if (root == null) {
      return 0;
    }

    int leftHeight = isBalanced(root.leftChild);
    int rightHeight = isBalanced(root.rightChild);
    if (leftHeight != -1 && rightHeight != -1) {
      if (abs(leftHeight - rightHeight) <= 1) {
        return max(leftHeight, rightHeight) + 1;
      }
    }
    return -1;
  }

  /**
   * 求二叉树的镜像
   */
  public static TreeNode mirror(TreeNode parent) {
    if (parent == null) {
      return null;
    }
    TreeNode left = mirror(parent.leftChild);
    TreeNode right = mirror(parent.rightChild);
    parent.leftChild = right;
    parent.rightChild = left;
    return parent;
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
  private static TreeNode rightRotate(TreeNode a) {
    TreeNode b = a.leftChild;
    a.leftChild = b.rightChild;
    b.rightChild = a;
    a.height = max(height(a.leftChild), height(a.rightChild)) + 1;
    b.height = max(height(b.leftChild), height(b.rightChild)) + 1;
    return b;
  }


  /**
   * 左旋
   */
  private static TreeNode leftRotate(TreeNode a) {
    TreeNode b = a.rightChild;
    a.rightChild = b.leftChild;
    b.leftChild = a;
    a.height = max(height(a.leftChild), height(a.rightChild)) + 1;
    b.height = max(height(b.leftChild), height(b.rightChild)) + 1;
    return b;
  }


  /**
   * LR型
   * <p>
   *           a
   *          /
   *         b
   *          \
   *           c
   */
  private static TreeNode leftRightRotate(TreeNode a) {
    a.leftChild = leftRotate(a.leftChild);
    return rightRotate(a);
  }

  private static TreeNode rightLeftRotate(TreeNode a) {
    a.rightChild = rightRotate(a.rightChild);
    return leftRotate(a);
  }

  /**
   * height of the {@code node}
   * if node is null, return -1
   */
  private static int height(TreeNode node) {
    return node == null ? -1 : node.height;
  }

  private static TreeNode maximum(TreeNode node) {
    if (node != null) {
      while (node.rightChild != null) {
        node = node.rightChild;
      }
    }
    return node;
  }

  private static TreeNode minimum(TreeNode node) {
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


  @Getter
  @Setter
  static class TreeNode {
    int val;
    int height;
    TreeNode leftChild;
    TreeNode rightChild;

    TreeNode(int n, TreeNode leftChild, TreeNode rightChild) {
      this.val = n;
      this.leftChild = leftChild;
      this.rightChild = rightChild;
    }

    @Override
    public String toString() {
      return "TreeNode{val=" + val + ", height=" + height + '}';
    }
  }
}
