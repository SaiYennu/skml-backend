package com.skml.util;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.skml.exception.InvoiceGenerationException;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadPdf(File file) {

        try {
        	Map<?, ?> result = cloudinary.uploader().upload(
        		    file,
        		    Map.of(
        		        "resource_type", "raw",   // 🔥 IMPORTANT
        		        "folder", "invoices",
        		        "use_filename", true,
        		        "unique_filename", false
        		    )
        		);


        	// 🔥 Normalize URL for PDF view
        	String secureUrl = result.get("secure_url").toString();

        	
        	return secureUrl;

        } catch (Exception e) {
            throw new InvoiceGenerationException("Cloudinary upload failed", e);
        }
    }
}
