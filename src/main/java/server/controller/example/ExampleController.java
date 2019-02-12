package server.controller.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.tool.TreeData;
import server.tool.FileRec;
import server.tool.Res;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
//权限访问测试
@RestController
@RequestMapping("/test")
public class ExampleController {
    private final FileRec fileRec;
    private final TreeData buildTree;

    @Autowired
    public ExampleController(FileRec fileRec, TreeData buildTree) {
        this.fileRec = fileRec;
        this.buildTree = buildTree;
    }

    //获取当前用户相关信息。
    @PostMapping("infoByHeader")
    public Res getInfo( ) {
        Map<String, Object> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        long timeout = subject.getSession().getTimeout();
        Object obj = subject.getPrincipal();
        map.put("principal", obj);
        map.put("timeOut", timeout);
        return Res.success(map);
    }

    @RequestMapping("tree")
    public Res getTree(@RequestParam("c") Boolean compareSelfId, @RequestParam("r") Integer rootId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<TreeItem> li = new ArrayList<TreeItem>() {{
            add(new TreeItem(1, "name1", null));
            add(new TreeItem(2, "name2", 1));
            add(new TreeItem(3, "name3", 2));
            add(new TreeItem(4, "name4", 2));
            add(new TreeItem(5, "name5", null));
            add(new TreeItem(6, "name6", 2));
        }};
        JSONArray objects = buildTree.tree(li, rootId, compareSelfId == null ? false : compareSelfId, new HashMap<>());
        return Res.success(objects);
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public class TreeItem {
        private Integer id;
        private String name;
        private Integer parentId;
    }

    //-----以下为权限测试，若需测试权限功能，需将本controller访问url先加入到shiro的检测路径中。-----
    @RequestMapping("/everyone")
    public Res everyone() {
        return Res.success("WebController：everyone");
    }

    @RequestMapping("/require_user")
    @RequiresUser
    public Res requireUser() {
        return Res.success("WebController：You are user");
    }

    @RequestMapping("/require_auth")
    @RequiresAuthentication
    public Res requireAuth() {
        return Res.success("WebController：You are authenticated");
    }

    @RequestMapping("/require_role1")
    @RequiresRoles("role1")
    public Res requireRole1() {
        return Res.success("WebController：You are visiting require_role [role1]");
    }

    @RequestMapping("/require_role3")
    @RequiresRoles("role3")
    public Res requireRole3() {
        return Res.success("WebController：You are visiting require_role [role3]");
    }

    @RequestMapping("/require_role_permission")
    @RequiresPermissions(value = {"admin:view"})
    public Res requireRolePermission() {
        return Res.success("WebController：You are visiting permission require edit,view");
    }

    @RequestMapping("/require_permission")
    @RequiresPermissions(value = {"view", "edit"})
    public Res requirePermission() {
        return Res.success("WebController：You are visiting permission require edit,view");
    }
    //-------------------------------------------------------------------------------------------------

    //上传文件示例
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject fileUpload(@RequestParam("file") MultipartFile file) {
        return fileRec.fileUpload(file, "uploadTest/image");
    }

    //上传文件示例1
    @PostMapping(value = "/upload1")
    public JSONObject fileUpload1(MultipartFile file, String tip) {
        return fileRec.fileUpload(file, "uploadTest/image");
    }

}
