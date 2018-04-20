package map;

public class SimpleHashMap<K, V> implements Map<K, V> {
	Entry<K, V>[] table;
	int capacity, size;
	double loadFactor = 0.75;

	/**
	 * Constructs an empty hashmap with the default initial capacity (16) and
	 * the default load factor (0.75).
	 */
	public SimpleHashMap() {
		capacity = 16;
		table = (Entry<K, V>[]) new Entry[capacity];
	}

	/**
	 * Constructs an empty hashmap with the specified initial capacity and the
	 * default load factor (0.75).
	 */
	public SimpleHashMap(int capacity) {
		this.capacity = capacity;
		table = (Entry<K, V>[]) new Entry[capacity];
		size = 0;
	}

	public String show() {
		String str = "";
		for (int i = 0; i < table.length; i++) {
			str += (i + " ");
			Entry<K, V> temp = table[i];
			while (temp != null) {
				str += temp.toString() + " ";
				temp = temp.nextObj;
			}
			str += "\n";
		}
		return str;
	}

	public V get(Object key) {
		Entry<K, V> e = find(index((K) key), (K) key);
		if (e != null) {
			return e.getValue();
		}
		return null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public V put(K key, V value) {
		Entry<K, V> e = find(index(key), key);

		if (e != null) {
			return e.setValue(value);
		}

		e = new Entry<K, V>(key, value);

		if (table[index(key)] != null) {
			e.nextObj = table[index(key)];
		}

		table[index(key)] = e;
		size++;
		if ((double) size / capacity > loadFactor) {
			rehash();
		}

		return null;
	}

	private void rehash() {
		capacity *= 2;
		Entry<K, V>[] prevTable = table;
		table = (Entry<K, V>[]) new Entry[capacity];
		size = 0;

		for (Entry<K, V> e : prevTable) {
			while (e != null) {
				put(e.getKey(), e.getValue());
				e = e.nextObj;
			}
		}
	}

	public V remove(Object key) {
		Entry<K, V> e = table[index((K) key)];
		if (e != null) {
			if (e.getKey().equals(key)) {
				table[index((K) key)] = e.nextObj;
				size--;
				return e.getValue();
			} else {
				while (e.nextObj != null) {
					if (e.nextObj.getKey().equals(key)) {
						V v = e.nextObj.getValue();
						e.nextObj = e.nextObj.nextObj;
						size--;
						return v;
					}

					e = e.nextObj;
				}
			}
		}

		return null;
	}

	public int size() {
		return size;
	}

	private int index(K key) {
		return Math.abs(key.hashCode() % capacity);
	}

	private Entry<K, V> find(int index, K key) {
		Entry<K, V> e = table[index];
		while (e != null) {
			if (e.getKey().equals(key)) {
				return e;
			}
			e = e.nextObj;
		}
		return null;
	}

	public static class Entry<K, V> implements Map.Entry<K, V> {
		K key;
		V value;
		private Entry<K, V> nextObj;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		/*
		 * returns the old value!
		 */
		public V setValue(V value) {
			V oldV = this.value;
			this.value = value;
			return oldV;
		}

		public String toString() {
			return key.toString() + " = " + value.toString();
		}
	}

	public static void main(String[] args) {
		SimpleHashMap<String, Integer> m = new SimpleHashMap<String, Integer>(3);
		m.put("Alex", 123);
		System.out.println("---");
		m.put("Felix", 123);
		m.put("qweeqwqwe", 123);
		m.put("qwe", 123);
		System.out.println("---");
		m.put("Johan", 1);
		// m.put("Felix", 23);
		System.out.print(m.show());
		// System.out.println("---");
		// m.remove("Alex");
		// m.rehash();
		// m.rehash();
		// System.out.print(m.show());
	}
}
