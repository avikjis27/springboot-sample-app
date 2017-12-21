# springboot-sample-app

 - Run `maven install`
 - Go to the Project directory
 - Run `sudo docker build -t s3-boot .`
 - Get the image id of **s3-boot** using `sudo docker images --all`
 - Run `sudo docker run -p 8080:8080 <imageid>`
 - Test `curl "http://localhost:8080"`
 - Sample output:
 `
Hello - from boot service
http://localhost:8080/putfile?bucketName=<NAME>&objectKey=<KEY>&region=<REGION>
http://localhost:8080/getfile?bucketName=<NAME>&objectKey=<KEY>&region=<REGION>
http://localhost:8080/deletefile?bucketName=<NAME>&objectKey=<KEY>&region=<REGION>
 
 `
 
# Upload in aws ecr


- try to run `docker run hello-world` without **sudo**. if not follow this [site](https://askubuntu.com/questions/477551/how-can-i-use-docker-without-sudo) to make it happen
- create an ECR Repository in correct region and follow below steps
- Go to the Project directory
- `eval $(aws ecr get-login --no-include-email --region us-east-2 --profile your-profile | sed 's|https://||')`
- `docker build -t test-boot-service .`
- `docker tag test-boot-service:latest XXXXXX.dkr.ecr.us-east-2.amazonaws.com/test-boot-service:latest`
- `docker push XXXXXX.dkr.ecr.us-east-2.amazonaws.com/test-boot-service:latest `