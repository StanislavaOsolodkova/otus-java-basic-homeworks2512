package ru.otus.java.basic.homeworks.hw18;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> implements SearchTree<T> {
    private TreeNode<T> root;

    public BinarySearchTree(List<T> sortedList) {
        if (sortedList == null || sortedList.isEmpty()) {
            root = null;
        } else {
            root = buildTree(sortedList, 0, sortedList.size() - 1);
        }
    }

    private TreeNode<T> buildTree(List<T> sortedList, int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        TreeNode<T> node = new TreeNode<>(sortedList.get(mid));
        node.left = buildTree(sortedList, start, mid - 1);
        node.right = buildTree(sortedList, mid + 1, end);
        return node;
    }

    @Override
    public T find(T element) {
        return findRecursive(root, element);
    }

    private T findRecursive(TreeNode<T> node, T element) {
        if (node == null) return null;
        int cmp = element.compareTo(node.value);
        if (cmp == 0) return node.value;
        return cmp < 0 ? findRecursive(node.left, element) : findRecursive(node.right, element);
    }

    @Override
    public List<T> getSortedList() {
        List<T> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    private void inorderTraversal(TreeNode<T> node, List<T> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.value);
            inorderTraversal(node.right, result);
        }
    }
}
