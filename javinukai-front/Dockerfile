#Stage 1
FROM node:20-alpine as builder
WORKDIR /app
COPY package.json .
RUN npm i
COPY . .
RUN npm run build

#Stage 2
FROM nginx:1.24.0
RUN rm -f /etc/nginx/conf.d/default.conf
ADD ./nginx/default.conf /etc/nginx/conf.d
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=builder /app/dist .
ENTRYPOINT ["nginx", "-g", "daemon off;"]