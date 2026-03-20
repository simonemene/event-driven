import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 50,              // utenti virtuali
  iterations: 2000,     // totale richieste
};

export default function () {
  const payload = JSON.stringify({
    name: "phone-" + Math.floor(Math.random() * 1000000),
    cost: 12.5
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  http.post('http://source:8080/api/source', payload, params);

  sleep(0.01);
}