package com.example.backend.controllers;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.backend.entity.ResponseBase;
import com.example.backend.services.AccessService;
import com.example.backend.services.UploadService;
import com.example.backend.services.utils.UploadGiteeImgBed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/upload")

public class UploadController {
    @Autowired
    private UploadService uploadService;
    @Autowired
    private AccessService accessService;
    /**
     *  上传图片
     * @param multipartFile 文件对象
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadAvatar")
    public ResponseEntity<ResponseBase> uploadImg(@RequestParam("file") MultipartFile multipartFile,
                                                  @RequestParam("accessToken") String accessToken) throws IOException {
        try {
            log.info("uploadImg()请求已来临...");
            //根据文件名生成指定的请求url
            int user_id = accessService.getAuthenticatedId(accessToken);
            String originalFilename = multipartFile.getOriginalFilename();
            String targetURL = UploadGiteeImgBed.createUploadFileUrl(originalFilename);
            log.info("目标url：" + targetURL);
            //请求体封装
            Map<String, Object> uploadBodyMap = UploadGiteeImgBed.getUploadBodyMap(multipartFile.getBytes());
            //借助HttpUtil工具类发送POST请求
            String JSONResult = HttpUtil.post(targetURL, uploadBodyMap);
            //解析响应JSON字符串
            JSONObject jsonObj = JSONUtil.parseObj(JSONResult);
            //请求失败
            if (jsonObj == null || jsonObj.getObj("commit") == null) {
                ResponseBase responseBase = new ResponseBase();
                responseBase.setMessage("上传gitee失败");
                responseBase.setStatus(1);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBase);
            }
            //请求成功：返回下载地址
            JSONObject content = JSONUtil.parseObj(jsonObj.getObj("content"));
            String downloadUrl = content.getStr("download_url");
            JSONObject data = new JSONObject();
            data.put("userAvatar", downloadUrl);
            int isSuccess = uploadService.uploadPersonalAvatar(user_id,downloadUrl);
            ResponseBase responseBase = new ResponseBase();
            responseBase.pushData(data);
            responseBase.setMessage("上传成功");
            return ResponseEntity.status(HttpStatus.OK).body(responseBase);
        }
        catch (IOException e) {
            ResponseBase responseBase = new ResponseBase();
            responseBase.setMessage("上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBase);
        }
    }
}
