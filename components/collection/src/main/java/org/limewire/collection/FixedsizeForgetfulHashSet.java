package org.limewire.collection;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

/**
 * Stores a fixed size of elements as a <code>Set</code> and removes elements 
 * when that size is reached. <code>FixedsizeForgetfulHashSet</code> is a 
 * <code>Set</code> version of {@link FixedsizeForgetfulHashMap}. Like 
 * <code>ForgetfulHashMap</code>, values are "forgotten" using a FIFO replacement 
 * policy.   
 * <p>
 * <code>FixedsizeForgetfulHashSet</code> works in constant time.
 * 
<pre>
    FixedsizeForgetfulHashSet&lt;String&gt; ffhs = new FixedsizeForgetfulHashSet&lt;String&gt;(4);

    ffhs.add("Abby");
    System.out.println(ffhs);
    if(!ffhs.add("Abby"))
        System.out.println("The set already contained that item; Set contents: " + ffhs);

    ffhs.add("Bob");
    ffhs.add("Bob");
    ffhs.add("Chris");

    ffhs.add("Dan");
    System.out.println(ffhs);   
    ffhs.add("Eric");
    System.out.println(ffhs);   

    Output:
        [Abby]
        The set already contained that item; Set contents: [Abby]
        [Abby, Bob, Chris, Dan]
        [Bob, Chris, Dan, Eric]

</pre>
* 
*/
public class FixedsizeForgetfulHashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable {

    /**
     * Backing map which the set delegates.
     */
    private transient FixedsizeForgetfulHashMap<E,Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    /**
     * Constructs a new, empty set.
     */
    public FixedsizeForgetfulHashSet(int size) {
        map = new FixedsizeForgetfulHashMap<E,Object>(size);
    }
    
    /**
     * Constructs a new, empty set, using the given initialCapacity.
     */
    public FixedsizeForgetfulHashSet(int size, int initialCapacity) {
        map = new FixedsizeForgetfulHashMap<E,Object>(size, initialCapacity);
    }
    
    /**
     * Constructs a new, empty set, using the given initialCapacity & loadFactor.
     */
    public FixedsizeForgetfulHashSet(int size, int initialCapacity, float loadFactor) {
        map = new FixedsizeForgetfulHashMap<E,Object>(size, initialCapacity, loadFactor);
    }
    
    /**
     * Tests if the set is full.
     * 
     * @return true, if the set is full (ie if adding any other entry will
     * lead to removal of some other entry to maintain the fixed-size property
     * of the set). Returns false, otherwise
     */
    public boolean isFull() {
        return map.isFull();
    }
    
    /**
     * Removes the least recently used entry from the set
     * @return The least recently used value from the set.
     * @modifies this.
     */
    public E removeLRUEntry() {
        if(isEmpty())
            return null;
        
        Iterator<E> i = iterator();
        E value = i.next();
        i.remove();
        return value;
    }

    /**
     * Returns an iterator over the elements in this set.  The elements
     * are returned in no particular order.
     *
     * @return an Iterator over the elements in this set.
     * @see ConcurrentModificationException
     */
    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * @return the number of elements in this set (its cardinality).
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * @return <tt>true</tt> if this set contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     *
     * @param o element whose presence in this set is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
     */
    @SuppressWarnings({"SuspiciousMethodCalls"})
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * Adds the specified element to this set if it is not already
     * present.
     *
     * @param o element to be added to this set.
     * @return <tt>true</tt> if the set did not already contain the specified
     * element.
     */
    @Override
    public boolean add(E o) {
        return map.put(o, PRESENT)==null;
    }

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param o object to be removed from this set, if present.
     * @return <tt>true</tt> if the set contained the specified element.
     */
    @Override
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }

    /**
     * Removes all of the elements from this set.
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * Returns a shallow copy of this <tt>FixedsizeForgetfulHashSet</tt> instance: the elements
     * themselves are not cloned.
     *
     * @return a shallow copy of this set.
     */
    @Override
    @SuppressWarnings("unchecked")
    public FixedsizeForgetfulHashSet<E> clone() throws CloneNotSupportedException {
        try { 
            FixedsizeForgetfulHashSet<E> newSet = (FixedsizeForgetfulHashSet<E>)super.clone();
            newSet.map = map.clone();
            return newSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
