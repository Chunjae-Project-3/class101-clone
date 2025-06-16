package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tbl_category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_idx", columnDefinition = "int(11) not null comment '카테고리 인덱스'")
    private int categoryIdx;

    @Column(name = "category_name", columnDefinition = "varchar(100) not null comment '카테고리 이름'")
    private String categoryName;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategoryEntity> subCategories = new ArrayList<>();

}
