package org.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Resource
    private BorrowMapper borrowMapper;

    // 查询某个用户的借阅记录
    @GetMapping("/myList")
    public List<Borrow> myList(@RequestParam String username) {
        QueryWrapper<Borrow> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).orderByDesc("borrow_time");
        return borrowMapper.selectList(wrapper);
    }

    @GetMapping("/allList")
    public List<Borrow> allList() {
        return borrowMapper.selectList(null);
    }
}