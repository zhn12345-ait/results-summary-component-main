package org.example.Controller;

import org.example.DemoUser.Demouser;
import org.example.DemoUser.Leave_Apply;
import org.example.Service.DemouserService;
import org.example.Service.Leave_ApplyService;
import org.example.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/leave")
public class Leave_ApplyController {
    @Autowired
    private Leave_ApplyService leave_applyService;
    
    @Autowired
    private DemouserService demouserService;
    
    @PostMapping("/insertLeave")
    public Result<?> insertLeave(@RequestBody Leave_Apply leave, HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        leave.setUsername(username);
        // 查询用户ID并设置
        Demouser user = demouserService.login(username);
        if (user != null) {
            leave.setUserId(user.getId());
        }
        // 默认状态为待审批
        if (leave.getStatus() == null || leave.getStatus().isEmpty()) {
            leave.setStatus("pending");
        }
        leave_applyService.insertLeave(leave);
        return Result.success("提交成功");
    }
    
    @GetMapping("/UserIdLeave")
    public Result<?> getAllPaperByUserId(HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        List<Leave_Apply> list = leave_applyService.getByUsername(username);
        return Result.success(list);
    }
    
    @GetMapping("/listAllLeave")
    public Result<?> getAllPaper(HttpServletRequest request, @RequestParam(required = false) String username){
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden();
        }
        List<Leave_Apply> list;
        if (username != null && !username.isEmpty()) {
            // 按用户名搜索
            list = leave_applyService.getByUsername(username);
        } else {
            list = leave_applyService.listAll();
        }
        return Result.success(list);
    }
    
    @PostMapping("/updateStatus")
    public Result<?> updateStatus(@RequestBody Map<String, Object> params, HttpServletRequest request){
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden();
        }
        Integer leaveId = ((Number) params.get("id")).intValue();
        String status = (String) params.get("status");
        leave_applyService.updateStatus(leaveId, status);
        return Result.success("操作成功");
    }
    
    // 审批通过
    @GetMapping("/approve")
    public Result<?> approveLeave(HttpServletRequest request, @RequestParam Integer id){
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden();
        }
        leave_applyService.updateStatus(id, "approved");
        return Result.success("审批通过");
    }
    
    // 拒绝审批
    @GetMapping("/reject")
    public Result<?> rejectLeave(HttpServletRequest request, @RequestParam Integer id){
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden();
        }
        leave_applyService.updateStatus(id, "rejected");
        return Result.success("已拒绝");
    }
}
