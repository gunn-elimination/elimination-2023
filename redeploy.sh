#!/usr/bin/env bash
git pull
docker compose -f compose-production.yml build
docker compose -f compose-production.yml down
docker compose -f compose-production.yml up -d