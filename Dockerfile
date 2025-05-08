FROM mcr.microsoft.com/playwright/java:v1.44.0-jammy

WORKDIR /app
COPY . /app

# Cấp quyền cho Maven Wrapper nếu có
RUN chmod +x mvnw

# Cài đặt dependencies
RUN ./mvnw clean install -DskipTests

# Mặc định sẽ chạy test
CMD ["./mvnw", "test"]
