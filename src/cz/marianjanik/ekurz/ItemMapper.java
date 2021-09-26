package cz.marianjanik.ekurz;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ItemMapper {

    @Select("SELECT id, partNo, serialNo, name, description, numberInStock, price FROM table_items")
    List<Item> loadAllAvailableItems() throws SQLException;


    @Select("SELECT id, partNo, serialNo, name, description, numberInStock, price FROM table_items WHERE id=#{id}")
    Item loadItemById(Integer id) throws SQLException;

    @Delete("DELETE FROM table_items WHERE numberInStock=#{0}")
    void deleteAllOutOfStockItems() throws SQLException;


    @Insert("INSERT INTO table_items (partNo, serialNo, name, description, numberInStock, price) VALUES (#{partNo}, #{serialNo}, #{name}, #{description}, #{numberInStock}, #{price})")
    void saveItem(Item item) throws SQLException;


    @Update("UPDATE table_items SET price = #{price} WHERE id = #{id}")
    void updatePrice(Integer id, BigDecimal price) throws SQLException;
}
