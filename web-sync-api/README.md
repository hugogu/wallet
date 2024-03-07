# Web API

## Query

### JSON API based

* 查询所有
  ```
  http://localhost:8080/json/httpCommandEntity
  ```
* 指定记录
  ```
  http://localhost:8080/json/httpCommandEntity/13262a6c-d36f-44e5-8fd8-6e060a54d001
  ```

* 分页查询
  ```
  http://localhost:8080/json/httpCommandEntity?page%5Bsize%5D=2&page%5Bnumber%5D=2
  ```
  
* 查询指定字段
  ```
  http://localhost:8080/json/httpCommandEntity?page%5Bsize%5D=2&page%5Bnumber%5D=2&fields%5BhttpCommandEntity%5D=commandTime
  ```
  
* 过滤条件
  ```
  http://localhost:8080/json/httpCommandEntity?page%5Bsize%5D=2&page%5Bnumber%5D=2&filter=createTime%3E2024-03-07T03:08:27.000Z
  ```
  
* 响应体中带上总页数
  ```
  &page%5Btotals%5D=true
  ```

* 关联查询
  ```
  
  ```

参考[文档](https://jsonapi.org/examples/)
