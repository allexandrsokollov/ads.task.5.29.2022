package vsu.cs.sokolov;

import vsu.cs.sokolov.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
                findNodesHeight(null, tree.root);
                paintPanel.repaint();
                panelPaintArea.updateUI();
            }
        });
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



}
