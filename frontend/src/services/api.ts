import axios from 'axios';

export const userApi = axios.create({
  baseURL: import.meta.env.VITE_USER_SERVICE_URL || 'http://localhost:8080',
});

export const productApi = axios.create({
  baseURL: import.meta.env.VITE_PRODUCT_SERVICE_URL || 'http://localhost:8081',
});

export const orderApi = axios.create({
  baseURL: import.meta.env.VITE_ORDER_SERVICE_URL || 'http://localhost:8082',
});

export const paymentApi = axios.create({
  baseURL: import.meta.env.VITE_PAYMENT_SERVICE_URL || 'http://localhost:8083',
});

export const cartApi = axios.create({
  baseURL: import.meta.env.VITE_CART_SERVICE_URL || 'http://localhost:8084',
});

// Interceptor to add token
const applyToken = (config: any) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
};

userApi.interceptors.request.use(applyToken);
productApi.interceptors.request.use(applyToken);
orderApi.interceptors.request.use(applyToken);
paymentApi.interceptors.request.use(applyToken);
cartApi.interceptors.request.use(applyToken);

