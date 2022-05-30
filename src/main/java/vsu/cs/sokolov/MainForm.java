package vsu.cs.sokolov;

import vsu.cs.sokolov.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainForm extends JFrame {
    private JTextField textFieldBracketNotationTree;
    private JButton buttonMakeTree;
    private JPanel panelPaintArea;
    private JButton buttonFindHeight;
    private JPanel panelMain;
    private JPanel paintPanel = null;

    SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>();

    public MainForm() {
        this.setTitle("task 5.29.2022 by Alexandr Sokolov");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);


        paintPanel = new JPanel() {

            @Override
            public void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                Dimension paintSize = BinaryTreePainter.paint(tree, gr);
                if (!paintSize.equals(this.getPreferredSize())) {
                    SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
                }
            }
        };

        JScrollPane paintJScrollPane = new JScrollPane(paintPanel);
        panelPaintArea.add(paintJScrollPane);


        buttonMakeTree.addActionListener(e -> {
            try {
                SimpleBinaryTree<Integer> binaryTree = new SimpleBinaryTree<>(Integer::parseInt);
                binaryTree.fromBracketNotation(textFieldBracketNotationTree.getText());
                this.tree = binaryTree;
                paintPanel.repaint();
                panelPaintArea.updateUI();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }

        });

        buttonFindHeight.addActionListener(e -> {
            if (tree.isEmpty()) {
                try {
                    throw new Exception("Empty tree ERROR");
                } catch (Exception ex) {
                    SwingUtils.showErrorMessageBox(ex);
                }
            } else {
                postOrder(tree.root, new Visitor(0));
                updateUI();
            }
        });
    }

    private void updateUI() {
        paintPanel.repaint();
        panelPaintArea.updateUI();
    }

    private void findNodesHeight(SimpleBinaryTree<Integer>.SimpleTreeNode prevNode,
                                 SimpleBinaryTree<Integer>.SimpleTreeNode node) {
        node.setValue(0);

        if (prevNode != null && prevNode.value.equals(node.getValue())) {
            prevNode.setValue(prevNode.value + 1);
        }

        if (node.left != null) {
            findNodesHeight(node, node.left);
        }

        if (node.right != null) {
            findNodesHeight(node, node.right);
        }
    }

    static class Visitor {
        int level;
        int branchMax;
        int val;
        LinkedList<SimpleBinaryTree<Integer>.SimpleTreeNode> nodes;

        public Visitor(int level) {
            this.level = level;
            branchMax = 0;
            nodes = new LinkedList<>();
            val = 0;
        }
        void incrementLevels() {
            for (SimpleBinaryTree<Integer>.SimpleTreeNode node : nodes) {
                if (node.value < val) {
                    node.value++;
                }
            }
        }
    }


    private void postOrder(SimpleBinaryTree<Integer>.SimpleTreeNode node, Visitor visitor) {
        if (node != null) {
           node.setValue(0);

            visitor.nodes.add(node);
            visitor.val++;
            visitor.incrementLevels();

            if (node.left != null) {
                postOrder(node.left, visitor);
            }


            if (node.right != null) {
                postOrder(node.right, visitor);
            }

            if (!visitor.nodes.isEmpty()) {
                visitor.nodes.removeLast();
                visitor.val--;
            }

        }
    }



}
