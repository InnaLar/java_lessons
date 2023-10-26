package org.example.hw4_1;

// todo: implement
public class MyArrayList<E> implements MyList<E> {

    private static final int CAPACITY = 10;
    // todo  static final

    private int size;
    private Object[] elementData;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(final Object o) {
        if (o == null) {
            return false;
        }
        for (Object elem : elementData) {
            if (elem.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(final E e) {
        if (size == CAPACITY) {
            // todo: CAPACITY -> DEFAULT_CAPACITY and 1.5 * size
            Object[] arrToExpend = new Object[(int) (1.5 * CAPACITY)];
            System.arraycopy(elementData, 0, arrToExpend, 0, size);
            arrToExpend[size] = e;
            elementData = arrToExpend;
        } else {
            elementData[size] = e;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(final Object o) {
        int counter = 0;
        for (Object elem : elementData) {
            if (elem.equals(o)) {
                // todo: System.arraycopy(elementData, 0, arrToExpend, 0, size);
                for (int i = counter; i < size - 1; i++) {
                    elementData[i] = elementData[i + 1];
                }
                elementData[size - 1] = null;
                size--;
                return true;
            }
            counter++;
        }
        return false;
    }

    @Override
    public void clear() {
        // todo: Arrays.fill(myArray, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(final int index) {
        if (elementData[index] != null) {

            return (E) elementData[index];

        }
        return null;
    }
}
