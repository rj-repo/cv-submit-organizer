@echo off
call mvn clean install -DskipTests
call docker compose build
call docker compose up