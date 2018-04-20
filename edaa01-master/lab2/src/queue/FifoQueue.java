package queue;
import java.util.*;

public class FifoQueue<E> extends AbstractQueue<E> implements Queue<E> {
	private QueueNode<E> last;
	private int size;

	public FifoQueue() {
		size = 0;
	}

	/**	
	 * Returns an iterator over the elements in this queue
	 * @return an iterator over the elements in this queue
	 */	
	public Iterator<E> iterator() {
		return new QueueIterator();
	}
	private class QueueIterator implements Iterator<E>{
		private QueueNode<E> pos;
		private QueueIterator() {
			if(last == null) {
				pos = null;
			}
			else {
				pos = last.next;
			}
		}
		public boolean hasNext() {
			return pos != null;
		}
		public E next() {
			if(hasNext()) {
				E ele = pos.element;
				pos = pos.next;
				if(pos.equals(last.next)) {
					pos = null;
				}
				return ele;
			}
			throw new NoSuchElementException();
			
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**	
	 * Returns the number of elements in this queue
	 * @return the number of elements in this queue
	 */
	public int size() {		
		return size;
	}

	/**	
	 * Inserts the specified element into this queue, if possible
	 * post:	The specified element is added to the rear of this queue
	 * @param	x the element to insert
	 * @return	true if it was possible to add the element 
	 * 			to this queue, else false
	 */
	public boolean offer(E x) {
		QueueNode<E> ex = new QueueNode(x);
		if(last == null	) {
			last = ex;
			ex.next = ex;
		}
		else {
			ex.next = last.next;
			last.next = ex;
			last = ex;
		}
		size++;
		return true;
	}

	/**	
	 * Retrieves and removes the head of this queue, 
	 * or null if this queue is empty.
	 * post:	the head of the queue is removed if it was not empty
	 * @return 	the head of this queue, or null if the queue is empty 
	 */
	public E poll() {
		if(last == null) {
			return null;
		}
		E ele = last.next.element;
		if(size == 1) {
			last = null;
		}
		else {
			last.next = last.next.next;
		}
		size--;
		return ele;
	}

	/**	
	 * Retrieves, but does not remove, the head of this queue, 
	 * returning null if this queue is empty
	 * @return 	the head element of this queue, or null 
	 * 			if this queue is empty
	 */
	public E peek() {
		if(last == null) {
			return null;
		}
		return last.next.element;
	}
	/**
	 * Appends the specified queue to this queue
	 * post: all elements from the specified queue are appended
	 * to this queue. the specified queue (q) is empty
	 * @param q the queue to append
	 */
	public void append(FifoQueue<E> q) {
		for(int i = 0; i<q.size(); i++) {
			if(last == null && q.last != null) {
				last = q.last;
			}
			else if(last != null && q.last != null) {
				QueueNode temp = last.next;
				last.next = q.last.next;
				q.last.next = temp;
			}
		}
		size +=q.size();
	}


	private static class QueueNode<E> {
		E element;
		QueueNode<E> next;

		private QueueNode(E x) {
			element = x;
			next = null;
		}

	}

}
