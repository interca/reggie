package com.it.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.it.entity.AddressBook;
import com.it.entity.User;
import com.it.service.AddressBookService;
import com.it.utli.BaseContext;
import com.it.utli.SystemJsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public SystemJsonResponse save(HttpServletRequest request, @RequestBody AddressBook addressBook) {
        Long  id = (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(id);
        addressBook.setCreateTime(LocalDateTime.now());
        addressBook.setUpdateTime(LocalDateTime.now());
        addressBook.setUpdateUser(id);
        addressBook.setCreateUser(id);
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return SystemJsonResponse.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public SystemJsonResponse setDefault(HttpServletRequest request,@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, (Long) request.getSession().getAttribute("user"));
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBook.setUserId((Long) request.getSession().getAttribute("user"));
        addressBook.setUpdateTime(LocalDateTime.now());
        addressBookService.updateById(addressBook);
        return SystemJsonResponse.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public SystemJsonResponse get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return SystemJsonResponse.success(addressBook);
        } else {
            return SystemJsonResponse.fail("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public SystemJsonResponse getDefault(HttpServletRequest request) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, (Long) request.getSession().getAttribute("user"));
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return SystemJsonResponse.fail("没有找到该对象");
        } else {
            return SystemJsonResponse.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public SystemJsonResponse list(HttpServletRequest request,AddressBook addressBook) {
        addressBook.setUserId((Long) request.getSession().getAttribute("user"));
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return SystemJsonResponse.success(addressBookService.list(queryWrapper));
    }
}
