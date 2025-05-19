FROM mcr.microsoft.com/playwright:v1.40.0-jammy

# Install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-11-jdk \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Install project dependencies
RUN mvn clean install -DskipTests

# Expose Playwright Grid port
EXPOSE 4444

# Start Playwright Grid
CMD ["npx", "playwright", "grid", "start"]
