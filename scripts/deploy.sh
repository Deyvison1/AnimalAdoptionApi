#!/bin/bash

set -e

echo "================================="
echo "🚀 Deploy AnimalAdoption"
echo "================================="

echo "🐳 Rebuild e restart..."

docker compose up -d --build

echo "✅ Deploy concluído!"

docker ps --filter "name=api_animal_adoption"
