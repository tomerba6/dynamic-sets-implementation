public class Tester
{
    public static void main(String[] args) {
        System.out.println("Starting Test Suite...\n");

        testTask2_1_ArrayReverse();
        testTask2_2_LinkedListReverse();
        testTask2_3_AVLDepthOfMin();
        testTask3_1_DynamicSet();
        testTask4_1_FirstDataStructure();
        testTask4_2_SecondDataStructure();

        System.out.println("\nAll tests completed! (Check console for any FAIL messages)");
    }

    // ==========================================
    // TASK 2.1: MyArray Reverse
    // ==========================================
    public static void testTask2_1_ArrayReverse() {
        System.out.println("--- Testing Task 2.1: MyArray Reverse ---");
        MyArray<String> arr = new MyArray<>(10);

        // Edge Case 1: Empty Array
        arr.reverse();
        check(arr.size() == 0, "Empty array reverse");

        // Edge Case 2: Array of size 1
        ArrayElement<String> e1 = new ArrayElement<>(1, "A");
        arr.insert(e1);
        arr.reverse();
        check(arr.get(0).key() == 1, "Size 1 array reverse");

        // Edge Case 3: Even length array
        ArrayElement<String> e2 = new ArrayElement<>(2, "B");
        arr.insert(e2);
        arr.reverse(); // Now should be B, A
        check(arr.get(0).key() == 2 && arr.get(1).key() == 1, "Even length array reverse");

        // Edge Case 4: Odd length array
        ArrayElement<String> e3 = new ArrayElement<>(3, "C");
        arr.insert(e3); // Now B, A, C
        arr.reverse();  // Should be C, A, B
        check(arr.get(0).key() == 3 && arr.get(1).key() == 1 && arr.get(2).key() == 2, "Odd length array reverse");
    }

    // ==========================================
    // TASK 2.2: MyLinkedList Reverse
    // ==========================================
    public static void testTask2_2_LinkedListReverse() {
        System.out.println("\n--- Testing Task 2.2: MyLinkedList Reverse ---");
        MyLinkedList<String> list = new MyLinkedList<>();

        // Edge Case 1: Empty List
        list.reverse();
        check(list.head() == null, "Empty list reverse");

        // Edge Case 2: List of size 1
        list.insert(new ListLink<>(1, "A"));
        list.reverse();
        check(list.head().key() == 1 && list.tail().key() == 1, "Size 1 list reverse");

        // Edge Case 3: Even/Odd length list
        list.insert(new ListLink<>(2, "B")); // List: B -> A
        list.insert(new ListLink<>(3, "C")); // List: C -> B -> A
        list.reverse(); // Should be: A -> B -> C
        check(list.head().key() == 1 && list.tail().key() == 3, "Odd length list reverse head/tail pointers");
    }

    // ==========================================
    // TASK 2.3: MyAVLTree DepthOfMin
    // ==========================================
    public static void testTask2_3_AVLDepthOfMin() {
        System.out.println("\n--- Testing Task 2.3: MyAVLTree Depth of Min ---");
        MyAVLTree<String> tree = new MyAVLTree<>();

        // Edge Case 1: Empty Tree
        check(tree.depthOfMin() == -1, "Empty tree depthOfMin should be -1");

        // Edge Case 2: Only root
        tree.insert(new TreeNode<>(10, "Root"));
        check(tree.depthOfMin() == 0, "Root only depthOfMin should be 0");

        // Edge Case 3: Left-leaning depth
        tree.insert(new TreeNode<>(5, "Min"));
        tree.insert(new TreeNode<>(15, "Max"));
        check(tree.depthOfMin() == 1, "Depth of min should be 1");
    }

    // ==========================================
    // TASK 3.1: MyDynamicSet
    // ==========================================
    public static void testTask3_1_DynamicSet() {
        System.out.println("\n--- Testing Task 3.1: MyDynamicSet ---");
        MyDynamicSet<String> set = new MyDynamicSet<>(10);

        Element<String> e5 = new Element<>(5, "Five");
        Element<String> e10 = new Element<>(10, "Ten");
        Element<String> e15 = new Element<>(15, "Fifteen");

        set.insert(e10);
        set.insert(e5);
        set.insert(e15);

        check(set.minimum().key() == 5, "Minimum is correct");
        check(set.maximum().key() == 15, "Maximum is correct");

        Element<String> searched = set.search(10);
        check(searched != null && searched.key() == 10, "Search finds element");

        set.delete(searched); // Delete 10
        check(set.search(10) == null, "Delete successfully removes element");
        check(set.successor(set.minimum()).key() == 15, "Successor bypasses deleted element");
    }

    // ==========================================
    // TASK 4.1: MyFirstDataStructure
    // ==========================================
    public static void testTask4_1_FirstDataStructure() {
        System.out.println("\n--- Testing Task 4.1: MyFirstDataStructure ---");
        MyFirstDataStructure<String> ds1 = new MyFirstDataStructure<>(10);

        ds1.insert(new Element<>(10, "Data1"));
        ds1.insert(new Element<>(5, "Data2"));
        ds1.insert(new Element<>(15, "Data3")); // Insert order: 10, 5, 15. Last=15, First=10

        check(ds1.maximum().key() == 15, "Maximum is correct");
        check(ds1.first().key() == 10, "First (oldest) is correct");
        check(ds1.last().key() == 15, "Last (newest) is correct");

        // The Ultimate Test: The Two-Child Deletion Trap!
        ds1.findAndRemove(10); // 10 is the root, has 2 children (5 and 15)

        // If the pointers were corrupted, first() will return a dead node, or maximum() will break
        check(ds1.maximum().key() == 15, "Maximum survived the two-child trap");

        // CORRECTION: Instead of search(10), we check if the 'first' pointer updated correctly!
        // Since 10 was deleted, the next oldest element (5) should now be the first.
        check(ds1.first().key() == 5, "10 was successfully deleted, making 5 the new 'first'");

        ds1.findAndRemove(15); // Delete the max
        check(ds1.maximum().key() == 5, "Maximum correctly updated after max deletion");
    }

    // ==========================================
    // TASK 4.2: MySecondDataStructure
    // ==========================================
    public static void testTask4_2_SecondDataStructure() {
        System.out.println("\n--- Testing Task 4.2: MySecondDataStructure ---");
        MySecondDataStructure ds2 = new MySecondDataStructure(10);

        // Uses exact Product constructor: Product(id, quality, price, name)
        Product p1 = new Product(1, 3, 100, "P1"); // Quality 3, Price 100
        Product p2 = new Product(2, 5, 200, "P2"); // Quality 5, Price 200
        Product p3 = new Product(3, 3, 50,  "P3"); // Quality 3, Price 50

        ds2.insert(p1);
        ds2.insert(p2);
        ds2.insert(p3);

        // Avg Quality: (3 + 5 + 3) / 3 = 3.666...
        check(Math.abs(ds2.avgQuality() - 3.66) < 0.1, "Average quality calculation");

        // Median Quality: 3 items. Qualities are 3, 3, 5. Lower middle is 3.
        check(ds2.medianQuality() == 3, "Median quality calculation (odd)");

        // Most Expensive is P2 (200)
        check(ds2.mostExpensive().id() == 2, "Most expensive initially correct");

        // The Offset Trap Test
        ds2.raisePrice(200, 3); // Raise quality 3 by 200. P1 is now 300. P3 is now 250.
        check(ds2.mostExpensive().id() == 1, "Most expensive updated after raisePrice");

        // Insert new product AFTER raise. Real price is 150. Should NOT be affected by the +200.
        Product p4 = new Product(4, 3, 150, "P4");
        ds2.insert(p4);

        // If the offset failed, P4 would incorrectly evaluate to 150 + 200 = 350 and become max.
        // If the offset succeeded, P1 (300) is still max.
        check(ds2.mostExpensive().id() == 1, "New insertions correctly offset retroactive price raises");

        // Test Find and Remove Most Expensive
        ds2.findAndRemove(1); // Remove P1. Max should fall to P3 (250).
        check(ds2.mostExpensive().id() == 3, "FindNewMax updates correctly after deleting most expensive");
    }

    // Simple helper to print Pass/Fail
    private static void check(boolean condition, String testName) {
        if (condition) {
            System.out.println("[PASS] " + testName);
        } else {
            System.out.println("[FAIL] " + testName);
        }
    }
}
