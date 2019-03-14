package com.harmy.util;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Description: 红黑树+链表， key值相同的插入链表
 * Created by za-hejun on 2019/3/14
 */
public class TreeList<K, V> {
    private final Comparator<? super K> comparatorKey;

    private final Comparator<? super V> comparatorValue;

    public TreeList(Comparator<? super K> comparatorKey, Comparator<? super V> comparatorValue){
        this.comparatorKey = comparatorKey;
        this.comparatorValue = comparatorValue;
    }

    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    static class Node<K, V>{
        K key;
        V value;
        Node<K, V> next;
        Node(){};

        Node(K k, V v, Node<K, V> next){
            this.key = k;
            this.value = v;
            this.next = next;
        }

    }

    static final class TreeNode<K, V> extends Node<K, V>{
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> parent;
        boolean color = true;

        TreeNode(K k, V v, TreeNode<K, V> parent){
            super(k, v, null);
            this.parent = parent;
        }
    }

    private transient TreeNode<K, V> root;

    final int compare(Object k1, Object k2) {
        return comparatorKey == null ? ((Comparable<? super K>)k1).compareTo((K)k2)
                : comparatorKey.compare((K)k1, (K)k2);
    }

    public V put(K key, V value){
        TreeNode<K, V> t = root;
        if (t == null) {
            compare(key, key);

            root = new TreeNode<>(key, value, null);
            return null;
        }
        int cmp;
        TreeNode<K,V> parent;
        do {
            parent = t;
            cmp = comparatorKey.compare(key, t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                //如果key相等的情况，直接往链表后面追加
                return addList(key, value, parent);
        } while (t != null);

        TreeNode<K,V> e = new TreeNode<>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        fixAfterInsertion(e);
        return null;
    }

    private V addList(K key, V value, Node<K, V> head){
        Node newNode = new Node();
        newNode.key = key;
        int cmp = comparatorValue.compare(value, head.value);
        if(cmp < 0){
            newNode.value = head.value;
            newNode.next = head.next;
            head.next = newNode;
            head.value = value;
        }else{
            newNode.value = value;
            newNode.next = head.next;
            head.next = newNode;
        }
        return value;
    }

    private void fixAfterInsertion(TreeNode<K,V> x) {
        x.color = RED;

        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                TreeNode<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                TreeNode<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    private static <K,V> boolean colorOf(TreeNode<K,V> p) {
        return (p == null ? BLACK : p.color);
    }

    private static <K,V> TreeNode<K,V> parentOf(TreeNode<K,V> p) {
        return (p == null ? null: p.parent);
    }

    private static <K,V> void setColor(TreeNode<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    /** From CLR */
    private void rotateLeft(TreeNode<K,V> p) {
        if (p != null) {
            TreeNode<K,V> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /** From CLR */
    private void rotateRight(TreeNode<K,V> p) {
        if (p != null) {
            TreeNode<K,V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }


    private static <K,V> TreeNode<K,V> leftOf(TreeNode<K,V> p) {
        return (p == null) ? null: p.left;
    }

    private static <K,V> TreeNode<K,V> rightOf(TreeNode<K,V> p) {
        return (p == null) ? null: p.right;
    }


    public final TreeNode<K,V> getFirstEntry() {
        TreeNode<K,V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    static <K,V> TreeNode<K,V> successor(TreeNode<K,V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            TreeNode<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            TreeNode<K,V> p = t.parent;
            TreeNode<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (TreeNode<K, V> e = getFirstEntry(); e != null; e = successor(e)) {
            action.accept(e.key, e.value);
        }
    }

    public void forEachNode(Consumer<? super V> action){
        Objects.requireNonNull(action);
        for(TreeNode<K, V> e = getFirstEntry(); e!= null; e = successor(e)){
            Node<K, V> node = e;
            do{
                action.accept(node.value);

            }while((node = node.next) != null);
        }
    }
}
