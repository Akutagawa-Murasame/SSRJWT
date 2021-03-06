package org.mura.service;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:26
 *
 * service调用mapper使用tk-mybatis内置的函数进行方便的单表查询
 */
public interface IBaseService<T> {
    // Select
    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record 实体
     * @return java.util.List<T>
     */
    List<T> select(T record);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key 主键
     * @return T
     */
    T selectByPrimaryKey(Object key);

    /**
     * 查询全部结果，select(null)方法能达到同样的效果
     *
     * @return java.util.List<T>
     */
    List<T> selectAll();

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果时抛出异常，查询条件使用等号
     *
     * @param record 实体
     * @return 唯一结果
     */
    T selectOne(T record);

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record 实体
     * @return 满足条件的字段总数
     */
    int selectCount(T record);

    // Insert
    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record 实体
     * @return 成功插入的记录个数
     */
    int insert(T record);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record 实体
     * @return 成功插入的记录个数
     */
    int insertSelective(T record);

    // Update
    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record 唯一实体，因为主键是唯一的
     * @return 成功为1，失败为0
     */
    int updateByPrimaryKey(T record);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param record 唯一实体
     * @return 成功为1，失败为0
     */
    int updateByPrimaryKeySelective(T record);

    // Delete
    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record 实体
     * @return 成功删除的个数
     */
    int delete(T record);

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param key 主键
     * @return 成功为1，失败为0
     */
    int deleteByPrimaryKey(Object key);

    // Example
    /**
     * 根据Example条件进行查询，这个查询支持通过Example类指定查询列，通过selectProperties方法指定查询列
     *
     * @param example 查询的条件
     * @return java.util.List<T>
     */
    List<T> selectByExample(Object example);

    /**
     * 根据Example条件进行查询总数
     *
     * @param example 查询条件
     * @return 结果的数量
     */
    int selectCountByExample(Object example);

    /**
     * 根据Example条件更新实体record包含的全部属性，null值会被更新
     *
     * @param record 待更新的实体记录
     * @param example 更新条件
     * @return 更改的数量
     */
    int updateByExample(@Param("record") T record, @Param("example") Object example);

    /**
     * 根据Example条件更新实体record包含的不是null的属性值
     *
     * @param record 实体记录
     * @param example 更新条件
     * @return 更新的记录数量
     */
    int updateByExampleSelective(@Param("record") T record, @Param("example") Object example);

    /**
     * 根据Example条件删除数据
     * @param example 删除条件
     * @return 删除的数量
     */
    int deleteByExample(Object example);

    // RowBounds
    /**
     * 根据实体属性和RowBounds进行分页查询
     * @param record 查询实体
     * @param rowBounds 页数
     * @return java.util.List<T>
     */
    List<T> selectByRowBounds(T record, RowBounds rowBounds);

    /**
     * 根据example条件和RowBounds进行分页查询
     * @param example 查询条件
     * @param rowBounds 页数
     * @return java.util.List<T>
     */
    List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds);
}