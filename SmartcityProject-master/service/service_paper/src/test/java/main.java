import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BDQ<E>{
    private final int capicity;
    transient Node<E> first, last;
    private transient int count;
    final Lock lock = new ReentrantLock();
    final Condition notEmpty = lock.newCondition();
    final Condition notFull = lock.newCondition();

    BDQ(int captivity) {
        this.capicity = captivity;
    }
    static final class Node<E>{ E item; Node prev; Node next; Node(E x) { item = x; }}

    public boolean linkFirst(Node<E> node){
        if(count >= capicity)
            return false;
        Node<E> f = first;
        node.next = f;
        first = node;
        if(last == null)
            last = node;
        else
            f.prev = node;
        ++count;
        // 唤醒
        notEmpty.signal();
        return true;
    }

    public boolean linkLast(Node<E> node){
        if(count >= capicity)
            return false;
        Node<E> l = last;
        node.prev = l;
        last = node;
        if(first == null)
            first = node;
        else
            l.next = node;
        ++count;
        // 唤醒
        notEmpty.signal();
        return true;
    }

    private E unLinkFirst(){
        Node<E> f = first;
        if(f == null)
            return null;
        Node<E> n = f.next;
        E item = f.item;
        f.item = null;
        f.next = f;
        first = n;

        if(n == null)
            last = null;
        else
            n.prev = null;
        --count;
        notFull.signal();
        return item;
    }

    private E unLinkLast() throws InterruptedException {
        Node<E> l = last;
        if(l == null)
            notEmpty.await();
        l = last;
        Node<E> p = l.prev;
        E item = l.item;
        l.item = null;
        l.prev = p;
        last = p;

        if(p == null)
            first = null;
        else
            p.next = null;
        --count;
        notFull.signal();
        return item;
    }


    public void putFirst(E e) throws InterruptedException{
        if(e == null) throw new NullPointerException();
        Node<E> node = new Node<E>(e);
        final ReentrantLock lock = (ReentrantLock) this.lock;
        lock.lock();
        try{
            if (!linkFirst(node)){
                notFull.await();
            }
        }finally {
            lock.unlock();
        }
    }

    public void putLast(E e) throws InterruptedException{
        if(e == null) throw new NullPointerException();
        Node<E> node = new Node<E>(e);
        final ReentrantLock lock = (ReentrantLock) this.lock;
        lock.lock();
        try{
            while (!linkLast(node)){
                notFull.await();
            }
        }finally {
            lock.unlock();
        }
    }

    public E pollFirst() {
        final ReentrantLock lock = (ReentrantLock) this.lock;
        lock.lock();
        try {
            return unLinkFirst();
        } finally {
            lock.unlock();
        }
    }
    public E pollLast() {
        final ReentrantLock lock = (ReentrantLock) this.lock;
        lock.lock();
        E e=null;
        try {
            e = unLinkLast();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return e;
    }
}

public class main {
    public static void main(String[] args) throws InterruptedException {
        final BDQ blockingQueue3 = new BDQ<Integer>(2);
        final int threads = 2;
        final int times = 10;
        List<Thread> threadList = new ArrayList<>(threads * 2);
        long startTime = System.currentTimeMillis();
        //  创建两个线程，向队列中并发放入0-19，每次放10个数字
        for (int i = 0; i < threads; i++) {
            final int offset = i * times;
            Thread producer = new Thread(()->{
                for (int j = 0; j < times; j++) {
                    try {
                        blockingQueue3.putFirst(offset + j);
                        System.out.println("put:" + String.valueOf(offset + j));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadList.add(producer);
            producer.start();
        }
        //  创建两个线程，向队列中并发取走数字并打印
        for (int i = 0; i < threads; i++) {
            Thread consumer = new Thread(()->{
                for (int j = 0; j < times; j++) {
                    Object e = null;
                    e = blockingQueue3.pollLast();
                    System.out.println(e);
                }
            });
            threadList.add(consumer);
            consumer.start();
        }

        // 等待全部线程执行完毕
        for (Thread thread : threadList) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("总耗时：%.2fs", (endTime - startTime) / 1e3));
        return;
    }
}
