#!/bin/sh
exec apt-get update
echo "install imagemagick and tesseract as dependencies"
exec apt-get -y install libjpeg-dev libpng-dev tesseract-ocr imagemagick
echo "The application will start in ${JHIPSTER_SLEEP}s..." && sleep ${JHIPSTER_SLEEP}
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.gitlab.amirmehdi.ETradeApp"  "$@"
