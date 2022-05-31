package vsu.cs.sokolov;

import vsu.cs.sokolov.util.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {
    private JTextField textFieldBracketNotationTree;
    private JButton buttonMakeTree;
    private JPanel panelPaintArea;
    private JButton buttonFindHeight;
    private JPanel panelMain;
    private final JPanel paintPanel;

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
                findHeightOfNode(tree.root, new Visitor());
                updateUI();
            }
        });
    }

    private void updateUI() {
        paintPanel.repaint();
        panelPaintArea.repaint();
        panelPaintArea.updateUI();
        paintPanel.updateUI();
    }

    static class Visitor {
        int height;
    }
/*
Some trees for test:
1 (2 (3 (4), 5 (1 (2 (3 (4), 5 (, 9 (, 2 (, 3)))), 3 (, 4 (5, 6))), 9 (, 2 (, 3)))), 3 (, 4 (5, 6)))
1 (2 (, 5 (1 (2 (, 5 (, 9 (, 2 (, 3)))), 3 (, 4 (5, 6))), 9 (, 2 (, 3)))), 3 (, 4 (5, 6)))
1 (2, 3 (4 (6, 7), 5))
*/
    private void findHeightOfNode(SimpleBinaryTree<Integer>.SimpleTreeNode node, Visitor visitor) {
        if (node != null) {
            node.value = 0;

            if (node.right == null && node.left == null) {
                visitor.height = 0;
            } else {

                if (node.left != null) {
                    findHeightOfNode(node.left, visitor);
                    visitor.height++;

                    if (visitor.height >= node.value) {
                        node.value = visitor.height;
                    }
                }

                if (node.right != null) {
                    findHeightOfNode(node.right, visitor);
                    visitor.height++;

                    if (visitor.height >= node.value) {
                        node.value = visitor.height;
                    } else {
                        visitor.height = node.value;
                    }
                }
            }
       }
    }
}
