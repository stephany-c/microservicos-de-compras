import { useState, useEffect } from 'react';
import { orderApi, userApi } from '../services/api';
import { jwtDecode } from 'jwt-decode';

interface OrderItem {
  id: string;
  productId: string;
  quantity: number;
}

interface Order {
  id: string;
  userId: string;
  productId: string;
  status: string;
}

export const Orders = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        const decoded: any = jwtDecode(token);
        const email = decoded.sub;
        
        // Get user ID first
        const userRes = await userApi.get(`/users/email?email=${email}`);
        const userId = userRes.data.id;
        
        // Get orders
        const ordersRes = await orderApi.get(`/orders/user/${userId}`);
        setOrders(ordersRes.data);
      }
    } catch (e) {
      console.error("Erro ao buscar pedidos", e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="page-title">Meus Pedidos</h1>
      
      {loading ? (
        <p>Carregando seus pedidos...</p>
      ) : orders.length === 0 ? (
        <div className="card text-center" style={{ padding: '3rem' }}>
          <p>Você ainda não fez nenhum pedido.</p>
        </div>
      ) : (
        <div className="space-y-4">
          {orders.map(order => (
            <div key={order.id} className="card" style={{ marginBottom: '1rem' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', borderBottom: '1px solid var(--border-color)', paddingBottom: '1rem', marginBottom: '1rem' }}>
                <div>
                  <h3 style={{ fontWeight: 'bold' }}>Pedido #{order.id.substring(0, 8)}</h3>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <span style={{ 
                    backgroundColor: 'var(--accent-primary)', 
                    color: 'white', 
                    padding: '0.3rem 0.8rem', 
                    borderRadius: '20px',
                    fontSize: '0.8rem',
                    fontWeight: 'bold'
                  }}>
                    {order.status === 'PAID' ? 'PAGO' : order.status}
                  </span>
                </div>
              </div>
              <div>
                <p style={{ fontWeight: 'bold', marginBottom: '0.5rem' }}>Produto ID: {order.productId}</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
