package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.AddressBook;
import com.it.mapper.AddressBookMapper;
import com.it.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * service
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements  AddressBookService{
}
