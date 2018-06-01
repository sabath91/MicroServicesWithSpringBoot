package pl.czyz.socialmultiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.jws.soap.SOAPBinding;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class User {

    private final String alias;

    protected User() {
        this(null);
    }

}
