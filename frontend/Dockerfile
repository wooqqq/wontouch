FROM nginx:alpine

COPY ./dist /usr/share/nginx/html

COPY ./nginx.template /etc/nginx/conf.d/default.conf.template

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]