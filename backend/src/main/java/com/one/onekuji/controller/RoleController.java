//package com.one.onekuji.controller;
//
//import com.one.onekuji.service.RoleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/role")
//public class RoleController {
//
//
//    @Autowired
//    private RoleService roleService;
//
//
//    @GetMapping("/query")
//    public ResponseEntity<List<Role>> getAllRole(){
//        List<Role> roleList = roleService.getAllRole();
//        return ResponseEntity.ok(roleList);
//    }
//
//    @GetMapping("/{roleId}")
//    public ResponseEntity<Role> getRoleById(Integer roleId){
//        Role role = roleService.getRoleById(roleId);
//        return ResponseEntity.ok(role);
//    }
//}
