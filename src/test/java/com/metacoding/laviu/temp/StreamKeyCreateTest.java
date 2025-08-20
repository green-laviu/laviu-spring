package com.metacoding.laviu.temp;

import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class StreamKeyCreateTest {

    private static final String SECRET_KEY = "메타코딩시크릿키"; // 16자리 비밀키

    // 📌 스트림 키 생성 (AES 암호화)
    public static String generateStreamKey(Integer userId, Integer streamId) {
        try {
            String data = userId + ":" + streamId;

            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("스트림 키 생성 실패", e);
        }
    }

    // 📌 스트림 키 복호화
    public static Integer[] parseStreamKey(String streamKey) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decoded = Base64.getDecoder().decode(streamKey);
            byte[] decrypted = cipher.doFinal(decoded);

            String data = new String(decrypted);
            String[] parts = data.split(":");

            return new Integer[]{
                    Integer.parseInt(parts[0]), // userId
                    Integer.parseInt(parts[1])  // streamId
            };

        } catch (Exception e) {
            throw new RuntimeException("잘못된 스트림 키", e);
        }
    }

    @Test
    public void generateStreamKeyTest() {
        Integer userId = 4;
        Integer streamId = 4;
        String streamKey = generateStreamKey(userId, streamId);
        System.out.println(streamKey);
    }
}
