import http from 'k6/http';
import { uuidv4 } from "https://jslib.k6.io/k6-utils/1.0.0/index.js";
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '3m30s', target: 200 },
        { duration: '1m', target: 200 },
        { duration: '30s', target: 0 },
    ],
};

export default function() {
    let params = {
        headers: {
            'Content-Type': 'application/json'
        },
    };
    let request = {
        "from": "ab1d4eac-40ad-44e8-875a-91a02af9e861",
        "to": "e45ef48c-d767-4278-bfd3-e6532d30c627",
        "monetary": {
            "amount": 20, "currency": "CNY"
        }
    }

    let res = http.post('http://localhost:8080/transfer', JSON.stringify(request), params);
    check(res, {
        'status was 200': r => r.status === 200,
        'not found': r => r.status === 404,
        'invalid': r => r.status === 400,
        'request error': r => r.status > 400,
        'server error': r => r.status >=500
    });
    sleep(0.05)
}
