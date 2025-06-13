# ====================================
# AFTAS Frontend - Multi-stage Dockerfile
# ====================================

# Build stage
FROM node:18-alpine AS build
WORKDIR /app

# Copy package files and install dependencies (cached layer)
COPY package*.json ./
RUN npm ci --only=production

# Copy source code and build application
COPY . .
RUN npm run build --prod

# Runtime stage with Nginx
FROM nginx:1.25-alpine
WORKDIR /usr/share/nginx/html

# Create non-root user for security
RUN addgroup -g 1001 -S aftas && \
    adduser -S aftas -u 1001

# Copy built application from build stage
COPY --from=build /app/dist/wi-gold-fish-front ./

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Change ownership to non-root user
RUN chown -R aftas:aftas /usr/share/nginx/html && \
    chown -R aftas:aftas /var/cache/nginx && \
    chown -R aftas:aftas /var/log/nginx && \
    chown -R aftas:aftas /etc/nginx/conf.d
RUN touch /var/run/nginx.pid && \
    chown -R aftas:aftas /var/run/nginx.pid

USER aftas

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:80/ || exit 1

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
