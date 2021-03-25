package com.toy.artifact.utils;

import com.toy.artifact.db.vo.HasId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Paginator<T> {
    private final int size;
    private final long total;
    private final long page;
    private final List<T> data;


    public int getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getData() {
        return data;
    }

    public long getPage() {
        return page;
    }

    public Paginator(List<T> data, long total, long page, int size) {
        this.size = size;
        this.total = total;
        this.page = page;
        this.data = data;
    }

    public <R> Paginator<R> map(Function<? super T, ? extends R> mapper) {
        List<R> trans = data.stream().map(mapper)
            .collect(Collectors.toList());
        return new Paginator<>(trans, total, page, size);
    }

    public Paginator<T> forEach(Consumer<? super T> mapper) {
        data.forEach(mapper);
        return this;
    }

    public List<Long> fetchKeys() {
        if (! data.isEmpty()) {
            if (data.get(0) instanceof HasId) {
                List<Long> list = new ArrayList<>();
                data.forEach(v -> list.add(((HasId) v).getId()));
                return list;
            }
            throw new RuntimeException("List item must implements HasId");
        } else {
            return Collections.emptyList();
        }
    }

    public <R> Paginator<R> fill(Function<? super List<Long>, ? extends Paginator<R>> f) {
        return f.apply(fetchKeys());
    }
}
