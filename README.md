# springboot-sample-app

 - Go to the Project directory
 - Run mvn clean insatll
 - Run `sudo docker build -t s3-boot .`
 - Get the image id of **s3-boot** using `sudo docker images --all`
 - Run `sudo docker run -p 8080:8080 <imageid>`
 
# Upload in aws ecr


- try to run `docker run hello-world` withoud **sudo**. if not follow this [site](https://askubuntu.com/questions/477551/how-can-i-use-docker-without-sudo) to make it happen
- create an ECR Repository in correct region and follow below steps
- Go to the Project directory
- `eval $(aws ecr get-login --no-include-email --region us-east-2 --profile your-profile | sed 's|https://||')`
- `docker build -t test-boot-service .`
- `docker tag test-boot-service:latest XXXXXX.dkr.ecr.us-east-2.amazonaws.com/test-boot-service:latest`
- `docker push XXXXXX.dkr.ecr.us-east-2.amazonaws.com/test-boot-service:latest `
