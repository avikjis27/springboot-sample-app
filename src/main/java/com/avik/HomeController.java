package com.avik;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public String testService() {
		return "Hello - from boot service" + "\n" 
				+ "http://localhost:8080/putfile?bucketName=<NAME>&objectKey=<KEY>&region=<REGION>" + "\n"
				+ "http://localhost:8080/getfile?bucketName=<NAME>&objectKey=<KEY>&region=<REGION>" + "\n"
				+ "http://localhost:8080/deletefile?bucketName=<NAME>&objectKey=<KEY>&region=<REGION>"+ "\n";
	}
	
	@ResponseBody
	@RequestMapping(path="putfile", method = RequestMethod.GET)
	public String putObjectOverGet(@RequestParam(name = "bucketName") String bucket_name,
			@RequestParam(name = "objectKey") String object_key, @RequestParam(name = "region") String region) {
		System.out.format("Putting object to S3 bucket: %s of region: %s \n", object_key, bucket_name,region);
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
		File file = new File("temp"+Math.random());
		try {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return e.getMessage();
			}
			s3.putObject(new PutObjectRequest(bucket_name, object_key , file));
			file.delete();
			System.out.format("Put object %s from S3 bucket: %s of region: %s\n", object_key, bucket_name,region);
			return "Successfully put the key";
		} catch (AmazonServiceException e) {
			e.printStackTrace();
			return e.getErrorMessage();
		}
		finally {
			file.delete();
		}
	}
	
	@ResponseBody
	@RequestMapping(path="getfile", method = RequestMethod.GET)
	public String readObjectOverGet(@RequestParam(name = "bucketName") String bucket_name,
			@RequestParam(name = "objectKey") String object_key, @RequestParam(name = "region") String region) {
		System.out.format("Getting object %s from S3 bucket: %s of region: %s \n", object_key, bucket_name,region);
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
		try {
			S3Object object = s3.getObject(new GetObjectRequest(bucket_name, object_key));
			InputStream objectData = object.getObjectContent();			
			return getStringFromInputStream(objectData);
		} catch (AmazonServiceException e) {
			e.printStackTrace();
			return e.getErrorMessage();
		}
	}
	
	
	@ResponseBody
	@RequestMapping(path="deletefile", method = RequestMethod.GET)
	public String deleteObjectOverGet(@RequestParam(name = "bucketName") String bucket_name,
			@RequestParam(name = "objectKey") String object_key, @RequestParam(name = "region") String region) {
		System.out.format("Deleting object %s from S3 bucket: %s of region: %s \n", object_key, bucket_name,region);
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
		try {
			s3.deleteObject(bucket_name, object_key);
			System.out.format("Deleted object %s from S3 bucket: %s of region: %s\n", object_key, bucket_name,region);
			return "Successfully deleted the key";
		} catch (AmazonServiceException e) {
			e.printStackTrace();
			return e.getErrorMessage();
		}
	}
}