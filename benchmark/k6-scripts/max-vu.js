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
        "from": "4682ef8c-5480-408a-b979-60378df6eb43",
        "to": "5e803bd2-3287-41b3-bf6b-6a87505b98c5",
        "monetary": {
            "amount": 20, "currency": "CNY"
        }
    }

    let res = http.post('http://localhost:8082/transfer', JSON.stringify(request), params);
    check(res, {
        'status was 200': r => r.status === 200,
        'not found': r => r.status === 404,
        'invalid': r => r.status === 400,
        'request error': r => r.status > 400,
        'server error': r => r.status >=500
    });
    sleep(0.05)
}
