package pro.sky.newmagicschool.mapper;

import org.springframework.stereotype.Component;
import pro.sky.newmagicschool.dto.AvatarDto;
import pro.sky.newmagicschool.entity.Avatar;

@Component
public class AvatarMapper {
    public AvatarDto toDto(Avatar avatar) {
        AvatarDto avatarDto = new AvatarDto();
        avatarDto.setId(avatar.getId());
        avatarDto.setMediaType(avatar.getMediaType());
        avatarDto.setFileSize(avatar.getFileSize());
        avatarDto.setFilePath(avatar.getFilePath());
        return avatarDto;
    }

    public Avatar toEntity(AvatarDto avatarDto) {
        Avatar avatar = new Avatar();
        avatar.setId(avatarDto.getId());
        avatar.setMediaType(avatarDto.getMediaType());
        avatar.setFileSize(avatarDto.getFileSize());
        avatar.setFilePath(avatarDto.getFilePath());
        return avatar;
    }
}
