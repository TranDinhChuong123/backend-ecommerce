# Sử dụng openjdk 17 làm base image
FROM openjdk:17-jdk-alpine as builder

# Thiết lập thư mục làm việc cho ứng dụng
WORKDIR /app

# Sao chép mã nguồn vào container
COPY . /app

# Cấp quyền thực thi cho gradlew
RUN chmod +x gradlew

# Build ứng dụng và tạo JAR file
RUN ./gradlew bootJar --no-daemon

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine

# Thiết lập thư mục làm việc cho ứng dụng
WORKDIR /app

# Sao chép file JAR từ stage builder
COPY --from=builder /app/build/libs/ecommerce-0.0.1-SNAPSHOT.jar /app/ecommerce.jar

# Expose port mà ứng dụng sẽ chạy
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app/ecommerce.jar"]
