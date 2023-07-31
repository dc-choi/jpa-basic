package jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "movie_map")
@DiscriminatorValue("movie")
@Getter
@Setter
public class Movie extends Item {
    private String actor;
}
