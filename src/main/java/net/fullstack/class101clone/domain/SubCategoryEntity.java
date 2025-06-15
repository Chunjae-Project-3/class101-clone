package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_sub_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_category_idx")
    private int subCategoryIdx;

    @Column(name = "sub_category_name", nullable = false)
    private String subCategoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx", foreignKey = @ForeignKey(name = "FK_subcategory_category"))
    private CategoryEntity parentCategory;

    @OneToMany(mappedBy = "classSubCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassEntity> classList = new ArrayList<>();

}
