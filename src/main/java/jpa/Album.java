package jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "album_map")
@DiscriminatorValue("album") // 구분자에 대한 값을 지정하는 어노테이션
@Getter
@Setter
public class Album extends Item {
    private String artist;
}
