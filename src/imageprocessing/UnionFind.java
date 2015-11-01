package imageprocessing;

/**
 * The interface of union-find This does not have count method due to the
 * duplication with ComponentImage interface
 * 
 * @author Thai Kha Le
 *
 */
public interface UnionFind {
	
	/**
	 * Union two components
	 * @param p
	 * @param q
	 */
	void union(int p, int q);

	/**
	 * Chase the root of the component
	 * @param p
	 * @param depth initially 1
	 * @return root of component p and the depth
	 */
	int[] findRootR(int p, int depth);

	/**
	 * Check if two components are connected
	 * @param p
	 * @param q
	 * @return boolean value if they are connected
	 */
	boolean connected(int p, int q);
}