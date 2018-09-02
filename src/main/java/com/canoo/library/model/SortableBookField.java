package com.canoo.library.model;

import java.util.Comparator;

public enum SortableBookField {
    TITLE(Book.titleComparator()), AUTHOR(Book.authorComparator()), PUBLICATIONDATE(Book.publicationDateComparator());

    private Comparator<Book> comparator;

    SortableBookField(Comparator<Book> comparator){
        this.comparator = comparator;
    }

    public Comparator<Book> getComparator() {
        return comparator;
    }
}
