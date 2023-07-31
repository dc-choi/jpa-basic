package jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book_map")
@DiscriminatorValue("book")
@Getter
@Setter
public class Book extends Item {
    private String author;
}
