/*
 * Copyright 2016 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avik;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Controller
@RequestMapping("/")
public class HomeController {

	@RequestMapping(method = RequestMethod.GET)
	public String home(ModelMap model) {
		model.addAttribute("insertRecord", new Record());
		return "home";
	}
	@ResponseBody
	@RequestMapping(path="rest", method = RequestMethod.GET)
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
			return "Error";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String deleteObjectOverPost(ModelMap model, @ModelAttribute("insertRecord") @Valid Record record,
			BindingResult result) {
		if (!result.hasErrors()) {
			String bucket_name = record.getBucketName();
			String object_key = record.getObjectKey();
			String region = record.getRegion();
			System.out.format("Deleting object %s from S3 bucket: %s of region: %s \n", object_key, bucket_name,region);
			final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
			try {
				s3.deleteObject(bucket_name, object_key);
				System.out.format("Deleted object %s from S3 bucket: %s of region: %s\n", object_key, bucket_name,region);
				model.addAttribute("result", "Successfully deleted the key");
				return home(model);
			} catch (AmazonServiceException e) {
				e.printStackTrace();
				model.addAttribute("result", e.getErrorMessage());
				return home(model);
			}

		} else {
			System.out.format("Invalid request parameter");
			model.addAttribute("result", "Invalid request parameter");
			return home(model);
		}
	}
}