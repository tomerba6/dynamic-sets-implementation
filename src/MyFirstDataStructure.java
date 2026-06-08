/**
 * A composite data structure designed to maintain elements with rapid access to their chronological
 * insertion order, maximum value, and logarithmic time search/deletion.
 *
 * Architecture:
 * - A primary AVL Tree provides O(log n) search and deletion capabilities.
 * - An underlying unsorted Linked List acts as a chronological queue, tracking the exact order of insertions.
 * - Nodes in the AVL tree maintain a direct reference to their corresponding link in the Linked List,
 * allowing O(1) list deletions without traversal.
 * - A direct pointer tracks the maximum element, yielding O(1) retrieval.
 *
 * @param <T> The type of the satellite data of the elements in the data structure.
 */
public class MyFirstDataStructure<T> {

	/**
	 * The balanced binary search tree used to maintain the elements for O(log n) access.
	 */
	private MyAVLTree<T> tree;

	/**
	 * The chronological list tracking insertion order. The head represents the newest element,
	 * and the tail represents the oldest element.
	 */
	private MyLinkedList<T> list;

	/**
	 * A direct pointer to the element with the maximum key currently in the structure.
	 */
	private Element<T> maxElement;

	/**
	 * Initializes an empty composite data structure.
	 * Time Complexity: O(1) worst-case.
	 *
	 * @param N The maximum number of elements in the data structure at any given time.
	 * (Unused in this dynamic implementation).
	 */
	public MyFirstDataStructure(int N) {
		tree = new MyAVLTree<>();
		list = new MyLinkedList<>();
		maxElement = null;
	}

	/**
	 * Inserts a new element into the data structure. The element is added to the chronological
	 * list, the AVL tree, and updates the maximum pointer if necessary.
	 * Time Complexity: O(log n) worst-case, bounded by the AVL tree insertion.
	 *
	 * @param x The Element to be inserted.
	 */
	public void insert(Element<T> x) {
		ListLink<T> xLink = new ListLink<>(x);
		list.insert(xLink);

		TreeNode<T> xNode = new TreeNode<>(x);
		xNode.setListNode(xLink);
		tree.insert(xNode);

		if (maxElement == null || x.key() >= maxElement.key()) {
			maxElement = x;
		}
	}

	/**
	 * Searches for an element by its key and removes it from both the AVL tree and the
	 * chronological linked list.
	 * * Time Complexity: O(log n) expected. The search and tree deletion operate in O(log n).
	 * The list deletion operates in O(1) using the cached list node reference. If the maximum
	 * element is removed, recalculating the new maximum takes O(log n) via tree traversal.
	 *
	 * @param k The integer key of the element to find and remove.
	 */
	public void findAndRemove(int k) {
		TreeNode<T> target = tree.search(k);
		if (target != null) {
			boolean wasMax = k == maxElement.key();

			list.delete(target.getListNode());

			ListLink<T> successorLink = null;
			if (target.getLeft() != null && target.getRight() != null) {
				TreeNode<T> successor = target.getRight();
				while (successor.getLeft() != null) {
					successor = successor.getLeft();
				}
				successorLink = successor.getListNode();
			}

			tree.delete(target);

			if (successorLink != null) {
				target.setListNode(successorLink);
			}


			if (wasMax) {
				if (tree.root() == null){
					maxElement = null;
				}
				else {
					TreeNode<T> current = tree.root();
					while (current.getRight() != null) {
						current = current.getRight();
					}
					maxElement = current;
				}
			}
		}
	}

	/**
	 * Retrieves the element with the maximum key currently in the data structure.
	 * Time Complexity: O(1) worst-case, utilizing the direct pointer.
	 *
	 * @return The Element with the largest key, or null if the structure is empty.
	 */
	public Element<T> maximum() {
		return maxElement;
	}

	/**
	 * Retrieves the element that was inserted first (the oldest element) chronologically.
	 * Time Complexity: O(1) worst-case, by returning the tail of the underlying list.
	 *
	 * @return The chronologically oldest Element, or null if the structure is empty.
	 */
	public Element<T> first() {
		return list.tail();
	}

	/**
	 * Retrieves the element that was inserted last (the newest element) chronologically.
	 * Time Complexity: O(1) worst-case, by returning the head of the underlying list.
	 *
	 * @return The chronologically newest Element, or null if the structure is empty.
	 */
	public Element<T> last() {
		return list.head();
	}

}
