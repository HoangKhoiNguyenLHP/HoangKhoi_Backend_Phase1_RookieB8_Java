package nh.khoi.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService
{
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${CLOUD_NAME}") String CLOUD_NAME,
            @Value("${CLOUD_API_KEY}") String CLOUD_API_KEY,
            @Value("${CLOUD_API_SECRET}") String CLOUD_API_SECRET
    )
    {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", CLOUD_API_KEY,
                "api_secret", CLOUD_API_SECRET
        ));
    }

    public String uploadFile(MultipartFile file)
    {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        }
        catch (IOException e) {
            throw new RuntimeException("Error uploading file to Cloudinary", e);
        }
    }
}
