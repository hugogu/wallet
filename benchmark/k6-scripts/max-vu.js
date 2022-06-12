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
            'Content-Type': 'application/json',
            'X-Request-ID': uuidv4().toString()
        },
    };
    let request = {
        "from": "94e37be9-ce18-4fde-b8e1-6c6819b6035d",
        "to": "ac87fec9-b203-49a5-aab5-7749bef0ed5f",
        "monetary": {
            "amount": 20,
            "currency": "CNY"
        }
    }

    let res = http.post('http://localhost:8080/transfer', JSON.stringify(request), params);
    check(res, {
        'status was 200': r => r.status === 200,
        'not found': r => r.status === 404,
        'invalid': r => r.status === 400,
        'client error': r => r.status > 400 && r.status < 500,
        'server error': r => r.status >=500
    });
    sleep(0.05)
}
