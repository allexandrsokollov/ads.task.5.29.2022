package vsu.cs.sokolov;

public class BinaryTreeHandler {

    @FunctionalInterface
    interface Visitor<T> {
        void visit(T value, int level);
    }
}
