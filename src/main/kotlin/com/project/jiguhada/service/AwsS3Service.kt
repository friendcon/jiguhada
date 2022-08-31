package com.project.jiguhada.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.*

@Service
class AwsS3Service(
    @Value("\${cloud.aws.s3.bucket}")
    var bucket: String,
    private val amazonS3Client: AmazonS3Client
) {
    fun uploadImgToTemp(multipartFile: MultipartFile): String{
        val fileName = multipartFile.name + UUID.randomUUID()

        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = multipartFile.size
        objectMetadata.contentType = multipartFile.contentType
        println("${bucket}/temp")
        return putS3("${bucket}/temp", fileName, multipartFile.inputStream, objectMetadata)
    }

    fun putS3(bucket: String, fileName: String, inputStream: InputStream, objectMetadata: ObjectMetadata): String {
        amazonS3Client.putObject(PutObjectRequest(bucket, fileName, inputStream, objectMetadata))
        return amazonS3Client.getResourceUrl(bucket, fileName).toString()
    }
}