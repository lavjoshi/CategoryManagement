package com.CategoryManagement.repository;

import com.CategoryManagement.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepositoryImpl extends JpaRepository<Category, Long> {


    Category findByName(String name);

    @Query(value = "select c.name from categories c where c.right_value=c.left_value+1", nativeQuery = true)
    Set<String> getLeafNodes();

    @Modifying
    @Query(value = "update categories set right_value = right_value + 2  where right_value > ?", nativeQuery = true)
    int updateRightValue(Integer value);

    @Modifying
    @Query(value = "update categories set left_value = left_value + 2  where left_value > ?", nativeQuery = true)
    int updateLeftValue(Integer value);

    @Modifying
    @Query(value = "update categories set right_value = right_value - ?  where right_value > ?", nativeQuery = true)
    int updateRightValue(Integer width, Integer value);

    @Modifying
    @Query(value = "update categories set left_value = left_value - ?  where left_value > ?", nativeQuery = true)
    int updateLeftValue(Integer width, Integer value);

    @Modifying
    @Query(value = "delete from categories where left_value between ? and ?", nativeQuery = true)
    int delete(Integer left, Integer right);

    @Query(value = "select id from categories where left_value between ? and ? order by left_value", nativeQuery = true)
    Set<Long> getChildCategoryIDs(Integer left, Integer right);


    @Query(value = "select parent.name from categories as node, categories as parent\n" +
            "where node.left_value between parent.left_value and parent.right_value\n" +
            "and node.id = ? order by parent.left_value;", nativeQuery = true)
    Set<String> getCategoryTreeById(Long id);


}
