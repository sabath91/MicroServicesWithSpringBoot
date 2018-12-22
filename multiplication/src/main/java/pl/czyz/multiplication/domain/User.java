package pl.czyz.multiplication.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class User {

  //    @Column(unique = true)
  private final String alias;
  @Id
  @GeneratedValue
  @Column(name = "USER_ID")
  private Long id;

  protected User() {
    this(null);
  }
}
