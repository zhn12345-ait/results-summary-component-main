package org.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private BookService bookService;

    @Resource
    private BorrowMapper borrowMapper;

    // 图书列表
    @GetMapping("/list")
    public List<Book> getList() {
        return bookMapper.selectList(null);
    }

    // 初始化图书
    @GetMapping("/initData")
    public String initData() {
        List<Book> list = new ArrayList<>();

        Book book1 = new Book(null,"红楼梦","曹雪芹","古典文学",0,"经典");
        Book book2 = new Book(null,"西游记","吴承恩","古典文学",0,"经典");
        Book book3 = new Book(null,"三国演义","罗贯中","古典文学",0,"经典");
        Book book4 = new Book(null,"水浒传","施耐庵","古典文学",0,"经典");
        Book book5 = new Book(null,"Java编程思想","埃克尔","计算机",0,"编程");
        Book book6 = new Book(null,"SpringBoot实战","张三","计算机",0,"编程");
        Book book7 = new Book(null,"MySQL从入门到精通","李四","计算机",0,"数据库");
        Book book8 = new Book(null,"人类简史","赫拉利","历史",0,"社科");

        list.add(book1);
        list.add(book2);
        list.add(book3);
        list.add(book4);
        list.add(book5);
        list.add(book6);
        list.add(book7);
        list.add(book8);

        bookService.saveBatch(list);
        return "初始化成功！共 " + list.size() + " 本图书";
    }

    // 新增图书
    @PostMapping("/add")
    public String addBook(String bookName, String author, String category, String type) {
        Book book = new Book();
        book.setBookName(bookName);
        book.setAuthor(author);
        book.setCategory(category);
        book.setType(type);
        book.setStatus(0);
        bookMapper.insert(book);
        return "新增成功";
    }

    // 编辑图书
    @PostMapping("/update")
    public String updateBook(Integer id, String bookName, String author, String category, String type) {
        Book book = bookMapper.selectById(id);
        if (book == null) return "图书不存在";
        book.setBookName(bookName);
        book.setAuthor(author);
        book.setCategory(category);
        book.setType(type);
        bookMapper.updateById(book);
        return "修改成功";
    }

    // ---------------------- 【借书：已修复！能记录谁借的】----------------------
    @PostMapping("/borrow")
    public String borrowBook(Integer id, String username) {
        Book book = bookMapper.selectById(id);
        if (book == null) return "图书不存在";
        if (book.getStatus() == 1) return "已借出";

        // 1. 修改状态
        book.setStatus(1);
        bookMapper.updateById(book);

        // 2. 插入借阅记录（带用户名！知道谁借的！）
        Borrow borrow = new Borrow();
        borrow.setBookId(id);
        borrow.setBookName(book.getBookName());
        borrow.setUsername(username); // 关键：记录借阅人！
        borrow.setBorrowTime(new java.util.Date());
        borrow.setStatus(0);
        borrowMapper.insert(borrow);

        return "借阅成功！";
    }

    // ---------------------- 【还书：已修复】----------------------
    @PostMapping("/return")
    public String returnBook(Integer id) {
        Book book = bookMapper.selectById(id);
        if (book == null) return "图书不存在";
        if (book.getStatus() == 0) return "未借出";

        book.setStatus(0);
        bookMapper.updateById(book);

        QueryWrapper<Borrow> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id", id).eq("status", 0);
        Borrow borrow = borrowMapper.selectOne(wrapper);
        if (borrow != null) {
            borrow.setStatus(1);
            borrow.setReturnTime(new java.util.Date());
            borrowMapper.updateById(borrow);
        }
        return "归还成功";
    }

    // 搜索（已修复）
    @GetMapping("/search")
    public List<Book> search(String bookName, String category) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (bookName != null && !bookName.isEmpty()) {
            wrapper.like("book_name", bookName);
        }
        if (category != null && !category.isEmpty()) {
            wrapper.like("category", category);
        }
        return bookMapper.selectList(wrapper);
    }

    // 删除
    @PostMapping("/delete")
    public String deleteBook(Integer id) {
        bookMapper.deleteById(id);
        return "删除成功";
    }

    // 按状态筛选
    @GetMapping("/filterByStatus")
    public List<Book> filterByStatus(Integer status) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return bookMapper.selectList(wrapper);
    }
}