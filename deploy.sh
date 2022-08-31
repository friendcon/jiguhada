#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/jiguhada/deploy
cd $REPOSITORY

APP_NAME = jiguhada
JAR_NAME=$(ls $REPOSITORY | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료 할 프로세스가 없습니다"
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
echo "> nohup.out 권한 변경"
sudo chmod 755 nohup.out
sudo nohup java -jar $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &



