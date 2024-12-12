package com.shop.ecommerce.Utils;

public class SimilarityUtils {

    public static double cosineSimilarity(int[] vectorA, int[] vectorB) {
        int dotProduct = 0;
        int normA = 0;
        int normB = 0;

        // Tính tổng của dot product và chuẩn của mỗi vector
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }

        // Tránh chia cho 0
        if (normA == 0 || normB == 0) {
            return 0.0; // Trả về 0 nếu một trong các vector có chuẩn = 0 (không có tương tác)
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
