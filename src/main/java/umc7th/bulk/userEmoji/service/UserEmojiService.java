package umc7th.bulk.userEmoji.service;

public interface UserEmojiService {
    int countEmojisByUser(Long groupId, Long receiverUserId);
}
