package org.example.Service;


import org.example.DemoUser.Leave_Apply;
import org.example.Mapper.Leave_ApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Leave_ApplyService {
    // @Autowired是Spring的自动注入
    @Autowired
    private Leave_ApplyMapper leave_applyMapper;
    public void insertLeave(Leave_Apply leaveApply){
        leave_applyMapper.insertLeave(leaveApply);
    }
    
    public List<Leave_Apply> getByUserId(Integer userId){
        return leave_applyMapper.getByUserId(userId);
    }
    
    public List<Leave_Apply> getByUsername(String username){
        return leave_applyMapper.getByUsername(username);
    }
    
    public List<Leave_Apply> listAll(){
        return leave_applyMapper.listAll();
    }
    
    public void updateStatus(Integer id,String status){
        leave_applyMapper.updateStatus(id,status);
    }
    
}
