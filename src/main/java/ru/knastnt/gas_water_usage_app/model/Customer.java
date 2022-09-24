package ru.knastnt.gas_water_usage_app.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.hasText;

@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String firstName;
    private String middleName;
    @Column(nullable = false)
    private String lastName;
    @OneToMany(mappedBy = "customer")
    private List<Account> accounts = new ArrayList<>();

    public String getDemoFio() {
        return Stream.of(
                firstName,
                middleName,
                hasText(lastName) ? lastName.substring(0, 1) + "." : null
        )
        .filter(StringUtils::hasText)
        .collect(Collectors.joining(" "));
    }
}
