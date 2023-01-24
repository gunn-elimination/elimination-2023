FROM node:lts-alpine as deps
RUN apk add --no-cache git libc6-compat
WORKDIR /app

COPY package.json yarn.lock* ./
RUN yarn install --frozen-lockfile

FROM node:lts-alpine as builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
ENV NODE_ENV production
RUN yarn generate

FROM nginx:stable-alpine as runner
COPY --from=builder /app/.output/public /usr/share/nginx/html