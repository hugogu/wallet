# Wallet Service
## Development

1. Build Image
    ```shell
    ./gradlew :web-api:bootBuildImage
    ```
2. Run
    ```shell
    docker-compose up -d
    ```
3. Benchmark
    ```shell
    docker run --network="host" -i loadimpact/k6 run --out influxdb=http://localhost:8086/k6  - < ./benchmark/k6-scripts/max-vu.js
    ```
