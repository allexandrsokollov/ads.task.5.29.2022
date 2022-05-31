package vsu.cs.sokolov;

import vsu.cs.sokolov.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
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
                findHeightOfNode(tree.root, new Height());
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

    static class Height {
        int height;
    }
/*
1 (2 (3 (4), 5 (1 (2 (3 (4), 5 (, 9 (, 2 (, 3)))), 3 (, 4 (5, 6))), 9 (, 2 (, 3)))), 3 (, 4 (5, 6)))
1 (2 (, 5 (1 (2 (, 5 (, 9 (, 2 (, 3)))), 3 (, 4 (5, 6))), 9 (, 2 (, 3)))), 3 (, 4 (5, 6)))
1 (2, 3 (4 (6, 7), 5))
*/
    private void findHeightOfNode(SimpleBinaryTree<Integer>.SimpleTreeNode node, Height height) {
        if (node != null) {
            node.value = 0;

            if (node.right == null && node.left == null) {
                height.height = 0;
            } else {

                if (node.left != null) {
                    findHeightOfNode(node.left, height);
                    height.height++;

                    if (height.height >= node.value) {
                        node.value = height.height;
                    }
                }

                if (node.right != null) {
                    findHeightOfNode(node.right, height);
                    height.height++;

                    if (height.height >= node.value) {
                        node.value = height.height;
                    } else {
                        height.height = node.value;
                    }
                }
            }
       }
    }
}
