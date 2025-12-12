package com.example.login.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "http://localhost:3000")
public class FileUploadController {

    // 文件上传根目录
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/music-uploads/";

    // 头像上传目录
    private static final String AVATAR_DIR = UPLOAD_DIR + "avatars/";

    // 歌手图片上传目录
    private static final String SINGER_PIC_DIR = UPLOAD_DIR + "singers/";

    // 音乐文件上传目录
    private static final String SONG_DIR = UPLOAD_DIR + "songs/";

    // 歌曲封面上传目录
    private static final String SONG_PIC_DIR = UPLOAD_DIR + "song-pics/";

    public FileUploadController() {
        // 创建上传目录
        createDirectoryIfNotExists(AVATAR_DIR);
        createDirectoryIfNotExists(SINGER_PIC_DIR);
        createDirectoryIfNotExists(SONG_DIR);
        createDirectoryIfNotExists(SONG_PIC_DIR);
    }

    private void createDirectoryIfNotExists(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * 上传用户头像
     * POST /api/file/avatar
     */
    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, AVATAR_DIR, "avatars");
    }

    /**
     * 上传歌手图片
     * POST /api/file/singer-pic
     */
    @PostMapping("/singer-pic")
    public ResponseEntity<?> uploadSingerPic(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, SINGER_PIC_DIR, "singers");
    }

    /**
     * 上传歌曲文件
     * POST /api/file/song
     */
    @PostMapping("/song")
    public ResponseEntity<?> uploadSong(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, SONG_DIR, "songs");
    }

    /**
     * 上传歌曲封面
     * POST /api/file/song-pic
     */
    @PostMapping("/song-pic")
    public ResponseEntity<?> uploadSongPic(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, SONG_PIC_DIR, "song-pics");
    }

    /**
     * 通用文件上传方法
     */
    private ResponseEntity<?> uploadFile(MultipartFile file, String uploadDir, String urlPath) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "文件为空");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();

            // 生成唯一文件名
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // 保存文件
            Path filePath = Paths.get(uploadDir + uniqueFilename);
            Files.write(filePath, file.getBytes());

            // 返回文件访问URL
            String fileUrl = "/api/file/" + urlPath + "/" + uniqueFilename;

            response.put("success", true);
            response.put("message", "上传成功");
            response.put("url", fileUrl);
            response.put("filename", uniqueFilename);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "上传失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取头像文件
     * GET /api/file/avatars/{filename}
     */
    @GetMapping("/avatars/{filename}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String filename) {
        return getFile(AVATAR_DIR + filename);
    }

    /**
     * 获取歌手图片
     * GET /api/file/singers/{filename}
     */
    @GetMapping("/singers/{filename}")
    public ResponseEntity<byte[]> getSingerPic(@PathVariable String filename) {
        return getFile(SINGER_PIC_DIR + filename);
    }

    /**
     * 获取歌曲文件
     * GET /api/file/songs/{filename}
     */
    @GetMapping("/songs/{filename}")
    public ResponseEntity<byte[]> getSong(@PathVariable String filename) {
        return getFile(SONG_DIR + filename);
    }

    /**
     * 获取歌曲封面
     * GET /api/file/song-pics/{filename}
     */
    @GetMapping("/song-pics/{filename}")
    public ResponseEntity<byte[]> getSongPic(@PathVariable String filename) {
        return getFile(SONG_PIC_DIR + filename);
    }

    /**
     * 通用文件读取方法
     */
    private ResponseEntity<byte[]> getFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);

            // 设置Content-Type
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .body(data);

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 删除文件
     * DELETE /api/file/delete
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam String url) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 从URL中提取文件路径
            String filePath = UPLOAD_DIR;
            if (url.contains("/avatars/")) {
                filePath = AVATAR_DIR + url.substring(url.lastIndexOf("/") + 1);
            } else if (url.contains("/singers/")) {
                filePath = SINGER_PIC_DIR + url.substring(url.lastIndexOf("/") + 1);
            } else if (url.contains("/songs/")) {
                filePath = SONG_DIR + url.substring(url.lastIndexOf("/") + 1);
            } else if (url.contains("/song-pics/")) {
                filePath = SONG_PIC_DIR + url.substring(url.lastIndexOf("/") + 1);
            }

            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                response.put("success", true);
                response.put("message", "删除成功");
            } else {
                response.put("success", false);
                response.put("message", "文件不存在");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
