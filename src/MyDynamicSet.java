/**
 * A Dynamic Set implementation backed by a sorted doubly-linked list.
 * This structure maintains elements in a sorted order to optimize minimum,
 * maximum, and relative positioning queries to O(1) time complexity, while
 * insertions and searches operate in O(n) time.
 *
 * @param <T> The type of the satellite data of the elements in the dynamic set.
 */
public class MyDynamicSet<T> {
	/**
	 * The underlying sorted linked list used to store the dynamic set elements.
	 */
	private MySortedLinkedList<T>  list;


	/**
	 * Initializes an empty dynamic set.
	 * * Note: Because this implementation is backed by a linked list rather than
	 * a statically sized array, the maximum capacity parameter is unused and
	 * the set can grow dynamically.
	 *
	 * @param N The maximum number of elements in the dynamic set at any given time.
	 * (Unused in this dynamic implementation).
	 */
	public MyDynamicSet(int N) {
		this.list = new MySortedLinkedList<>();
	}

	/**
	 * Searches for an element with the specified key in the dynamic set.
	 * * Time Complexity: O(n) expected, as it may require traversing the list.
	 *
	 * @param k The integer key to search for.
	 * @return The Element containing the matching key, or null if no such element exists.
	 */
	public Element<T> search(int k) {
		return list.search(k);
	}

	/**
	 * Inserts a new element into the dynamic set while maintaining sorted order.
	 * * Time Complexity: O(n) worst-case, to find the correct sorted insertion point.
	 *
	 * @param x The generic Element to be inserted into the set.
	 */
	public void insert(Element<T> x) {
		ListLink<T> xLink = new ListLink<>(x);
		list.insert(xLink);
	}

	/**
	 * Removes a specific element from the dynamic set.
	 * * Time Complexity: O(1) expected, since the operation receives a direct
	 * pointer to the node being deleted and bypassing it requires constant time.
	 *
	 * @param x The Element to be removed from the set.
	 */
	public void delete(Element<T> x) {
		ListLink<T> xLink = (ListLink<T>) x;
		list.delete(xLink);
	}

	/**
	 * Retrieves the element with the smallest key in the dynamic set.
	 * * Time Complexity: O(1) worst-case, by returning the head of the sorted list.
	 *
	 * @return The Element with the minimum key, or null if the set is empty.
	 */
	public Element<T> minimum() {
		return list.head();
	}

	/**
	 * Retrieves the element with the largest key in the dynamic set.
	 * * Time Complexity: O(1) worst-case, by returning the tail of the sorted list.
	 *
	 * @return The Element with the maximum key, or null if the set is empty.
	 */
	public Element<T> maximum() {
		return list.tail();
	}

	/**
	 * Finds the element with the smallest key that is strictly greater than the key of the given element.
	 * * Time Complexity: O(1) worst-case, by accessing the next pointer in the linked list.
	 *
	 * @param x The Element for which to find the successor.
	 * @return The successor Element, or null if the given element is the maximum.
	 */
	public Element<T> successor(Element<T> x) {
		ListLink<T> xLink = (ListLink<T>) x;
		return xLink.getNext();
	}

	/**
	 * Finds the element with the largest key that is strictly smaller than the key of the given element.
	 * * Time Complexity: O(1) worst-case, by accessing the previous pointer in the linked list.
	 *
	 * @param x The Element for which to find the predecessor.
	 * @return The predecessor Element, or null if the given element is the minimum.
	 */
	public Element<T> predecessor(Element<T> x) {
		ListLink<T> xLink = (ListLink<T>) x;
		return xLink.getPrev();
	}
}
