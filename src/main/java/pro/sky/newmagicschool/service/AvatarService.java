package pro.sky.newmagicschool.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.newmagicschool.dto.StudentDto;
import pro.sky.newmagicschool.entity.Avatar;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.mapper.StudentMapper;
import pro.sky.newmagicschool.repository.AvatarRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("${student.avatar.dir.path}")
    private String avatarDir;

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;
    private final StudentMapper studentMapper;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
        this.studentMapper = studentMapper;
    }


    public void uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
        StudentDto studentDto = studentService.findStudentById(studentId);
        Student student = studentMapper.toEntity(studentDto);

        Path avatarPath = Path.of(avatarDir, studentId + "." + getExtension(avatarFile.getOriginalFilename()));
        Files.createDirectories(avatarPath.getParent());
        Files.deleteIfExists(avatarPath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(avatarPath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(avatarPath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateImagePreview(avatarPath));

        avatarRepository.save(avatar);
    }

    public Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private byte[] generateImagePreview(Path avatarPath) throws IOException {
        try (InputStream is = Files.newInputStream(avatarPath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image,0,0,100, height,null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(avatarPath.getFileName().toString()), baos);
            return baos.toByteArray();

        }
    }

    private String getExtension(String avatarName) {
        return avatarName.substring(avatarName.lastIndexOf(".")+1);
    }

}
