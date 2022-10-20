package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * mapper接口
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
