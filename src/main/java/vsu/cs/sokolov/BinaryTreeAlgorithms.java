package vsu.cs.sokolov;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTreeAlgorithms {

    @FunctionalInterface
    public interface Visitor<T> {
        /**
         * "Посещение" значения
         *
         * @param value Значение, которое "посещаем"
         * @param level Уровень дерева/поддерева, на котором находится данное значение
         */
        void visit(T value, int level);
    }


    /**
     * Обход поддерева с вершиной в данном узле в виде итератора в
     * симметричном/поперечном/центрированном/LNR порядке (предполагается, что в
     * процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> inOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            BinaryTree.TreeNode<T> node = treeNode;
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    BinaryTree.TreeNode<T> node = stack.pop();
                    T result = node.getValue();
                    if (node.getRight() != null) {
                        node = node.getRight();
                        while (node != null) {
                            stack.push(node);
                            node = node.getLeft();
                        }
                    }
                    return result;
                }
            };
        };
    }


    /**
     *  Класс для хранения узла дерева вместе с его уровнем, нужен для методов
     *  {@link #byLevelVisit(BinaryTree.TreeNode, Visitor)} и {@link #byLevelValues(BinaryTree.TreeNode)}
     *
     * @param <T>
     */
    private static class QueueItem<T> {
        public BinaryTree.TreeNode<T> node;
        public int level;

        public QueueItem(BinaryTree.TreeNode<T> node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    /**
     * Обход поддерева с вершиной в данном узле "посетителем" по уровням (обход в ширину)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor Посетитель
     */
    public static <T> void byLevelVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        Queue<QueueItem<T>> queue = new LinkedList<>();
        queue.add(new QueueItem<>(treeNode, 0));
        while (!queue.isEmpty()) {
            QueueItem<T> item = queue.poll();
            if (item.node.getLeft() != null) {
                queue.add(new QueueItem<>(item.node.getLeft(), item.level + 1));
            }
            if (item.node.getRight() != null) {
                queue.add(new QueueItem<>(item.node.getRight(), item.level + 1));
            }
            visitor.visit(item.node.getValue(), item.level);
        }
    }

    /**
     * Обход поддерева с вершиной в данном узле в виде итератора по уровням (обход в ширину)
     * (предполагается, что в процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> byLevelValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Queue<QueueItem<T>> queue = new LinkedList<>();
            queue.add(new QueueItem<>(treeNode, 0));

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return queue.size() > 0;
                }

                @Override
                public T next() {
                    QueueItem<T> item = queue.poll();
                    if (item == null) {
                        // такого быть не должно, но на вский случай
                        return null;
                    }
                    if (item.node.getLeft() != null) {
                        queue.add(new QueueItem<>(item.node.getLeft(), item.level + 1));
                    }
                    if (item.node.getRight() != null) {
                        queue.add(new QueueItem<>(item.node.getRight(), item.level + 1));
                    }
                    return item.node.getValue();
                }
            };
        };
    }


    /**
     * Представление дерева в виде строки в скобочной нотации
     *
     * @param treeNode Узел поддерева, которое требуется представить в виже скобочной нотации
     * @return дерево в виде строки
     */
    public static <T> String toBracketStr(BinaryTree.TreeNode<T> treeNode) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 2-мя параметрами)
        class Inner {
            void printTo(BinaryTree.TreeNode<T> node, StringBuilder sb) {
                if (node == null) {
                    return;
                }
                sb.append(node.getValue());
                if (node.getLeft() != null || node.getRight() != null) {
                    sb.append(" (");
                    printTo(node.getLeft(), sb);
                    if (node.getRight() != null) {
                        sb.append(", ");
                        printTo(node.getRight(), sb);
                    }
                    sb.append(")");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().printTo(treeNode, sb);

        return sb.toString();
    }
}
