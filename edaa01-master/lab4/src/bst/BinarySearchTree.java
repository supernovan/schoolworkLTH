package bst;

public class BinarySearchTree<E extends Comparable<? super E>> {
	BinaryNode<E> root;
    int size;
    
	/**
	 * Constructs an empty binary searchtree.
	 */
	public BinarySearchTree() {
		size = 0;
	}

	/**
	 * Inserts the specified element in the tree if no duplicate exists.
	 * @param x element to be inserted
	 * @return true if the the element was inserted
	 */
	public boolean add(E x) {
		if (root == null) {
			root = new BinaryNode<E>(x);
			size++;
			return true;
		}
		return compareObj(root, x);
	}
	private boolean compareObj(BinaryNode<E> node, E x) {
		if(node.element.compareTo(x) < 0) {
			if(node.left == null) {
				node.left = new BinaryNode<E>(x);
				size++; 
				return true;
			}
			else {
				return compareObj(node.left, x);
			}
		}
		else if(node.element.compareTo(x) > 0) {
			if(node.right == null) {
				node.right = new BinaryNode<E>(x);
				size++;
				return true;
			}
			else {
				return compareObj(node.right, x);
			}
		}
		return false;
		
	}
	
	/**
	 * Computes the height of tree.
	 * @return the height of the tree
	 */
	public int height() {
			
		return heightOfTree(root);
	}
	
	public int heightOfTree(BinaryNode<E> node) {
		if(node != null) {
			return 1 + Math.max(heightOfTree(node.left), heightOfTree(node.right));
		}
		return 0;
	}
	
	/**
	 * Returns the number of elements in this tree.
	 * @return the number of elements in this tree
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Print tree contents in inorder.
	 */
	public void printTree() {
		printTree(root);
		
	}
	public void printTree(BinaryNode<E> node) {
		if(node != null) {
			printTree(node.left);
			System.out.println(node.element.toString() + " ");
			printTree(node.right);
		}
	}

	/** 
	 * Builds a complete tree from the elements in the tree.
	 */
	public void rebuild() {
		if(root == null) {
			System.out.println("The tree is empty");
		}
		else {
			E[] array = (E[]) new Object[size];
			root = buildTree(array, 0, toArray(root, array, 0)-1);
		}
	}
	
	/*
	 * Adds all elements from the tree rooted at n in inorder to the array a
	 * starting at a[index].
	 * Returns the index of the last inserted element + 1 (the first empty
	 * position in a).
	 */
	private int toArray(BinaryNode<E> node, E[] array, int index) {
		if(node != null) {
			toArray(node.left, array, index);
			array[index] = node.element;
			index ++;
			toArray(node.right, array, index);
		}
		return index +1;
	}
	
	/*
	 * Builds a complete tree from the elements a[first]..a[last].
	 * Elements in the array a are assumed to be in ascending order.
	 * Returns the root of tree.
	 */
	private BinaryNode<E> buildTree(E[] array, int first, int last) {
		if(last < first) {
			return null;
		}
		int mid = (first + last)/2;
		BinaryNode<E> midNode = new BinaryNode<E>(array[mid]);
		midNode.left = buildTree(array, first, mid-1);
		midNode.right = buildTree(array, mid+1, last);
		
		return midNode;
		
	}
	


	static class BinaryNode<E> {
		E element;
		BinaryNode<E> left;
		BinaryNode<E> right;

		private BinaryNode(E element) {
			this.element = element;
		}	
	}
	public static void main(String [] args) {
		BinarySearchTree tree = new BinarySearchTree();
		BSTVisualizer bt = new BSTVisualizer("window", 600, 600);
		tree.add(5);
		tree.add(3);
		tree.add(3);
		tree.add(4);
		tree.add(27);
		tree.add(17);
		//tree.height();
		// tree.size();
		tree.printTree();
		bt.drawTree(tree);
		
	}
}
