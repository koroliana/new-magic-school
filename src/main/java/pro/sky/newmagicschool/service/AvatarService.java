package pro.sky.newmagicschool.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.newmagicschool.dto.AvatarDto;
import pro.sky.newmagicschool.entity.Avatar;
import pro.sky.newmagicschool.entity.Student;
import pro.sky.newmagicschool.exception.StudentNotFoundException;
import pro.sky.newmagicschool.mapper.AvatarMapper;
import pro.sky.newmagicschool.mapper.StudentMapper;
import pro.sky.newmagicschool.repository.AvatarRepository;
import pro.sky.newmagicschool.repository.StudentRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("${student.avatar.dir.path}")
    private String avatarDir;

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final AvatarMapper avatarMapper;



    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository, AvatarMapper avatarMapper) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.avatarMapper = avatarMapper;
    }


    public void uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("uploadAvatar method was invoked");

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        Path avatarPath = Path.of(avatarDir, studentId + "." + getExtension(Objects.requireNonNull(avatarFile.getOriginalFilename())));
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
        student.setAvatar(avatar);
        studentRepository.save(student);

    }

    public Avatar findAvatar(long studentId) {
        logger.info("findAvatar method was invoked");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private byte[] generateImagePreview(Path avatarPath) throws IOException {
        logger.info("generateImagePreview method was invoked");
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
        logger.info("getExtension method was invoked");
        return avatarName.substring(avatarName.lastIndexOf(".")+1);
    }

    public List<AvatarDto> getAll(int page, int size) {
        logger.info("getAll method was invoked");
        PageRequest pageRequest = PageRequest.of(page, size);
        return avatarRepository.findAll(pageRequest).getContent().stream()
                .map(avatarMapper::toDto)
                .collect(Collectors.toList());
    }
}
