/**
 * A highly optimized data structure designed to manage an inventory of products.
 * * Architecture & Optimizations:
 * - Delta Tracking (Lazy Updates): Price raises are tracked in a fixed-size `raisedPrices` array
 * rather than updating every product. This allows mass price raises to occur in $O(1)$ time.
 * New products adjust their base price downward upon insertion to normalize against the delta.
 * - Histogram Bucketing: Because the quality domain is strictly bounded (0-5), a frequency
 * histogram (`qualityHist`) tracks the distribution, allowing $O(1)$ Median and Average calculations.
 * - Max Buckets: The most expensive product for each quality tier is cached in a size-6 array
 * (`maxPerQuality`), allowing the overall most expensive product to be found in strict $O(1)$ time.
 * - Underlying Storage: An unsorted Linked List maintains the products, supporting expected $O(n)$ search.
 */
public class MySecondDataStructure {
	/** The strict upper bound of distinct product quality levels (0 through 5). */
	private final int QUALITY_OPTIONS = 6;

	/** Delta array tracking cumulative price raises applied to each quality tier. */
	private int[] raisedPrices;

	/** Frequency histogram tracking how many products exist at each quality tier. */
	private int[] qualityHist;

	/** Cache of the single most expensive product within each quality tier. */
	private Product[] maxPerQuality;

	/** Running sum of all product qualities in the system for O(1) average calculation. */
	private int sumQ;

	/** The underlying list storing all products in the data structure. */
	private MyLinkedList<Product> products;

	/** Running total of active products in the system. */
	private int productsCount;


	/**
	 * Initializes the product inventory system and its auxiliary tracking arrays.
	 * Time Complexity: O(1) worst-case, since the auxiliary arrays are of fixed constant size (6).
	 *
	 * @param N The maximum number of elements in the data structure at any given time.
	 * (Unused in this dynamic implementation).
	 */
	public MySecondDataStructure(int N) {
		qualityHist = new int[QUALITY_OPTIONS];
		raisedPrices = new int[QUALITY_OPTIONS];
		maxPerQuality = new Product[QUALITY_OPTIONS];

		products = new MyLinkedList<Product>();
		productsCount = 0;

		sumQ = 0;
	}

	/**
	 * Inserts a new product into the system.
	 * Applies a normalization hack: subtracts the current tier's accumulated raises from the
	 * product's base price so that future delta-additions yield the correct true price.
	 * Time Complexity: O(1) worst-case.
	 *
	 * @param product The Product to be inserted.
	 */
	public void insert(Product product) {
		ListLink<Product> productLink = new ListLink<>(product.id(), product);

		int quality = product.quality();
		product.setPrice(product.price() - raisedPrices[quality]);

		sumQ += quality;
		qualityHist[quality]++;

		if (maxPerQuality[quality] == null || (product.price() + raisedPrices[quality]) >= (maxPerQuality[quality].price() + raisedPrices[quality])) {
			maxPerQuality[quality] = product;
		}

		products.insert(productLink);
		productsCount += 1;
	}

	/**
	 * Searches for a product by its ID and removes it. If the removed product was the
	 * maximum for its tier, a full scan is triggered to find the new maximum.
	 * Time Complexity: O(n) worst-case. Searching the linked list takes O(n). Deletion
	 * takes O(1). Rebuilding the max cache (if necessary) takes O(n).
	 *
	 * @param id The unique integer ID of the product to remove.
	 */
	public void findAndRemove(int id) {
		ListLink<Product> target = products.search(id);
		if (target != null) {
			int quality = target.satelliteData().quality();
			boolean wasMax = target.satelliteData().equals(maxPerQuality[quality]);
			products.delete(target);
			productsCount -= 1;

			sumQ -= quality;
			qualityHist[quality] -= 1;

			if (wasMax) {
				if (qualityHist[quality] == 0) {
					maxPerQuality[quality] = null;
				}
				else {
					findNewMax(quality);
				}
			}
		}
	}

	/**
	 * Helper method to recalculate the most expensive product for a specific quality tier.
	 * Time Complexity: O(n) worst-case, as it requires scanning the entire product list.
	 *
	 * @param quality The quality tier (0-5) that requires a new max evaluation.
	 */
	private void findNewMax(int quality) {
		maxPerQuality[quality] = null;

		ListLink<Product> current = products.head();
		while (current != null) {
			Product product = current.satelliteData();

			if (product.quality() == quality) {
				if (maxPerQuality[quality] == null || (product.price() + raisedPrices[quality] >= maxPerQuality[quality].price() + raisedPrices[quality])){
					maxPerQuality[quality] = product;
				}
			}
			current = current.getNext();
		}
	}

	/**
	 * Calculates the median quality among all active products using the frequency histogram.
	 * Time Complexity: O(1) worst-case, as the loop is strictly bounded by 6 (constant time).
	 *
	 * @return The median quality (0-5), or -1 if the structure is empty.
	 */
	public int medianQuality() {
		if (productsCount != 0) {
			int target = (productsCount + 1) / 2;
			int sum = 0;
			for (int i = 0; i < QUALITY_OPTIONS; i++) {
				sum += qualityHist[i];

				if (sum >= target) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Calculates the exact average quality of all active products.
	 * Time Complexity: O(1) worst-case, utilizing the running sum cache.
	 *
	 * @return The average quality as a double, or -1 if the structure is empty.
	 */
	public double avgQuality() {
		if (productsCount != 0) {
			return sumQ / (double) productsCount;
		}
		return -1;
	}

	/**
	 * Applies a flat price raise to all products of a specific quality tier.
	 * Time Complexity: O(1) worst-case. Instead of iterating through products, the raise
	 * is lazily accumulated in the delta-tracking array.
	 *
	 * @param raise The flat integer amount to increase prices by.
	 * @param quality The quality tier (0-5) to apply the raise to.
	 */
	public void raisePrice(int raise, int quality) {
		raisedPrices[quality] += raise;
	}

	/**
	 * Identifies the single most expensive product across all quality tiers.
	 * Time Complexity: O(1) worst-case. Bypasses list iteration by checking the 6 buckets
	 * in the max cache array.
	 *
	 * @return A deep copy of the most expensive Product (with true price calculated), or null if empty.
	 */
	public Product mostExpensive() {
		Product mostExp = null;
		if (productsCount != 0) {
			for (int i = 0; i < QUALITY_OPTIONS; i++) { //T(n) = 6 which is O(1)
				Product qualityMax = maxPerQuality[i];
				if (qualityMax != null) {
					if (mostExp == null || (qualityMax.price() + raisedPrices[i] > mostExp.price() + raisedPrices[mostExp.quality()])) {
						mostExp = qualityMax;
					}
				}
			}
		}
        if (mostExp != null) {
            return new Product(mostExp.id(), mostExp.quality(), mostExp.price() + raisedPrices[mostExp.quality()], mostExp.name());
        }
        return mostExp;
	}
}
