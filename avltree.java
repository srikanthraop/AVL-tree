import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class avltree {

    public class Node {
        //structure of an AVL node
        int nodeVal, balanceFactor;
        Node left, right;

        public Node(int nodeVal) {
            this.nodeVal = nodeVal;
        }

        @Override
        public String toString() {
            return String.valueOf(this.nodeVal);
        }
    }

    // root node
    Node root;

    public void initialize() {
    }

    public void insert(int val) {
        // calls insert function
        root = insert(root, val);
        System.out.println(val + " inserted");
    }

    public void delete(int val) {
        // calls delete function
        root = delete(root, val);
        System.out.println(val + " deleted");
    }

    public Node search(int val) {
        // calls search a single node fuction
        Node res = search(root, val);
        System.out.println(res);
        return res;
    }

    public ArrayList<Integer> search(int low, int high) {
        // calls search range function
        ArrayList<Integer> values = search(root, low, high, new ArrayList<>());
        System.out.println(values);
        return values;
    }

    private Node search(Node curNode, int val) {
        // recursive way to search BST
        if (curNode == null || curNode.nodeVal == val)
            return curNode;

        if (curNode.nodeVal > val) {
            return search(curNode.left, val);
        } else {
            return search(curNode.right, val);
        }
    }

    private ArrayList<Integer> search(Node curNode, int low, int high, ArrayList<Integer> values) {
        // Traverse the tree, and add into ArrayList if the value of a node is in the
        // range low --- high
        if (curNode != null) {

            if (low < curNode.nodeVal) {
                search(curNode.left, low, high, values);
            }
            //add to array list if in the rangeß
            if (low <= curNode.nodeVal && curNode.nodeVal <= high) {
                values.add(curNode.nodeVal);
            }
            if (curNode.nodeVal < high) {
                search(curNode.right, low, high, values);
            }
        }
        return values;
    }

    // Inset similar to Binary search tree
    private Node insert(Node curNode, int val) {
        // reached the spot where the node has to be inserted
        if (curNode == null)
            return new Node(val);
        // if the node is alrady present in the tree, throw an exception
        if (curNode.nodeVal == val)
            throw new RuntimeException("[DUPLICATE VALUE] This value/key is already in the AVL tree");
        // if the value in the current node is greater than the value to be inserted,
        // move to the left subtree
        else if (curNode.nodeVal > val)
            curNode.left = insert(curNode.left, val);
        // if the value in the current node is smaller than the value to be inserted,
        // move to the right subtree
        else if (curNode.nodeVal < val)
            curNode.right = insert(curNode.right, val);

        // check for any imbalances in height caused due to insertion
        return balanceTree(curNode);
    }

    private Node balanceTree(Node curNode) {
        changeHeight(curNode);
        int balanceFactor = checkBalance(curNode);
        // if the left subtree is bigger
        if (balanceFactor >= 2) {
            if (getHeight(curNode.left.left) > getHeight(curNode.left.right)) {
                // LL rotation
                curNode = LLrotation(curNode);
            } else {
                // LR rotation
                curNode.left = RRrotation(curNode.left);
                curNode = LLrotation(curNode);
            }
        } else if (balanceFactor <= -2) { // if the right subtree is bigger
            if (getHeight(curNode.right.right) > getHeight(curNode.right.left)) {
                // RR rotation
                curNode = RRrotation(curNode);
            } else {
                // RL rotation
                curNode.right = LLrotation(curNode.right);
                curNode = RRrotation(curNode);
            }
        }
        return curNode;
    }

    // Recursively look for the node to delete
    private Node delete(Node curNode, int val) {
        if (curNode == null)
            return null;
        if (curNode.nodeVal > val)
            curNode.left = delete(curNode.left, val);
        else if (curNode.nodeVal < val)
            curNode.right = delete(curNode.right, val);
        else {  //when the node is found
            // if the node is one degree one or zero, change the pointer
            if (curNode.left == null || curNode.right == null) {
                if (curNode.left == null) {
                    curNode = curNode.right;
                } else {
                    curNode = curNode.left;
                }
            } else {
                // if the node to be deleted has 2 childern, replace the value of the node to be
                // deleted with the lastest values in the left subtree
                // Recursively find the value in the tree and delete it
                Node smallestLeftSubtree = smallestLeftSubtree(curNode.right);
                curNode.nodeVal = smallestLeftSubtree.nodeVal;
                curNode.right = delete(smallestLeftSubtree, smallestLeftSubtree.nodeVal);
            }
        }
        //rebalance the tree as there may be imbalances up ahead
        if (curNode != null) {
            curNode = balanceTree(curNode);
        }
        return curNode;
    }

    // LL rotation
    private Node LLrotation(Node A) {
        // Notations used in class
        Node B = A.left;
        Node C = B.right;
        B.right = A;
        A.left = C;
        changeHeight(A);
        changeHeight(B);
        return B;
    }

    // RR rotation
    private Node RRrotation(Node A) {
        // Notations used in class
        Node B = A.right;
        Node C = B.left;
        B.left = A;
        A.right = C;
        changeHeight(A);
        changeHeight(B);
        return B;
    }

    private int changeHeight(Node curNode) {
        // Recauculate and change the height of a node
        int height = 0;
        int leftHeight = getHeight(curNode.left);
        int rightHeight = getHeight(curNode.right);
        height = 1 + Math.max(leftHeight, rightHeight);
        return height;
    }

    private int checkBalance(Node curNode) {
        // check the balancing factor of a node
        // it changes after insertion and deletion
        if (curNode == null) {
            return 0;
        } else {
            int balanceFactor = getHeight(curNode.left) - getHeight(curNode.right);
            return balanceFactor;
        }
    }

    private int getHeight(Node curNode) {
        // get the height of a node
        if (curNode == null) {
            return -1;
        } else {
            return curNode.balanceFactor;
        }
    }

    private Node smallestLeftSubtree(Node curNode) {
        // find the smallest element in the tree (right subtree)
        if (curNode.left == null)
            return curNode;
        return smallestLeftSubtree(curNode.left);
    }

    public static void main(String[] args) throws IOException {
        avltree avl = new avltree();

        // FILE INPUT REFRRED FROM HERE:
        // https://www.w3schools.com/java/java_files_read.asp
        ArrayList<String> output = new ArrayList<>();
        try {
            File myObj = new File(args[0]);
            try (Scanner reader = new Scanner(myObj)) {
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    // System.out.println(data);
                    if (data.startsWith("Initialize")) {
                        avl.initialize();
                    } else if (data.startsWith("Insert")) {
                        String values = data.substring(7);
                        values = values.replaceAll("([\\[\\](){}])", "");
                        int parameter = Integer.valueOf(values);
                        avl.insert(parameter);
                    } else if (data.startsWith("Delete")) {
                        String values = data.substring(7);
                        values = values.replaceAll("([\\[\\](){}])", "");
                        int parameter = Integer.valueOf(values);
                        avl.delete(parameter);
                    } else if (data.startsWith("Search")) {
                        String values = data.substring(7);
                        values = values.replaceAll("([\\[\\](){}])", "");
                        String[] parameters = values.split(",");
                        if (parameters.length == 1) {
                            int param = Integer.valueOf(parameters[0]);
                            Node resultNode = avl.search(param);
                            if (resultNode == null) {
                                output.add("NULL");
                            } else {
                                output.add(String.valueOf(resultNode.nodeVal));
                            }
                        } else if (parameters.length == 2) {
                            int param1 = Integer.valueOf(parameters[0]);
                            int param2 = Integer.valueOf(parameters[1]);
                            ArrayList<Integer> resultantRange = avl.search(param1, param2);
                            if (resultantRange == null || resultantRange.size() == 0) {
                                output.add("NULL");
                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (Integer i : resultantRange) {
                                    sb.append(String.valueOf(i) + ", ");
                                }
                                sb = sb.delete(sb.length() - 2, sb.length());
                                output.add(sb.toString());
                            }
                        } else {
                            throw new RuntimeException("[TOO MANY PARAMETERS] You can only enter 1 or 2 parameters");
                        }
                    } else {
                        throw new RuntimeException("INVALID INPUT");
                    }
                }
                // FILE OUTPUT REFFERED FROM HERE:
                // https://stackoverflow.com/questions/6548157/how-to-write-an-arraylist-of-strings-into-a-text-file
                FileWriter writer = new FileWriter("output.txt");
                for (String str : output) {
                    writer.write(str + System.lineSeparator());
                }
                writer.close();
                reader.close();
            } catch (FileNotFoundException e) {
                throw e;
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}