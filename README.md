# Wallet Service
## Development

确认本地Docker已经安装并启动。

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
   docker run --network="host" -i wallet-k6 run -o xk6-influxdb -e K6_INFLUXDB_ORGANIZATION='hugo' -e K6_INFLUXDB_BUCKET='k6' -e K6_INFLUXDB_TOKEN='secret_token' - < ./benchmark/k6-scripts/max-vu.js
   docker run --network="host" -i wallet-k6 run -o xk6-influxdb --out xk6-influxdb=http://localhost:8086/k6 - < ./benchmark/k6-scripts/max-vu.js
   
   docker run --network="host" -i wallet-k6 run -o experimental-prometheus-rw - < ./benchmark/k6-scripts/max-vu.js
   docker run -i wallet-k6 run -o experimental-prometheus-rw - < ./benchmark/k6-scripts/max-vu.js
    ```

## Run K6 with InfluxDB

1. Install [Go](https://go.dev/doc/install)
1. [Generate InfluxDB Token](https://docs.influxdata.com/influxdb/v2/admin/tokens/create-token/#manage-tokens-in-the-influxdb-ui)
1. Install K6
    ```shell
    brew install k6
    go install go.k6.io/xk6/cmd/xk6@latest
    export PATH=$(go env GOPATH)/bin:$PATH
    ```

```yaml
  k6:
    build: ./benchmark/k6
    ports:
      - "6565:6565"
    environment:
      - K6_INFLUXDB_ADDR=http://localhost:8086
      - K6_INFLUXDB_ORGANIZATION=hugo
      - K6_INFLUXDB_BUCKET=k6
      - K6_INFLUXDB_INSECURE=true
      - K6_INFLUXDB_TOKEN=secret_token
    volumes:
      - ./benchmark/k6-scripts:/scripts
```

```yaml
  k6:
    build: ./benchmark/k6
    ports:
      - "6565:6565"
    environment:
      - K6_PROMETHEUS_RW_SERVER_URL=http://localhost:9090/api/v1
      - K6_PROMETHEUS_RW_TREND_STATS=p(95),p(99),min,max
    volumes:
      - ./benchmark/k6-scripts:/scripts
```